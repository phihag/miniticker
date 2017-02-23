package de.phihag.miniticker.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.phihag.miniticker.Config;
import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
	private NanoHandler[] handlers;

	public WebServer(Config config) {
		super(config.webPort);
		this.handlers = new NanoHandler[] {
			new StaticFileHandler("/bup/", config.bupLocation, config.bupIndex),
			new RedirectHandler("/", "/bup/#mt") 
		};
	}
	
	public Response serve(IHTTPSession session) {
		String path = session.getUri();
		Pattern pattern = Pattern.compile("^https?://[^/]+(/.*)$");
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			path = matcher.group(1);
		}

		for (NanoHandler h: handlers) {
			if (h.matches(path)) {
				return h.handleAbsPath(path, session);
			}
		}
		return HTTPUtils.error(404, "Not found");
	}
}
