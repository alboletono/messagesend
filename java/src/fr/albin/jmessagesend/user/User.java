package fr.albin.jmessagesend.user;


/**
 * Describes a user.
 * A user can receive message.
 * It consists of a netbios name (the identifier on the network) and a nickname
 * (an identifier for the final user).
 * @author avigier
 *
 */
public class User implements Comparable {

	/**
	 * Creates a new user.
	 * @param netbiosName
	 * @param nickname
	 */
	public User(String netbiosName, String nickname) {
		this.netbiosName = netbiosName;
		this.nickname = nickname;		
	}

	public String getNetbiosName() {
		return netbiosName;
	}
	public void setNetbiosName(String netbiosName) {
		this.netbiosName = netbiosName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String toString() {
		return this.nickname + " (" + this.netbiosName + ")";
	}
	
	public int compareTo(Object o) {
		if (o instanceof User) {
			User user = (User) o;
			return (this.nickname.compareTo(user.getNickname()));
		}
		else {
			return 0;
		}
	}

	private String netbiosName;
	private String nickname;
	
}
