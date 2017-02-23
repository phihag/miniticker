package de.phihag.miniticker;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

class WebServer {
	private HttpServer hs;

	public WebServer(Config config) throws IOException {
		hs = HttpServer.create();
		hs.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(100));
		StaticFileHandler.create(hs, "/bup/", config.bupLocation, config.bupIndex);
		RedirectHandler.create(hs, "/", "/bup/#mt");
		hs.bind(new InetSocketAddress(config.webPort), 0);
	}

	public void start() {
		hs.start();
	}

}
