package fr.albin.jmessagesend.utils;

import java.util.List;
import java.util.Vector;

/**
 * An utility class to manipulate list.
 * @author avigier
 *
 */
public class ListUtils {

	/**
	 * Constructs a list from an array.
	 * @param array
	 * @return
	 */
	public static List arrayToList(Object [] array) {
		List vector = new Vector();
		for (int i=0; i<array.length; i++) {
			vector.add(array[i]);
		}
		return vector;
	}
}
