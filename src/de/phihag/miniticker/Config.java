package de.phihag.miniticker;

public class Config {
	public static class SFTPConfig {
		public String server;
		public int port;
		public String directory;
		public String username;
		public String password;
	}

	public SFTPConfig sftp;
	public int webPort;
	public String bupLocation;
	public String bupIndex; 
	public String dataLocation;
}
