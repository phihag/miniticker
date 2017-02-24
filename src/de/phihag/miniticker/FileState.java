package de.phihag.miniticker;

public class FileState {
	public final String filename;
	public final String content;

	public FileState(String filename, String content) {
		this.filename = filename;
		this.content = content;
	}
}
