package de.phihag.miniticker.sftp;

import java.io.IOException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public interface SFTPTask {
	void run(ChannelSftp channelSftp) throws SftpException, IOException;
}
