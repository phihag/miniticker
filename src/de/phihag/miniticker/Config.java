package de.phihag.miniticker;

public class Config {
	public static class SFTPConfig {
		String server;
		int port;
		String directory;
		String username;
		String password;
	}

	public SFTPConfig sftp;
	public int webPort;
	public String bupLocation;
	public String bupIndex; 
	public String dataLocation;
}
