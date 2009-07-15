package fr.albin.jmessagesend.ihm;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NumberSpinner extends JSpinner{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NumberSpinner() {
		new SpinnerNumberModel(INITIAL_VALUE, MIN_VALUE, MAX_VALUE, STEP_VALUE);
	}
	
	/**
	 * @return The int value. If no value, return always the MIN_VALUE 0.
	 */
	public int getIntValue() {
		Integer integer = (Integer) this.getValue();
		// There is a value.
		if (integer != null) {
			return integer.intValue();
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Specifies if the time spinner has got a value or not.
	 * @return True if there is a value, false otherwise.
	 */
	public boolean hasValue() {
		return this.getValue() != null;
	}
	
	private static final int INITIAL_VALUE = 0;
	private static final int MIN_VALUE = 0;
	private static final int MAX_VALUE = 0;
	private static final int STEP_VALUE = 0;
}
