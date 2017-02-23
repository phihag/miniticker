package de.phihag.miniticker.http;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

class RedirectHandler extends SimpleHandler {
	private String redirect;
	
	public RedirectHandler(String pathPrefix, String redirect) {
		super(pathPrefix);
		this.redirect = redirect;
	}

	@Override
	protected Response handle(boolean isGET, String relPath,
			IHTTPSession session) {

		if (!relPath.equals("")) {
			return HTTPUtils.error(404, "Not in URL space");
		}

		Response res = NanoHTTPD.newFixedLengthResponse(HTTPUtils.translateCode(302), "text/plain", "");
		res.addHeader("Location", redirect);
		return res;
	}

}
