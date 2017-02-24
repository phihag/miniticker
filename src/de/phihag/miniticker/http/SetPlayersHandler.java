package de.phihag.miniticker.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.ResponseException;

public class SetPlayersHandler extends NanoHandler {

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
		String ev_json = params.get("json").get(0);
		
	}

}
