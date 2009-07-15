package fr.albin.jmessagesend.message.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.user.User;
import fr.albin.jmessagesend.utils.DummyEncrypter;
import fr.albin.jmessagesend.utils.DummyEncrypterFactory;

public class MessageFactory {

	public MessageFactory() {
		DummyEncrypterFactory dummyEncrypterFactory = new DummyEncrypterFactory();
		dummyEncrypter = 
			dummyEncrypterFactory.getDummyEncrypter(Configuration.getInstance().getSystemPassword());
	}
	
	public Message createIncomingMessage(String bulkText) {
		Message message = new Message();
		message.setType(MessageType.RECEIVED);
		String pattern = Configuration.getInstance().getMessagePattern();

		// System headers not found.
		if (bulkText.indexOf(pattern) != 0) {
			message.setMessage(bulkText);
			LOGGER.debug("No user found for this message.");
		}
		// Fills the information
		else {
			// User identification
			int nextSpaceIndex = bulkText.indexOf(" ", pattern.length() + 1);
			
			String netbiosName = bulkText.substring(pattern.length(), nextSpaceIndex);
			LOGGER.debug("User found for this message : " + netbiosName);
			message.setFromUser(netbiosName);
			
			boolean ifUsers = false;
			// Users 
			int userStart = bulkText.indexOf(USER_START);
			int userEnd = bulkText.indexOf(USER_END);
			List usersList = new ArrayList();
			if ( (userStart != -1) && (userEnd != -1) ) {
				ifUsers = true;
				String users = bulkText.substring(userStart + USER_START.length(), userEnd);
				StringTokenizer tokenizer = new StringTokenizer(users, USER_SEPARATOR);
				while (tokenizer.hasMoreTokens()) {
					usersList.add(new User(tokenizer.nextToken(), NO_NICKNAME));
				}
			}
			message.setUsers(usersList);
				
			String onlyMessage = null;
			
			// There are toUsers.
			if (ifUsers) {
				onlyMessage = bulkText.substring(userEnd + USER_END.length());
			}
			// There are no users, so there is at least the local user.
			else {
				usersList.add(new User(Configuration.getInstance().getUser(), NO_NICKNAME));
				message.setUsers(usersList);
				int firstCarriageReturn = bulkText.indexOf(Configuration.getInstance().getMessageEndPattern());
				if (firstCarriageReturn != -1) {
					onlyMessage = bulkText.substring(firstCarriageReturn);
				}
				else {
					onlyMessage = bulkText;
				}
				LOGGER.debug("Destination toUsers not found");
			}
			
			boolean ifCrypted = bulkText.endsWith(CRYPTED_FLAG);
			
			if (ifCrypted) {
				byte [] decrypted = dummyEncrypter.decrypt(onlyMessage.substring(0, onlyMessage.length() - CRYPTED_FLAG.length()).getBytes());
				message.setMessage(new String(decrypted));
			}
			else {
				message.setMessage(onlyMessage);
			}
			

		}
		return message;
	}
	
	public Message createOutgoingMessage(String text, List users, boolean ifCrypted) {
		Configuration configuration = Configuration.getInstance();
		
		String cryptedMessage = text;
		
		if (ifCrypted) {
			byte[] encrypted = dummyEncrypter.encrypt(text.getBytes());
			cryptedMessage = new String(encrypted);
		}
		
		Message message = new Message();
		message.setType(MessageType.SENT);
		message.setUsers(users);
		message.setMessage(text);
		message.setFromUser(Configuration.getInstance().getUser());
		
		String tempMessage = "";
		
		/* Formatting the message. */
		if (configuration.getMessageHeader() != null) {
			//tempMessage = configuration.getMessageHeader() + message.getMessage();
			tempMessage = configuration.getMessageHeader() + cryptedMessage;
		}
		tempMessage = this.getFormattedUsersList(message.getUsers()) + tempMessage;
		if (configuration.getMessageFooter() != null) {
			tempMessage += configuration.getMessageFooter();
		}
		
		if (ifCrypted) {
			// A crypted message ends with a crypted flag to recognize it.
			tempMessage += CRYPTED_FLAG;
		}
		
		// Message to send (with user list).
		message.setBulkMessage(tempMessage);
		
		return message;
	}
	
	private String getFormattedUsersList(List users) {
		String result = "";
		Iterator iterator = users.iterator();
		while (iterator.hasNext()) {
			User user = (User) iterator.next();
			if (result.equals("")) {
				result = USER_START + user.getNetbiosName();
			}
			else {
				result += USER_SEPARATOR + user.getNetbiosName();
			}
		}
		if (!result.equals("")) {
			result += USER_END;
		}
		return result;
	}
	
	private DummyEncrypter dummyEncrypter;
	
	private static final String USER_SEPARATOR = ";";
	private static final String USER_START = "|#";
	private static final String USER_END = "#|";
	private static final String NO_NICKNAME = "no nickname";
	private static final String CRYPTED_FLAG = "²`";
	
	private static final Log LOGGER = LogFactory.getLog(MessageFactory.class);
}
