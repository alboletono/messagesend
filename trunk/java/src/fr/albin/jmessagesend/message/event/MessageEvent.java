package fr.albin.jmessagesend.message.event;

import java.util.EventObject;

import fr.albin.jmessagesend.message.generic.Message;

public class MessageEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
	
	private Message message;

}
