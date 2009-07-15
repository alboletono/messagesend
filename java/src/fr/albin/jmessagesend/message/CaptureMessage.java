package fr.albin.jmessagesend.message;

import fr.albin.jmessagesend.message.capturer.MessageCapturer;

public class CaptureMessage implements MessageCapturer {

	public native String getMessageText();
	
	static {
		System.loadLibrary("capture_message");
	}
}
