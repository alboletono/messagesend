package fr.albin.jmessagesend.timer;

public interface Timer {

	/** Specifies whether the timer has reached is limit.
	 * @return True if reached. Else otherwise.
	 */
	public boolean isReached();
	
	/**
	 * Increments by millis the milliseconds counter.
	 * Can be usefull or not.
	 * @param millis Number of milliseconds to add.
	 */
	public void incTime(long millis);

}
