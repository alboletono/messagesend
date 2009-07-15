package fr.albin.jmessagesend.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to fetch the different parameters set in the configuration file.
 * This class is a singleton.
 * @author avigier
 *
 */
public class Configuration {

	private Configuration() {
		this.file = new File(DEFAULT_FILE_NAME);
		this.properties = new Properties();
		/* We now loading all the data in the properties. */
		this.load();
	}
	
	private void load() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.file);
			this.properties.load(fis);
		} catch (FileNotFoundException e) {
			LOGGER.error("Configuration file not found.");
		} catch (IOException e) {
			LOGGER.error("Cannot access configuration file.");
		}
		try {
			fis.close();
		} catch (IOException e) {
			LOGGER.error("Cannot close the configuration file stream.");
		}
	}
	
	/**
	 * Allows to get the singleton class.
	 * @return
	 */
	public static Configuration getInstance() {
		if (configurationProperties == null) {
			configurationProperties = new Configuration();
		}
		return configurationProperties;
	}
	
	public String getMessageHeader() {
		this.load();
		return this.properties.getProperty(MESSAGE_HEADER_KEY);
	}
	
	public String getMessageFooter() {
		this.load();
		return this.properties.getProperty(MESSAGE_FOOTER_KEY);
	}
	
	public String getDllPath() {
		this.load();
		return this.properties.getProperty(SYSTEM_DLL_PATH);
	}
	
	public String getUser() {
		this.load();
		return this.properties.getProperty(SYSTEM_USER);
	}
	
	public String getCommand() {
		this.load();
		return this.properties.getProperty(SYSTEM_CMD);
	}
	
	public long getIncomingMessageInterval() {
		this.load();
		return this.getLongValue(SYSTEM_INCOMING_MESSAGE_INTERVAL, DEFAULT_INCOMING_MESSAGE_INTERVAL);
	}
	

	public String getMessagePattern() {
		this.load();
		return this.properties.getProperty(MESSAGE_PATTERN);
	}
	
	public String getMessageEndPattern() {
		this.load();
		return this.properties.getProperty(MESSAGE_END_PATTERN);
	}
	
	public String getSystemBrowser() {
		this.load();
		return this.properties.getProperty(SYSTEM_BROWSER);
	}
	
	public String getDatePattern() {
		this.load();
		return this.properties.getProperty(SYSTEM_DATE_PATTERN);
	}
	
	public String getAnchorableProtocols() {
		this.load();
		return this.properties.getProperty(SYSTEM_ANCHORABLE_PROTOCOLS);
	}
	
	/**
	 * 
	 * @return True if the current config allows to use the tray icon feature.
	 * False otherwise.
	 */
	public boolean isUsingTrayIconFeature() {
		this.load();
		return this.properties.getProperty(SYSTEM_TRAY_ICON).compareTo(Boolean.toString(true)) == 0;
	}
	
	public String getSmileysPath() {
		return this.properties.getProperty(SMILEYS_PATH);
	}
	
	public String getSystemPassword() {
		return this.properties.getProperty(SYSTEM_PASSWORD, DEFAULT_SYTEM_PASSWORD);
	}
	
	public int getTcpPort() {
		return (int) this.getLongValue(TCP_PORT, DEFAULT_TCP_PORT);
	}
	
	public String getApplicationVersion() {
		return this.properties.getProperty(APPLICATION_VERSION);
	}
	
	private long getLongValue(String key, long defaultValue) {
		String property = this.properties.getProperty(key);
		long value = 0;
		try {
			value = Long.parseLong(property);
		}
		catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}
	
	private File file;
	private Properties properties;
	
	private static Configuration configurationProperties = null;;
	
	private static final String DEFAULT_FILE_NAME = "conf/config.properties";
	private static final Log LOGGER = LogFactory.getLog(Configuration.class);
	
	private static final String MESSAGE_FOOTER_KEY = "message.footer";
	private static final String MESSAGE_HEADER_KEY = "message.header";
	private static final String MESSAGE_PATTERN = "message.pattern";
	private static final String MESSAGE_END_PATTERN = "message.endPattern";
	
	private static final String SYSTEM_TRAY_ICON = "system.trayIcon";
	private static final String SYSTEM_DLL_PATH = "system.dllPath";
	private static final String SYSTEM_USER = "system.user";
	private static final String SYSTEM_CMD = "system.cmd";
	private static final String SYSTEM_INCOMING_MESSAGE_INTERVAL = "system.incomingMessageInterval";
	private static final String SYSTEM_BROWSER = "system.browser";
	private static final String SYSTEM_DATE_PATTERN = "system.datePattern";
	private static final String SYSTEM_ANCHORABLE_PROTOCOLS = "system.anchorableProtocols";
	private static final String SYSTEM_PASSWORD = "system.password";
	
	private static final String APPLICATION_VERSION = "application.version";
	
	private static final String TCP_PORT = "tcp.port";
	
	private static final String SMILEYS_PATH = "smileys.path";
	
	private static final long DEFAULT_TCP_PORT = 8189;
	private static final long DEFAULT_INCOMING_MESSAGE_INTERVAL = 250;
	private static final String DEFAULT_SYTEM_PASSWORD = "interfacesi";
}
