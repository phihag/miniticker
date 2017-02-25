package de.phihag.miniticker.sftp;

public class Filenames {
	final public static String OVERVIEW_FILENAME = "Gesamtstand.html";
	public static final String LIVESCORE_FILENAME = "Livescore.html";
	
	public static String matchFilename(int idx) {
		 return "Spiel_" + (idx + 1) + ".html";
	}

}
