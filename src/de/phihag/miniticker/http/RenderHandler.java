package de.phihag.miniticker.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.phihag.miniticker.Event;
import de.phihag.miniticker.Match;
import de.phihag.miniticker.Renderer;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class RenderHandler extends SimpleHandler {
	private Event ev;
	private Renderer renderer;

	protected RenderHandler(String pathPrefix, Event ev, Renderer renderer) {
		super(pathPrefix);
		this.ev = ev;
		this.renderer = renderer;
	}

	@Override
	protected Response handle(boolean isGET, String relPath,
			IHTTPSession session) {
		
		Pattern pattern = Pattern.compile("^Spiel_([0-9]+).html");
		Matcher matcher = pattern.matcher(relPath);
		if (matcher.find()) {
			int matchIdx = Integer.valueOf(matcher.group(1)) - 1;
			if ((ev.matches != null) && (matchIdx >= 0) && (matchIdx <= ev.matches.length)) {
				Match m = ev.matches[matchIdx];
				String html = renderer.renderMatch(ev, m);
				return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "text/html", html);
			}
		}

		if (relPath.equals("Livescore.html")) {
			String html = renderer.renderLivescore(ev);
			return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "text/html", html);
		}
		if (relPath.equals("Gesamtstand.html")) {
			String html = renderer.renderOverview(ev);
			return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "text/html", html);
		}
		
		return HTTPUtils.error(404, "Not found");
	}
}
