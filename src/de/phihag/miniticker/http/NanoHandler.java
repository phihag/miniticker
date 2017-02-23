package de.phihag.miniticker.http;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public abstract class NanoHandler {
	private String pathPrefix;

	protected NanoHandler(String pathPrefix) {
		assert pathPrefix.startsWith("/");
		assert pathPrefix.endsWith("/");
		this.pathPrefix = pathPrefix;
	}
	
	public boolean matches(String path) {
		return path.startsWith(this.pathPrefix);
	}
	
	public Response handleAbsPath(String absPath, IHTTPSession session) {
		assert absPath.startsWith(pathPrefix);
		String relPath = absPath.substring(pathPrefix.length());
		return handle(relPath, session);
	}

	protected abstract Response handle(String relativePath, IHTTPSession session);
}
