package de.phihag.miniticker.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.phihag.miniticker.Config;
import de.phihag.miniticker.Event;
import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
	private NanoHandler[] handlers;

	public WebServer(Config config, Event ev) {
		super(config.webPort);
		this.handlers = new NanoHandler[] {
			new StaticFileHandler("/bup/", config.bupLocation, config.bupIndex),
			new StaticFileHandler("/data/", config.dataLocation, "index.html"),
			new SelectEventHandler("/select_event", ev),
			new GetEventHandler("/get_event", ev),
			new SetPlayersHandler("/set_players", ev),
			new SetScoreHandler("/set_score", ev),
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
