package de.phihag.miniticker;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

class WebServer {
	private HttpServer hs;

	public WebServer(Config config) throws IOException {
		hs = HttpServer.create();
		StaticFileHandler.create(hs, "/bup/", config.bupLocation, "bup.html");
		hs.bind(new InetSocketAddress(config.webPort), 0);
	}

	public void start() {
		hs.start();
	}

}
