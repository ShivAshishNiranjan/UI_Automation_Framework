package com.shivashish.utils.commonutils;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


public class ConfigReader {

	private final static Logger logger = LoggerFactory.getLogger(ConfigReader.class);

	public static Map<String, String> configReader(String filePath, String fileName, String delimiter) throws IOException {
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


	/**
	 * This method is to update the config file according to the parameters provided <br />
	 * It will work on the basis of the section name and without section name as well <br />
	 * For updating with section name , the section name should be passed in the paremeter <br />
	 * For updating without section name , null section should be passed <br />
	 *
	 * @param filePath             the file path of the config file
	 * @param fileName             name of config file
	 * @param sectionName          name of section
	 * @param propertyName         name of property to be modified
	 * @param updatedPropertyValue new value of the property
	 * @throws ConfigurationException
	 * @throws FileNotFoundException
	 */
	public static synchronized void updateValueInConfigFile(String filePath, String fileName, String sectionName, String propertyName, String updatedPropertyValue)
			throws ConfigurationException, FileNotFoundException {
		propertyName = propertyName.trim();
		Configurations configs = new Configurations();

		INIConfiguration config = configs.ini(filePath + "//" + fileName);
		boolean isFileEdited = false;

		if (sectionName != null && config.getProperty(sectionName.toLowerCase() + "." + propertyName.toLowerCase()) != null) {
			//config.getSection(sectionName.toLowerCase()).clearProperty(propertyName.toLowerCase());
			config.getSection(sectionName.toLowerCase()).setProperty(propertyName.toLowerCase(), updatedPropertyValue);
			isFileEdited = true;

		} else if (config.getProperty(propertyName.toLowerCase()) != null) {
			for (Iterator<String> it = config.getKeys(); it.hasNext(); ) {
				String key = it.next();
				if (key.equalsIgnoreCase(propertyName)) {
					config.setProperty(propertyName.toLowerCase(), updatedPropertyValue);
					isFileEdited = true;
				}
			}
		} else {
			logger.warn("Either property name : [ {} ] or section name : [ {} ] doesn't exists, please check again.. ", propertyName, sectionName);
		}

		if (isFileEdited) {
			try {
				config.write(new FileWriter(filePath + "//" + fileName));
			} catch (IOException e) {
				logger.error("Got Exception while updating the config file : [ {} ], Cause : [ {} ], stacktrace : [ {} ]", filePath + "//" + fileName,
						e.getMessage(), e.getStackTrace());
				e.printStackTrace();
			}
		}
	}
	
}
