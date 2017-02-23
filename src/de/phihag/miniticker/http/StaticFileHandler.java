package de.phihag.miniticker.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class StaticFileHandler extends SimpleHandler {
	private static final Map<String,String> MIME_MAP = new HashMap<>();
	static {
		MIME_MAP.put("appcache", "text/cache-manifest");
		MIME_MAP.put("css", "text/css");
		MIME_MAP.put("gif", "image/gif");
		MIME_MAP.put("html", "text/html");
		MIME_MAP.put("js", "application/javascript");
		MIME_MAP.put("json", "application/json");
		MIME_MAP.put("jpg", "image/jpeg");
		MIME_MAP.put("jpeg", "image/jpeg");
		MIME_MAP.put("mp4", "video/mp4");
		MIME_MAP.put("pdf", "application/pdf");
		MIME_MAP.put("png", "image/png");
		MIME_MAP.put("svg", "image/svg+xml");
		MIME_MAP.put("xlsm", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		MIME_MAP.put("xml", "application/xml");
		MIME_MAP.put("zip", "application/zip");
		MIME_MAP.put("md", "text/plain");
		MIME_MAP.put("txt", "text/plain");
		MIME_MAP.put("php", "text/plain");
	};

	private String filesystemRoot;
	private String directoryIndex;
	
	/**
	 * @param urlPrefix The prefix of all URLs.
	 * 			         This is the first argument to createContext. Must start and end in a slash.
	 * @param filesystemRoot The root directory in the filesystem.
	 *                       Only files under this directory will be served to the client.
	 *                       For instance "./staticfiles".
	 * @param directoryIndex File to show when a directory is requested, e.g. "index.html".
	 */
	public StaticFileHandler(String urlPrefix, String filesystemRoot, String directoryIndex) {
		super(urlPrefix);

		if (!urlPrefix.startsWith("/")) {
			throw new RuntimeException("pathPrefix does not start with a slash");
		}
		if (!urlPrefix.endsWith("/")) {
			throw new RuntimeException("pathPrefix does not end with a slash");
		}

		assert filesystemRoot.endsWith("/");
		try {
			this.filesystemRoot = new File(filesystemRoot).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		this.directoryIndex = directoryIndex;
	}

	// This is one function to avoid giving away where we failed 
	private Response reportPathTraversal() {
		return HTTPUtils.error(400, "Path traversal attempt detected");
	}

	public static String getExt(String path) {
		int slashIndex = path.lastIndexOf('/');
		String basename = (slashIndex < 0) ? path : path.substring(slashIndex + 1);

		int dotIndex = basename.lastIndexOf('.');
		if (dotIndex >= 0) {
			return basename.substring(dotIndex + 1);
		} else {
			return "";
		}
	}

	public static String lookupMime(String path) {
		String ext = getExt(path).toLowerCase();
		return MIME_MAP.getOrDefault(ext, "application/octet-stream");
	}

	@Override
	protected Response handle(boolean isGET, String relPath,
			IHTTPSession session) {
		if (relPath.endsWith("/") || relPath.equals("")) {
			relPath += directoryIndex;
		}
		
		File f = new File(filesystemRoot, relPath);
		File canonicalFile;
		try {
			canonicalFile = f.getCanonicalFile();
		} catch (IOException e) {
			// This may be more benign (i.e. not an attack, just a 403),
			// but we don't want the attacker to be able to discern the difference.
			return reportPathTraversal();
		}
		
		String canonicalPath = canonicalFile.getPath();
		if (! canonicalPath.startsWith(filesystemRoot)) {
			return reportPathTraversal();
		}

		FileInputStream fis;
		try {
			fis = new FileInputStream(canonicalFile);
		} catch (FileNotFoundException e) {
			// The file may also be forbidden to us instead of missing, but we're leaking less information this way 
			return HTTPUtils.error(404, "File not found"); 
		}

		String mimeType = lookupMime(relPath);
		Response res = NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), mimeType, fis, f.length());
		res.addHeader("Content-Type", mimeType);
		res.addHeader("X-Content-Type-Options", "nosniff");
		return res;
	}
}
