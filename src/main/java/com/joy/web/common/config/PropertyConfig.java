package com.joy.web.common.config;

/**
 * <p>Title:miduo360</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Shanghai Mydream Information Technology Co.,Ltd</p>
 * @author
 * @version 1.0
 * history:
 * Date:          Resp        Comment
 * 2011-01-14     Selle Wu    Create
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyConfig {

	private Properties props;
	private Log log = LogFactory.getLog(PropertyConfig.class);

	public PropertyConfig() {
	}

	private String configFileName;

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public void load() {
		props = new Properties();
		InputStream stream = PropertyConfig.class.getResourceAsStream("/" + configFileName);
		if (stream == null) {
			log.debug(configFileName + " not found");
		} else {
			try {
				props.load(stream);
				log.debug("loaded properties from resource " + configFileName + ": " + props);
			} catch (Exception e) {
				log.error("problem loading properties from " + configFileName);
			} finally {
				try {
					stream.close();
				} catch (IOException ioe) {
					log.error("could not close stream on " + configFileName, ioe);
				}
			}
		}
	}

	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		String s = props.getProperty(key);
		return (s == null) ? defaultValue : s;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {

		String s = props.getProperty(key);

		if (s == null)
			return defaultValue;
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static final String[] TRUE_VALUES = {"true", "1", "y", "yes"};
	public static final String[] FALSE_VALUES = {"false", "0", "n", "no"};

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {

		String s = props.getProperty(key);

		if (s == null)
			return defaultValue;
		for (int i = 0; i < PropertyConfig.TRUE_VALUES.length; i++) {
			if (s.equalsIgnoreCase(PropertyConfig.TRUE_VALUES[i]))
				return true;
		}
		for (int i = 0; i < PropertyConfig.FALSE_VALUES.length; i++) {
			if (s.equalsIgnoreCase(PropertyConfig.FALSE_VALUES[i]))
				return false;
		}
		return defaultValue;
	}

}
