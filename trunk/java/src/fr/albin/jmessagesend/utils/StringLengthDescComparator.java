package fr.albin.jmessagesend.utils;

import java.util.Comparator;

public class StringLengthDescComparator implements Comparator {

	public int compare(Object object1, Object object2) {
		
		String string1 = (String) object1;
		String string2 = (String) object2;
		if (string1.equals(string2)) {
			return 0;
		}
		if (string2.length() > string1.length()) {
			return 1;
		}else {
			return -1;
		}
	}
}
