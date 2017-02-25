package de.phihag.miniticker.sftp;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import de.phihag.miniticker.Config.SFTPConfig;

public class SFTPUploader implements Runnable {

	private SFTPConfig config;
	private BlockingQueue<SFTPTask> q;
	private SFTPTask cur;
	
	public SFTPUploader(SFTPConfig config) {
		this.config = config;
		this.q = new LinkedBlockingQueue<SFTPTask>();
	}
	
	public void run() {
		
		for (;;) {
			try {
				loop();
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SftpException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
		
	public void loop() throws JSchException, IOException, SftpException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(config.username, config.server, config.port);
		session.setPassword(config.password);
	
		Properties props = new java.util.Properties();
		props.put("StrictHostKeyChecking", "no");
		session.setConfig(props);
	
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		channelSftp.cd(config.directory);
		
		while (true) {
			if (cur == null) {
				try {
					cur = q.take();
				} catch (InterruptedException e) {
					continue;
				}
				assert (cur != null);
			}

			cur.run(channelSftp);
			
			cur = null;
		}
	}

	/**
	 * Requests code to run on the SFTP thread.
	 * This function can be called from any thread.
	 * @param st The task to do
	 */
	public void schedule(SFTPTask st) {
		q.offer(st);
	}
}
