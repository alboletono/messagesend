package fr.albin.jmessagesend.message.generic;

public class MessageOrigin {
	
	private MessageOrigin(int type) {
		this.origin = type;
	}
	
	public int getOrigin() {
		return this.origin;
	}
	
	public static final MessageOrigin WINDOWS = new MessageOrigin(0);
	public static final MessageOrigin TCP = new MessageOrigin(1);

	private int origin;
}
