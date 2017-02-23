package de.phihag.miniticker;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Miniticker {
	private static final String CONFIG_FN = "config.json";

	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(CONFIG_FN));
		Config config = (Config) gson.fromJson(reader, Config.class);

		ServerConnection sc = new ServerConnection(config);
		WebServer ws = new WebServer(config);
		ws.start();

		sc.run();
	}
}
