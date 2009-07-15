package fr.albin.jmessagesend.message.generic;

public class MessageType {

	private MessageType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public static final MessageType SENT = new MessageType(0);
	public static final MessageType RECEIVED = new MessageType(1);

	private int type;
}
