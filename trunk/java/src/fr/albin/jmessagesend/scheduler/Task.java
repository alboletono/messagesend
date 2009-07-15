package fr.albin.jmessagesend.scheduler;

import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.timer.Timer;

public class Task {

	public Task(Message message, Timer timer) {
		this.message = message;
		this.timer = timer;
	}

	/**
	 * @return Returns the message.
	 */
	public Message getMessage() {
		return message;
	}
	/**
	 * @return Returns the timer.
	 */
	public Timer getTimer() {
		return timer;
	}
	
	private Message message;
	private Timer timer;
}
