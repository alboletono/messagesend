package fr.albin.jmessagesend.smiley;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.utils.StringLengthDescComparator;
import fr.albin.jmessagesend.utils.XMLProperties;

public class SmileyParser {

	public SmileyParser() {
		this.properties = new XMLProperties();
		this.load(new File(DEFAULT_FILE));
		
	}
	
	/**
	 * @param smileyFile The file used to map the smileys with the given image.
	 * Warning, the smileys are between double quotes.
	 * E.g. ":)" or ";)".
	 */
	public SmileyParser(File smileyFile) {
		this.properties = new XMLProperties();
		this.load(smileyFile);
	}
	
	private void load(File file) {
		try {
			this.properties.loadFromXML(new FileInputStream(file));
		} catch (Exception e) {
			LOGGER.error("You have to get a valiid smileys conf file in order to work.", e);
		}
	}
	
	/**
	 * @param text Text to parse for adding smileys images.
	 * @return An HTML string containing images <img>
	 */
	public String parse(String text) {
		LOGGER.debug("Parsing smileys...");
		String result = new String(text);
		TreeSet smileys = new TreeSet(new StringLengthDescComparator());
		smileys.addAll(this.properties.keySet());
		Iterator iterator = smileys.iterator();
		// Browses all the smileys.
		while (iterator.hasNext()) {
			// Gets key
			String key = (String) iterator.next();
			// Gets the associated image
			String image = this.properties.getProperty(key);
			// We replace the " characters in the key
			String smileyCode = new String(key);
			String imageDir = Configuration.getInstance().getSmileysPath();
			File imageFile = new File(imageDir, image);
			// Construction HTML code
			String html = "<img src=\"" + "file:///" + imageFile.getAbsolutePath() + "\">";
			result = this.replaceSmilies(result, smileyCode, html);
		}
		LOGGER.debug("Total replacement gives : " + result);
		return result;
	}
	
	private String replaceSmilies(String text, String smileyIn, String smileyOut) {
		StringBuffer result = new StringBuffer(); 

		String regex = "\\Q" + smileyIn + "\\E";
		
		// String to search. 
		Pattern pattern = Pattern.compile(regex);
		// Input string to parse. 
		Matcher matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			String smileyOutEscaped = smileyOut.replace('\\', '/');
			matcher.appendReplacement(result, smileyOutEscaped);
		}
		matcher.appendTail(result);
		
		if (result.length() == 0) {
			return new String(text);
		}
		else {
			return result.toString();
		}
	}
	
	private XMLProperties properties;
	
	private static final String DEFAULT_FILE = "./conf/smileys.properties";
	
	private static final Log LOGGER = LogFactory.getLog(SmileyParser.class);
	
}
