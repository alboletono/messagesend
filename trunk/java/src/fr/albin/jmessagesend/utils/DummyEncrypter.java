package fr.albin.jmessagesend.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple very simple encrypter.
 * It is a combination of a XOR plus a shift to the right.
 * @author avigier
 */
public class DummyEncrypter {

	public DummyEncrypter(byte[] key, long shift) {
		this.key = key;
		this.shift = shift;
	}
	
	/**
	 * @param bytes to encrypt.
	 * @return An encrypted array of bytes.
	 */
	public byte[] encrypt(byte[] bytes) {
		LOGGER.debug("Array to encrypt : " + getByteArrayAsString(bytes));
		byte [] encrypted = shift(bytes, true);
		LOGGER.debug("Encrypted array : " + getByteArrayAsString(encrypted));
		return encrypted;
	}
	
	public byte[] decrypt(byte[] bytes) {
		LOGGER.debug("Array to decrypt : " + getByteArrayAsString(bytes));
		byte [] decrypted = shift(bytes, false);
		LOGGER.debug("Decrypted array : " + getByteArrayAsString(decrypted));
		return decrypted;
	}
	
	private byte[] shift(byte[] bytes, boolean ifShiftLeft) {
		byte[] shifted = new byte[bytes.length];
		long shiftValue = shift;
		if (!ifShiftLeft) {
			shiftValue = - shiftValue;
		}
		for (int i=0; i<shifted.length; i++) {
			long oldValue = bytes[i];
			long newValue = oldValue;
			
			if (isCharInRange(newValue)) {
				
				if (oldValue + shiftValue > END_ASCII_VALUE) {
					newValue = oldValue + shiftValue - END_ASCII_VALUE + START_ASCII_VALUE;
				}
				else if (oldValue + shiftValue < START_ASCII_VALUE) {
					newValue = END_ASCII_VALUE + (shiftValue + (oldValue - START_ASCII_VALUE) );
				}
				else {
					newValue = oldValue + shiftValue;
				}
			}
			
			shifted[i] = (byte) newValue;
		}
		return shifted;
	}
	

	public String getByteArrayAsString(byte [] bytes) {
		String string = "";
		for (int i=0; i<bytes.length; i++) {
			if (i!=0) {
				string += "\t";
			}
			string += bytes[i];
		}
		return string;
	}
	
	private boolean isCharInRange(long ch) {
		
		return ch >= START_ASCII_VALUE && ch <= END_ASCII_VALUE;
	}
	
	private byte[] key;
	
	private long shift;
	
	private static final long START_ASCII_VALUE = 33; // First is exclamation mark.
	private static final long END_ASCII_VALUE = 126; // Last is tilde.
		
	private static final Log LOGGER = LogFactory.getLog(DummyEncrypter.class);

}
