package fr.albin.jmessagesend.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class only serves to give XML loading capability like in jdk >= 1.5.
 * Because we are using jdk 1.4.2 this class has been made.
 * @deprecated Only use this class if jvm < 1.5.
 * @author avigier
 */
public class XMLProperties extends Properties {

	public void loadFromXML(InputStream inputStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Element element = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);
			element = document.getDocumentElement();
		}
		catch (Exception e) {
			LOGGER.error("Cannot load xml stream.", e);
		}
		if (element != null) {
			NodeList entries = element.getElementsByTagName(ELEMENT_ENTRY);
			LOGGER.debug("Number of elements fetched : " + entries.getLength());
			for (int i=0; i<entries.getLength(); i++) {
				Element tmp = (Element) entries.item(i);
				String value = null;
				if (tmp.getFirstChild() != null) {
					value = tmp.getFirstChild().getNodeValue();
				}
				String key = tmp.getAttribute(ATTRIBUTE_ENTRY_KEY);
				if ( (key != null) && (value != null) ) {
					this.setProperty(key, value);
				}
			}
		}
	}
	
	public void loadFromXML(File file) throws FileNotFoundException, Exception {
		this.loadFromXML(new FileInputStream(file));
	}
	private static final Log LOGGER = LogFactory.getLog(XMLProperties.class);
	
	private static final String ELEMENT_ENTRY = "entry";
	private static final String ATTRIBUTE_ENTRY_KEY = "key";
}
