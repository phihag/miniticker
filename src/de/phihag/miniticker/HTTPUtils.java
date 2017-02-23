package de.phihag.miniticker;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

class HTTPUtils {
	public static void sendError(HttpExchange he, int rCode, String description) throws IOException {
		String message = "HTTP error " + rCode + ": " + description;
		byte[] messageBytes = message.getBytes("UTF-8");

		he.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
		he.sendResponseHeaders(rCode, messageBytes.length);
		OutputStream os = he.getResponseBody();
		os.write(messageBytes);
		os.close();
	}
}
