package com.shivashish.utils.commonutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	Date currentDate;
	SimpleDateFormat sdfDate;

	public FileUtils() {
		currentDate = new Date();
		sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	public static Boolean deleteFile(String path) {

		File file = new File(path);

		if (file.delete()) {
			logger.info("File deleted successfully");
			return true;
		} else {
			logger.error("Failed to delete the file");
			return false;
		}
	}

	// this function will create a Directory with Given Path iff not Exist
	// @Generic
	public boolean createDirIfNotExist(String Path) {
		File directory = new File(Path);
		boolean success = false;
		if (directory.exists()) {
			logger.debug("Directory already exists ...");
			return true;
		} else {
			logger.info("Directory : {} not exists, creating now", Path);
			success = directory.mkdirs();
			if (success) {
				logger.info("Successfully created new directory : {}", Path);
				return true;
			} else {
				logger.info("Failed to create new directory: {}", Path);
				return false;
			}
		}
	}

	// this function will create a File with Given Path in not Exist
	// @Generic
	boolean createFileIfNotExist(String Path) throws IOException {
		File f = new File(Path);
		boolean success = false;
		if (f.exists()) {
			logger.info("File already exists");
			return true;
		} else {
			logger.info("File : {} not exists, creating now", Path);
			success = f.createNewFile();
			if (success) {
				logger.info("Successfully created new File : {}", Path);
				return true;
			} else {
				logger.info("Failed to create new File: {}", Path);
				return false;
			}
		}

	}


	public boolean dumpResponseInFile(String filename, String output) {
		File file = new File(filename);
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			bw.write(output);
			bw.close();
			fw.close();
		} catch (Exception e) {
			logger.error("Error While Dumping Content in fileName : [{}] , Error : [{}]", filename, e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	// this method will return all the data stored in file
	// @generic
	public String getDataInFile(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		String str = new String(data, "UTF-8");
		return str;
	}
}
