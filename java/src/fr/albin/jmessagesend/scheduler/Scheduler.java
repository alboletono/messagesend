package fr.albin.jmessagesend.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.message.sender.MessageSender;
import fr.albin.jmessagesend.message.sender.WindowsMessageSender;


public class Scheduler implements Runnable {

	private Scheduler(int interval) {
		super();
		this.tasks = new ArrayList();
		this.thread = new Thread(this);
		this.finished = false;
		this.messageSender = new WindowsMessageSender();
		LOGGER.debug("Scheduler thread created.");
		this.thread.start();
	}
	
	public static Scheduler getInstance() {
		if (instance == null) {
			instance = new Scheduler(INTERVAL);
		}
		return instance;
	}
	
	public void stop() {
		this.finished = true;
	}
	
	/**
	 * Adds a new task to scedule.
	 * @param task
	 */
	public void addTask(Task task) {
		this.tasks.add(task);
		LOGGER.debug("A task has been added");
	}
	
	/**
	 * Runs the process.
	 */
	public void run() {
		while (!finished) {
			// Browse all the tasks.
			// New list without
			int i = 0;
			while (i < this.tasks.size()) {
				Task task = (Task) this.tasks.get(i);
				LOGGER.debug("Browsing tasks...");
				// Timer has expired.
				if ( (task != null) && (task.getTimer().isReached()) ) {
					LOGGER.debug("Timer has expired, sending message.");
					// Sends the message.
					this.messageSender.send(task.getMessage());
					// remove the task.
					this.tasks.remove(i);
				}
				else {
					LOGGER.debug("Timer has not expired, waiting.");
					task.getTimer().incTime(INTERVAL);
					i++;
				}
			}
			LOGGER.debug("Task browsing finished.");
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List tasks;
	private Thread thread;
	private boolean finished;
	private MessageSender messageSender;
	
	private static Scheduler instance = null;
	private static final int INTERVAL = 1000;
	
	private static final Log LOGGER = LogFactory.getLog(Scheduler.class);
}
