package de.phihag.miniticker.sftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class WriteFile implements SFTPTask {
	public final String filename;
	public final String content;

	public WriteFile(String filename, String content) {
		this.filename = filename;
		this.content = content;
	}

	@Override
	public void run(ChannelSftp channelSftp) throws SftpException, IOException {
		InputStream bais = new ByteArrayInputStream(this.content.getBytes("UTF-8"));
		channelSftp.put(bais, this.filename);
	}
}
