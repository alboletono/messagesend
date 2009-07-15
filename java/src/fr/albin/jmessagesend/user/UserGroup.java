package fr.albin.jmessagesend.user;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * A group of toUsers.
 * @see User.
 * @author avigier
 *
 */
public class UserGroup {

	/**
	 * Creates a user group with the given label.
	 * @param identifier a unique string identifier.
	 * @param label label used to describe the group.
	 */
	public UserGroup(String identifier, String label) {
		this.identifier = identifier;
		this.label = label;
		this.sortedList = new TreeSet();
	}
	
	public void add(User user) {
		this.sortedList.add(user);
	}
	
	public void remove(User user) {
		this.sortedList.remove(user);
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public TreeSet getSortedList() {
		return this.sortedList;
	}
	
	/**
	 * Fills the sorted set from a simple list (i.e. orders the list).
	 * @param list The list to order.
	 */
	public void setList(List list) {
		this.sortedList.clear();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			this.sortedList.add(it.next());
		}
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public int size() {
		return this.sortedList.size();
	}
	
	public String toString() {
		return this.label + " (" + this.identifier + ")";
	}
	
	private String label;
	private String identifier;
	private TreeSet sortedList;
	
}
