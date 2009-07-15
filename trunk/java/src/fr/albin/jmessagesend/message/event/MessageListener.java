package fr.albin.jmessagesend.message.event;

import java.util.EventListener;


public interface MessageListener extends EventListener {

	void messageReceived(MessageEvent e);
}
