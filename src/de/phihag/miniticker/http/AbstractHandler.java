package de.phihag.miniticker.http;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public abstract class AbstractHandler {
	private String pathPrefix;

	protected AbstractHandler(String pathPrefix) {
		assert pathPrefix.startsWith("/");
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
