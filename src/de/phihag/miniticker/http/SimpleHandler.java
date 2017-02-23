package de.phihag.miniticker.http;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;

/**
 * A handler that only supports GET and POST.
 */
public abstract class SimpleHandler extends NanoHandler {
	protected SimpleHandler(String pathPrefix) {
		super(pathPrefix);
	}

	protected Response handle(String relPath, IHTTPSession session) {
		Method method = session.getMethod();
		boolean isGET = method.equals(Method.GET);
		boolean isHEAD = method.equals(Method.HEAD);
		if (! (isGET || isHEAD)) {
			return HTTPUtils.error(501, "Unsupported HTTP method");
		}
		return handle(isGET, relPath, session);
	}

	abstract protected Response handle(boolean isGET, String relPath, IHTTPSession session);
}
