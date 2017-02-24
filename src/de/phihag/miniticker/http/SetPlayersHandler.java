package de.phihag.miniticker.http;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import de.phihag.miniticker.Event;
import de.phihag.miniticker.Player;
import de.phihag.miniticker.Team;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.ResponseException;

public class SetPlayersHandler extends NanoHandler {
	private Event ev;

	protected SetPlayersHandler(String pathPrefix, Event ev) {
		super(pathPrefix);
		this.ev = ev;
	}

	public static class SetPlayersRequest {
		public Map<String,Team[]> teams_by_match;
		public Player[][] backup_players;
		public Player[][] present_players;
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
		String req_json = params.get("json").get(0);
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(req_json));
		SetPlayersRequest spr = (SetPlayersRequest) gson.fromJson(reader, SetPlayersRequest.class);
		this.ev.setPlayers(spr);

		return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "application/json", "{\"status\":\"ok\"}");
	}

}
