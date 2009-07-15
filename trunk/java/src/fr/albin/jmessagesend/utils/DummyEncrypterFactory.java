package fr.albin.jmessagesend.utils;

public class DummyEncrypterFactory {

	public DummyEncrypter getDummyEncrypter(String key) {
		return new DummyEncrypter(key.getBytes(), key.length());
	}

}
