package de.phihag.miniticker.sftp;

import de.phihag.miniticker.Event;
import de.phihag.miniticker.Match;
import de.phihag.miniticker.Renderer;

public class SFTPChangeListener {
	private Renderer renderer;
	private SFTPUploader uploader;

	public SFTPChangeListener(Renderer renderer, SFTPUploader uploader) {
		this.renderer = renderer;
		this.uploader = uploader;
	}

	public void updateMatch(Event e, Match m) {
		int idx = -1;
		for (int i = 0;i < e.matches.length;i++) {
			if (e.matches[i] == m) {
				idx = i;
				break;
			}
		}
		assert (idx >= 0);
		
		String html = renderer.renderMatch(e, m);
		uploader.schedule(new WriteFile(Filenames.matchFilename(idx), html));
	}

	public void updateIndex(Event e) {
		String html = renderer.renderOverview(e);
		uploader.schedule(new WriteFile(Filenames.OVERVIEW_FILENAME, html));
	}
}
