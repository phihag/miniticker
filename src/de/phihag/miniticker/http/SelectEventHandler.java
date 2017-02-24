package de.phihag.miniticker.http;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import de.phihag.miniticker.Event;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.ResponseException;

public class SelectEventHandler extends AbstractHandler {
	private Event ev;

	public SelectEventHandler(String pathPrefix, Event ev) {
		super(pathPrefix);
		this.ev = ev;
	}

	@Override
	protected Response handle(String relativePath, IHTTPSession session) {
		if (! session.getMethod().equals(Method.POST)) {
			return HTTPUtils.error(501, "only post here");
		}

		try {
			session.parseBody(new HashMap<String,String>());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ResponseException e) {
			throw new RuntimeException(e);
		}

		Map<String, List<String>> params = session.getParameters();
		String ev_json = params.get("event_json").get(0);

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(ev_json));
		Event e = (Event) gson.fromJson(reader, Event.class);
		this.ev.select(e);
		
		return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "application/json", "{\"status\":\"ok\"}");
	}

}
