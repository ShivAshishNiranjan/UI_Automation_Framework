package com.shivashish.utils.commonutils;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by akshay.rohilla on 6/27/2017.
 */
public class ConfigReader {

	private final static Logger logger = LoggerFactory.getLogger(ConfigReader.class);

	public static Map<String, String> ConfigReader(String filePath, String fileName, String delimiter) throws IOException {
		Map<String, String> properties = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath + "//" + fileName));
		String oneLine;

		while ((oneLine = reader.readLine()) != null) {
			oneLine = oneLine.trim();
			//Skip Comment Lines
			if (oneLine.length() > 1 && oneLine.charAt(0) == '/' && oneLine.charAt(1) == '/')
				continue;
			String words[] = oneLine.split(delimiter);
			if (words.length == 1 || words[1].trim().equalsIgnoreCase("") || words[1] == null) {
				properties.put(words[0].trim().toLowerCase(), null);
				continue;
			}
			properties.put(words[0].trim().toLowerCase(), words[1].trim());
		}
		return properties;
	}

	public static String getValueFromConfigFile(String filePath, String fileName, String propertyName) throws ConfigurationException {
		return getValueFromConfigFile(filePath, fileName, null, propertyName);
	}

	public static String getValueFromConfigFile(String filePath, String fileName, String sectionName, String propertyName) throws ConfigurationException {
		String columnName = null;
		propertyName = propertyName.trim();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		if (sectionName != null && config.getProperty(sectionName.toLowerCase() + "." + propertyName.toLowerCase()) != null)
			columnName = config.getProperty(sectionName.toLowerCase() + "." + propertyName.toLowerCase()).toString();

		else if (config.getProperty(propertyName.toLowerCase()) != null)
			columnName = config.getProperty(propertyName.toLowerCase()).toString();

		return columnName;
	}

	public static String getValueFromConfigFileCaseSensitive(String filePath, String fileName, String propertyName) throws ConfigurationException {
		return getValueFromConfigFileCaseSensitive(filePath, fileName, null, propertyName);
	}

	public static String getValueFromConfigFileCaseSensitive(String filePath, String fileName, String sectionName, String propertyName) throws ConfigurationException {
		String columnName = null;
		propertyName = propertyName.trim();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		if (sectionName != null && config.getProperty(sectionName + "." + propertyName) != null)
			columnName = config.getProperty(sectionName + "." + propertyName).toString();

		else if (config.getProperty(propertyName) != null)
			columnName = config.getProperty(propertyName).toString();

		return columnName;
	}

	public static List<String> getAllPropertiesOfSection(String filePath, String fileName, String sectionName) throws ConfigurationException {
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);
		Iterator<String> keys = config.getKeys(sectionName.toLowerCase());
		List<String> allProperties = new ArrayList<String>();

		while (keys.hasNext()) {
			String key[] = keys.next().split("\\.");
			allProperties.add(key[1].toString());
		}

		return allProperties;
	}

	public static boolean containsSection(String filePath, String fileName, String sectionName) throws ConfigurationException {
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		boolean sectionFound = false;
		Iterator<String> sections = config.getSections().iterator();

		while (sections.hasNext()) {
			String next = sections.next();
			if (next != null && next.equalsIgnoreCase(sectionName)) {
				sectionFound = true;
				break;
			}
		}
		return sectionFound;
	}


	public static Map<String, String> getAllConstantProperties(String filePath, String fileName, String sectionName) throws ConfigurationException {
		Map<String, String> allDefaultProperties = new LinkedHashMap<>();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);
		Iterator<String> keys = null;
		if (sectionName == null) {
			keys = config.getKeys();
			while (keys.hasNext()) {
				String nextKey = keys.next();
				// Split keyname because they are section.keyname
				String keyName = nextKey;
				String keyValue = config.getString(nextKey);
				allDefaultProperties.put(keyName.toLowerCase(), keyValue);
			}
		} else {
			keys = config.getKeys(sectionName.toLowerCase());
			while (keys.hasNext()) {
				String nextKey = keys.next();
				// Split keyname because they are section.keyname
				String keyName = nextKey.split("\\.")[1];
				String keyValue = config.getString(nextKey);
				allDefaultProperties.put(keyName.toLowerCase(), keyValue);
			}
		}
		return allDefaultProperties;
	}

	public static Map<String, String> getAllProperties(String filePath, String fileName) throws ConfigurationException {
		return ConfigReader.getAllConstantProperties(filePath, fileName, null);
	}

	public static Map<String, String> getAllDefaultProperties(String filePath, String fileName) throws ConfigurationException {
		return ConfigReader.getAllConstantProperties(filePath, fileName, "default");
	}

	public static List<String> getAllSectionNames(String filePath, String fileName) throws ConfigurationException {
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		List<String> sectionNameList = new ArrayList<String>();
		Iterator<String> sections = config.getSections().iterator();

		while (sections.hasNext()) {
			String sectionName = sections.next();
			if (sectionName != null)
				sectionNameList.add(sectionName.toLowerCase());
		}
		return sectionNameList;
	}

	public static Map<String, String> getAllConstantPropertiesCaseSensitive(String filePath, String fileName, String sectionName) throws ConfigurationException {
		Map<String, String> allDefaultProperties = new HashMap<String, String>();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);
		Iterator<String> keys = config.getKeys();
		if (sectionName == null) {
			keys = config.getKeys();
			while (keys.hasNext()) {
				String nextKey = keys.next();
				// Split keyname because they are section.keyname
				String keyName = nextKey;
				String keyValue = config.getString(nextKey);
				logger.info("Key {} and Value {}", keyName, keyValue);
				allDefaultProperties.put(keyName, keyValue);
			}
		} else {
			keys = config.getKeys(sectionName);
			while (keys.hasNext()) {
				String nextKey = keys.next();
				// Split keyname because they are section.keyname
				String keyName = nextKey.split("\\.")[1];
				String keyValue = config.getString(nextKey);
				allDefaultProperties.put(keyName, keyValue);
			}
		}
		return allDefaultProperties;
	}

	public static boolean hasProperty(String filePath, String fileName, String property) throws ConfigurationException {
		return hasProperty(filePath, fileName, null, property);
	}

	public static boolean hasProperty(String filePath, String fileName, String sectionName, String property) throws ConfigurationException {
		boolean propertyFound = false;
		property = property.trim();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		if (sectionName != null && config.containsKey(sectionName.toLowerCase() + "." + property.toLowerCase()))
			propertyFound = true;
		else if (sectionName == null && config.containsKey(property.toLowerCase()))
			propertyFound = true;

		return propertyFound;
	}

	public static boolean hasPropertyCaseSensitive(String filePath, String fileName, String property) throws ConfigurationException {
		return hasPropertyCaseSensitive(filePath, fileName, null, property);
	}

	public static boolean hasPropertyCaseSensitive(String filePath, String fileName, String sectionName, String property) throws ConfigurationException {
		boolean propertyFound = false;
		property = property.trim();
		Configurations configs = new Configurations();
		INIConfiguration config = configs.ini(filePath + "//" + fileName);

		if (sectionName != null && config.containsKey(sectionName + "." + property))
			propertyFound = true;
		else if (sectionName == null && config.containsKey(property))
			propertyFound = true;

		return propertyFound;
	}
	
}
