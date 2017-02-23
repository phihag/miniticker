package de.phihag.miniticker;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Hold a persistent connection to the (SFTP) server
 */
class ServerConnection {
	private Config config;

	public ServerConnection(Config config) throws Exception {
		this.config = config;
	}
	
	public void run() throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(config.sftp.username, config.sftp.server, config.sftp.port);
		session.setPassword(config.sftp.password);

		Properties props = new java.util.Properties();
		props.put("StrictHostKeyChecking", "no");
		session.setConfig(props);

		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		channelSftp.cd(config.sftp.directory);
	}
}
