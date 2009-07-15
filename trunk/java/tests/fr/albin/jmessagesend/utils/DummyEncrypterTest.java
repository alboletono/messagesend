package fr.albin.jmessagesend.utils;

import java.util.Arrays;

import fr.albin.jmessagesend.utils.DummyEncrypter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DummyEncrypterTest extends TestCase {

	public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(DummyEncrypterTest.class);
    }

    public void setUp() {
    	// Nothing
    }
    
	public void testEncrypt() {
		DummyEncrypter dummyEncrypter = new DummyEncrypter(key.getBytes(), 20);
		System.out.println("Message   : " + dummyEncrypter.getByteArrayAsString(message.getBytes()));
		byte[] encrypted = dummyEncrypter.encrypt(message.getBytes());
		System.out.println("Encrypted : " + dummyEncrypter.getByteArrayAsString(encrypted));
		assertEquals(encrypted.length, message.length());
		byte[] decrypted = dummyEncrypter.decrypt(encrypted);
		assertEquals(encrypted.length, decrypted.length);
		System.out.println("Decrypted : " + dummyEncrypter.getByteArrayAsString(decrypted));
		assertEquals(Arrays.equals(message.getBytes(), decrypted), true);
	}
	
	private static final String key = "keytest";
	private static final String message = "je pense, à fortiori, que vous êtes une grosse buse ;) ET ça ne me fait pas rire !!! :=)";
}
