package fr.albin.jmessagesend.conf;

import java.io.File;
import java.io.FileInputStream;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.user.User;
import fr.albin.jmessagesend.user.UserGroup;
import fr.albin.jmessagesend.user.UserGroups;

/**
 * Manages the file used for storing the toUsers.
 * It is a properties file.
 * The key is the netbios name.
 * The value is the nickname plus some groups identifier (separated by commas).
 * To a user, 0 to n groups can be associated.
 * @author avigier
 *
 */
public class UsersProperties {

	public UsersProperties() {
		this.file = new File(DEFAULT_FILE_NAME);
		this.properties = new Properties();
		this.userGroups = new UserGroups();
	}
	
	public void setUserGroups(UserGroups userGroups) {
		this.userGroups = userGroups;
	}
	
	public List getUserList() {
		List vector = new Vector();
		/* Loads the properties file. */
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.file);
			this.properties.load(fis);
			fis.close();
		}
		catch (Exception e) {
			LOGGER.error("Problem with the user properties file : " + e.getMessage());
			return vector;
		}
		Enumeration keys = this.properties.keys();
		User user = null;
		UserGroup userGroup = null;
		String key = null;
		String value = null;
		String name = null;
		String token = null;
		
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			value = this.properties.getProperty(key);
			/* The value can contain groups after the name. */
			StringTokenizer st = new StringTokenizer(value, UsersProperties.GROUP_SEPARATOR);
			/* No items, there is no name. */
			if (st.countTokens() == 0) {
				LOGGER.debug("No user name found, assigning default name.");
				name = UsersProperties.DEFAULT_NAME;
				user = new User(key, name);
			}
			else {
				/* The first value is the name. */
				name = st.nextToken();
				user = new User(key, name);
				while (st.hasMoreTokens()) {
					token = st.nextToken();
					userGroup = this.userGroups.getUserGroupWithIdentifier(token);
					/* Adds the user to the group. */
					if (userGroup != null) {
						userGroup.add(user);
					}
				}
			}
			vector.add(user);
		}
		return vector;
	}
	
	
	
	private Properties properties;
	private File file;
	private UserGroups userGroups;
	private static final String DEFAULT_FILE_NAME = "./conf/users.properties";
	private static final Log LOGGER = LogFactory.getLog(UsersProperties.class);
	private static final String GROUP_SEPARATOR = ",";
	private static final String DEFAULT_NAME = "Sans nom";
}
