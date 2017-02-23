package de.phihag.miniticker.http;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

class HTTPUtils {
	public static Status translateCode(int code) {
		switch (code) {
		case 200:
			return Status.OK;
		case 302:
			return Status.REDIRECT;
		case 400:
			return Status.BAD_REQUEST;
		case 404:
			return Status.NOT_FOUND;
		case 501:
			return Status.NOT_IMPLEMENTED;
		}
		
		throw new RuntimeException("Unsupported HTTP code " + code);
	}
	
	public static Response error(int code, String description) {
		String message = "HTTP error " + code + ": " + description;
		byte[] messageBytes;
		try {
			messageBytes = message.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(messageBytes);

		return NanoHTTPD.newFixedLengthResponse(translateCode(code), "text/plain; charset=utf-8", bais, messageBytes.length);
	}
}
