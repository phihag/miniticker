package de.phihag.miniticker;

public class ChangeListener {
	private Renderer renderer;
	private SFTPUploader su;

	public ChangeListener(Renderer renderer, SFTPUploader su) {
		this.renderer = renderer;
		this.su = su;
	}

	public void updateMatch(Event e, Match m) {
		int idx = -1;
		for (int i = 0;i < e.matches.length;i++) {
			if (e.matches[i] == m) {
				idx = i + 1;
				break;
			}
		}
		assert (idx >= 1);
		
		String html = renderer.renderMatch(e, m);
		su.set(new FileState("Spiel_" + idx + ".html", html));
	}

	public void updateIndex(Event e) {
		String html = renderer.renderOverview(e);
		su.set(new FileState("Gesamtstand.html", html));
	}
}
