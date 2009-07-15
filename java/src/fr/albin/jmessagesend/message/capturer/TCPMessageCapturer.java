package fr.albin.jmessagesend.message.capturer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;

public class TCPMessageCapturer implements MessageCapturer {

	public TCPMessageCapturer() {
		try {
			Configuration configurationProperties = Configuration.getInstance();
			this.serverSocket = new ServerSocket(configurationProperties.getTcpPort());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	public String getMessageText() {
		byte [] bytes = null;  
		
		try {
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			bytes = new byte [2048];
			Arrays.fill(bytes, 0, 2048, (byte) 0);	
			int read = inputStream.read(bytes);
			
			if (read > 0) {
				byte [] newBytes = new byte[read];
				for (int i=0; i<newBytes.length; i++) {
					newBytes[i] = bytes[i];
				}
				bytes = newBytes;
			}

			socket.close();
		} catch (IOException e) {
			LOGGER.error("error", e);
		}
		
		return new String(bytes);
	}
	
	private ServerSocket serverSocket;

	private static final Log LOGGER = LogFactory.getLog(TCPMessageCapturer.class);
}
