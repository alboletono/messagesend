package fr.albin.jmessagesend.message.sender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.message.event.MessageEvent;
import fr.albin.jmessagesend.message.event.MessageListener;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.user.User;

public abstract class MessageSender implements Runnable {
	
	public MessageSender() {
		this.thread = new Thread(this);
		this.uncontactedUsers = new ArrayList();
		this.messageListeners = new ArrayList();
	}
	
	/**
	 * Sends the given message and return the users not contacted.
	 * @param message
	 * @return
	 */
	public synchronized void send(Message message) {
		this.message = message;
		this.uncontactedUsers.clear();
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	/**
	 * Launches in a thread the send to multi users.
	 * The list of users not contacted is stored in list.
	 */
	public void run() {
		// No message to send.
		if ( (message.getMessage() == null) ||
			 ((message.getMessage() != null) && (message.getMessage().length() == 0)) ) {
			LOGGER.info("No message to send.");
		}
		// There is a message to send
		else {
			Iterator it = message.getUsers().iterator();
			while (it.hasNext()) {
				User user = (User)it.next();
				boolean found = this.simpleSend(message, user);
				if (!found) {
					LOGGER.error("Unable to send message for user " + user.getNetbiosName());
					uncontactedUsers.add(user);
				}
			}
			// Messages have been sent
			this.fireMessageSentEvent(new MessageEvent(this, this.message));
		}
		this.thread.stop();
	}
	
	public List getUncontactedUsersList() {
		return this.uncontactedUsers;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public synchronized void addMessageListener(MessageListener messageListener) {
		this.messageListeners.add(messageListener);
	}
	
	public synchronized void removeMessageListener(MessageListener messageListener) {
		this.messageListeners.remove(messageListener);
	}
	
	public abstract boolean simpleSend(Message message, User user);
	
	protected void fireMessageSentEvent(MessageEvent event) {
		Iterator iterator = messageListeners.iterator();
		while (iterator.hasNext()) {
			MessageListener listener = (MessageListener) iterator.next();
			listener.messageReceived(event);
		}
	}

	private Thread thread;
	private List uncontactedUsers;
	private List messageListeners;
	private Message message;
	
	private static final Log LOGGER = LogFactory.getLog(MessageSender.class);

	
}
