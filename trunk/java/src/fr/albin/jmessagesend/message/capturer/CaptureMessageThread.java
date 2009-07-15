package fr.albin.jmessagesend.message.capturer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.message.CaptureMessage;
import fr.albin.jmessagesend.message.event.MessageEvent;
import fr.albin.jmessagesend.message.event.MessageListener;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.message.generic.MessageFactory;

public class CaptureMessageThread implements Runnable {
	
	public CaptureMessageThread(int type) {
		this.finished = false;
		if (type == WINDOWS) {
			this.messageCapturer = new CaptureMessage();
		} else if (type == TCP) {
			this.messageCapturer = new TCPMessageCapturer();
		}
		this.interval = Configuration.getInstance().getIncomingMessageInterval();
		this.thread = new Thread(this);
		this.messageFactory = new MessageFactory();
		this.messageListeners = new ArrayList();
		LOGGER.debug("Capture message thread created.");
		this.thread.start();
	}
	
	public void run() {
		while (!finished) {
			String messageText = this.messageCapturer.getMessageText();
			if (messageText != null) {
				Message message = messageFactory.createIncomingMessage(messageText);
				MessageEvent event = new MessageEvent(this, message);
				this.fireMessageReceivedEvent(event);
				LOGGER.info("WindowsMessage fetched.");
			}
			try {
				Thread.sleep(this.interval);
			} catch (InterruptedException e) {
				LOGGER.error(e);
			}
		}
	}
	
	public void stop() {
		this.finished = true;
	}
	
	public synchronized void addMessageListener(MessageListener messageListener) {
		this.messageListeners.add(messageListener);
	}
	
	public synchronized void removeMessageListener(MessageListener messageListener) {
		this.messageListeners.remove(messageListener);
	}
	
	private synchronized void fireMessageReceivedEvent(MessageEvent event) {
		Iterator iterator = messageListeners.iterator();
		while (iterator.hasNext()) {
			MessageListener listener = (MessageListener) iterator.next();
			listener.messageReceived(event);
		}
	}
	
	private boolean finished;
	private Thread thread;
	private MessageCapturer messageCapturer;
	private long interval;
	private List messageListeners;
	private MessageFactory messageFactory;
	
	public static final int WINDOWS = 0;
	public static final int TCP = 1;
	private static final Log LOGGER = LogFactory.getLog(CaptureMessageThread.class);
}
