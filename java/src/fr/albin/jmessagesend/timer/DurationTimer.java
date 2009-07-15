package fr.albin.jmessagesend.timer;


public class DurationTimer implements Timer {

	public DurationTimer(long finalDuration) {
		this.finalDuration = finalDuration;
		this.currentDuration = 0;
		
	}
	
	public boolean isReached() {
		return this.currentDuration >= this.finalDuration;
	}

	public void incTime(long millis) {
		this.currentDuration += millis;
	}

	private long finalDuration;
	private long currentDuration;
}
