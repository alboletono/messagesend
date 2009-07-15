package fr.albin.jmessagesend.message.sender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.message.generic.MessageOrigin;
import fr.albin.jmessagesend.user.User;

/**
 * Allows to send a message via NetBios protocol.
 * It uses the dos command "net send" or "smbclient -L" for unix like system.
 * @author avigier
 */
public class WindowsMessageSender extends MessageSender {
	
	public synchronized boolean simpleSend(Message message, User user) {
		
		boolean result = false;
		
		// The message is sent via Netbios (Windows)
		message.setOrigin(MessageOrigin.WINDOWS);
		
		try {
			Configuration configuration = Configuration.getInstance();
			Runtime rt = Runtime.getRuntime();
			
			String testArgs[] = {configuration.getCommand(), message.getBulkMessage(), user.getNetbiosName(), message.getFromUser()};
			Process p = rt.exec(testArgs) ;
			p.waitFor();
			p.getErrorStream();
			result = p.exitValue() == 0;
		}
		catch (Exception e) {
			LOGGER.error("Error", e);
		}
		return result;
	}

	private static final Log LOGGER = LogFactory.getLog(WindowsMessageSender.class);
}
