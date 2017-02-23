package de.phihag.miniticker;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class RedirectHandler implements HttpHandler {
	private String path;
	private String redirect;
	
	public RedirectHandler(String path, String redirect) {
		this.path = path;
		this.redirect = redirect;
	}

	public static void create(HttpServer hs, String path, String redirect) {
		hs.createContext(path, new RedirectHandler(path, redirect));
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		String method = he.getRequestMethod(); 
		if (! ("HEAD".equals(method) || "GET".equals(method))) {
			HTTPUtils.sendError(he, 501, "Unsupported HTTP method");
			return;
		}
		
		String reqPath = he.getRequestURI().getPath();
		if (!reqPath.equals(this.path)) {
			HTTPUtils.sendError(he, 404, "Not in URL space");
			return;
		}

		he.getResponseHeaders().set("Location", redirect);
		he.sendResponseHeaders(302, -1);
		he.close();
	}

}
