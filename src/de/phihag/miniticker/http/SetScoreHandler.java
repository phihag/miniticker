package de.phihag.miniticker.http;

import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import de.phihag.miniticker.Event;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;

public class SetScoreHandler extends NanoHandler {
	public static class SetScoreRequest {
		public String match_id;
		public String court_id;
		public int[][] network_score;
		public String presses_json;
	}

	private Event ev;

	protected SetScoreHandler(String pathPrefix, Event ev) {
		super(pathPrefix);
		this.ev = ev;
	}

	@Override
	protected Response handle(String relativePath, IHTTPSession session) {
		if (! session.getMethod().equals(Method.POST)) {
			return HTTPUtils.error(501, "only post here");
		}
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(session.getInputStream()));
		SetScoreRequest ssr = (SetScoreRequest) gson.fromJson(reader, SetScoreRequest.class);
		this.ev.setScore(ssr);

		return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "application/json", "{\"status\":\"ok\"}");
	}

}
