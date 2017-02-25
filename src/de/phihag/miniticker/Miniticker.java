package de.phihag.miniticker;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import de.phihag.miniticker.http.WebServer;
import de.phihag.miniticker.sftp.SFTPChangeListener;
import de.phihag.miniticker.sftp.SFTPUploader;
import de.phihag.miniticker.sftp.UpdateFromSFTPTask;

public class Miniticker {
	private static final String CONFIG_FN = "config.json";

	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(CONFIG_FN));
		Config config = (Config) gson.fromJson(reader, Config.class);

		SFTPUploader uploader = new SFTPUploader(config.sftp);
		Renderer r = new Renderer();
		SFTPChangeListener cl = new SFTPChangeListener(r, uploader);
		Event ev = new Event();
		ev.setChangeListener(cl);
		WebServer ws = new WebServer(config, ev, r);
		ws.start();

		UpdateFromSFTPTask ut = new UpdateFromSFTPTask(ev);
		uploader.schedule(ut);
		uploader.run();
	}
}
