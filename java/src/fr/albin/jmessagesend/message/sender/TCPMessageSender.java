package fr.albin.jmessagesend.message.sender;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.message.generic.MessageOrigin;
import fr.albin.jmessagesend.user.User;

public class TCPMessageSender extends MessageSender {

	public synchronized boolean simpleSend(Message message, User user) {
		
		boolean result = false;
		
		// The message is sent via TCP
		message.setOrigin(MessageOrigin.TCP);
		
		Configuration configuration = Configuration.getInstance();
		
		try {
			Socket socket = new Socket(user.getNetbiosName(), configuration.getTcpPort());
			
			// The message format is dependent of the Netbios format for
			// compatibility purpose.
			String pattern = configuration.getMessagePattern();
			
			// Preparing the string to send
			String string = pattern + " " + configuration.getUser() + " à " + user.getNetbiosName() + "\n";
			string += message.getBulkMessage();
			
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(string.getBytes());
			
			socket.close();
			
			result = true;
		}
		catch (Exception e) {
			LOGGER.error("Error", e);
		}
		return result;
	}
	
	private static final Log LOGGER = LogFactory.getLog(TCPMessageSender.class);
}
