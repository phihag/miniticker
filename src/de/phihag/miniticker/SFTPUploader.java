package de.phihag.miniticker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	private BlockingQueue<FileState> q;
	private FileState cur;
	
	public SFTPUploader(SFTPConfig config) {
		this.config = config;
		this.q = new LinkedBlockingQueue<FileState>();
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

			InputStream bais= new ByteArrayInputStream(cur.content.getBytes("UTF-8"));
			channelSftp.put(bais, cur.filename);
			
			cur = null;
		}
	}

	/**
	 * Requests an update of the specified file. Will run in the background.
	 * This function can be called from any thread.
	 * @param fs The file and contents to write
	 */
	public void set(FileState fs) {
		q.offer(fs);
	}
}
