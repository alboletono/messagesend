package fr.albin.jmessagesend.utils;

import fr.albin.jmessagesend.conf.Configuration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConfigurationTest extends TestCase {

	public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ConfigurationTest.class);
    }

    public void setUp() {
    	// Nothing
    }
    
    public void testGetApplicationVersion() {
    	assertNotNull(Configuration.getInstance().getApplicationVersion());
    }
}
