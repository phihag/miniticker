package de.phihag.miniticker.sftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import de.phihag.miniticker.EscapeUtils;
import de.phihag.miniticker.Event;
import de.phihag.miniticker.Match;

public class UpdateFromSFTPTask implements SFTPTask {
	private Event ev;

	public UpdateFromSFTPTask(Event ev) {
		this.ev = ev;
	}

	private JsonReader findJSON(ChannelSftp channelSftp, String filename) {
		InputStream is;
		try {
			is = channelSftp.get(filename);
		} catch (SftpException se) {
			return null;
		}
		String html;
		try {
			html = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Pattern pattern = Pattern.compile("<body\\s+data-miniticker-json=\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(html);
		if (! matcher.find()) {
			return null;
		}
		String json = EscapeUtils.unescapeHtml3(matcher.group(1));
		
		return new JsonReader(new StringReader(json));
	}
	
	@Override
	public void run(ChannelSftp channelSftp) throws IOException {
		Gson gson = new Gson();
		JsonReader jr = findJSON(channelSftp, Filenames.OVERVIEW_FILENAME);
		if (jr == null) {
			return;
		}

		Event loaded = (Event) gson.fromJson(jr, Event.class);

		for (int idx = 0;idx < loaded.matches.length;idx++) {
			JsonReader mjr = findJSON(channelSftp, Filenames.matchFilename(idx));
			if (mjr != null) {
				Match m = (Match) gson.fromJson(mjr, Match.class);
				loaded.matches[idx] = m;
			}
		}

		this.ev.load(loaded);
	}

}
