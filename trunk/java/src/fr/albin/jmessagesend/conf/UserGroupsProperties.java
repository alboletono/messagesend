package fr.albin.jmessagesend.conf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.user.UserGroup;
import fr.albin.jmessagesend.user.UserGroups;

/**
 * Allows to create a list of UserGroup from a properties file.
 * The key is unique string identifier identifying a group.
 * The value is the label of the group.
 * Warning : The created user groups won't be filled with toUsers.
 * They will be empty, just containing an empty list of toUsers.
 * @author avigier
 *
 */
public class UserGroupsProperties {

	public UserGroupsProperties() {
		this.file = new File(DEFAULT_FILE_NAME);
		this.properties = new Properties();
	}
	
	public UserGroups getUserGroupsList() {
		UserGroups userGroups = new UserGroups();
		/* Loads the properties file. */
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.file);
			this.properties.load(fis);
			fis.close();
		}
		catch (Exception e) {
			LOGGER.error("Problem with the group properties file : " + e.getMessage());
			return userGroups;
		}
		TreeMap treeMap = new TreeMap(this.properties);
		Set keys = treeMap.keySet();
		UserGroup userGroup = null;
		String key = null;
		String value = null;
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			key = (String)it.next();
			value = this.properties.getProperty(key);
			userGroup = new UserGroup(key, value);
			userGroups.add(userGroup);
			LOGGER.debug("User group " + userGroup.toString() + " has been added.");
		}
		return userGroups;
	}
	
	private Properties properties;
	private File file;
	private static final String DEFAULT_FILE_NAME = "./conf/groups.properties";
	private static final Log LOGGER = LogFactory.getLog(UserGroupsProperties.class);
}
