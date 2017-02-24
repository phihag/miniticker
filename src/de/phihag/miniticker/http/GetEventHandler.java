package de.phihag.miniticker.http;

import com.google.gson.Gson;

import de.phihag.miniticker.Event;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class GetEventHandler extends SimpleHandler {
	private Event ev;

	protected GetEventHandler(String pathPrefix, Event ev) {
		super(pathPrefix);
		this.ev = ev;
	}

	@Override
	protected Response handle(boolean isGET, String relPath,
			IHTTPSession session) {

		String json = (new Gson()).toJson(this.ev);
		return NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(200), "application/json", json);
	}

}
