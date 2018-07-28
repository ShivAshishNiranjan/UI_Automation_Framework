package utils.commonUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shivashish on 28/6/17.
 */
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

	// this will dump the response of get rating API to given file which can be verified later
	// @generic
	public void dumpResponseInFile(String filename, String output) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			bw.write(output);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
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

	// this function will save the content of Message in Given file Location
	// MailBox Automation Specific
	boolean saveMessageContentInFile(File file, Message message) throws IOException, MessagingException {
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("DATE: " + message.getSentDate().toString() + "\n");
			bw.write("<div>");
			bw.write("FROM: " + message.getFrom()[0].toString() + "\n");
			bw.write("<div>");
			bw.write("SUBJECT: " + message.getSubject().toString() + "\n");
			bw.write("<div>");
			bw.write("TO: " + message.getAllRecipients()[0].toString() + "\n");
			bw.write("<div>");
			bw.write("<div>");
			if (message.isMimeType("multipart/*")) {
				MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
				String result = getMessageContentFromMimeMultipart(mimeMultipart);
				bw.write(result);
			}
//			 else if (message.isMimeType("text/plain")) {
//				bw.write(message.getContent().toString());
//			}
			else {
				bw.write(message.getContent().toString());
			}
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IO exception happened");
			return false;
		}
	}

	// function that will return text from Mime Multipart type Message
	// MailBox Automation Specific
	private String getMessageContentFromMimeMultipart(
			MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + "\n" + html;
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getMessageContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	public Boolean writeResponseIntoFile(HttpResponse response, String outputFile) {
		HttpEntity entity = response.getEntity();
		Boolean status = true;
		try {
			if (entity != null) {
				BufferedInputStream bis = new BufferedInputStream(entity.getContent());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
				int inByte;
				while ((inByte = bis.read()) != -1)
					bos.write(inByte);
				bis.close();
				bos.close();
			}
		} catch (Exception e) {
			status = false;
			logger.error("Exception occurred while dumping response into file {} {}\n", outputFile, e.getMessage());
		}
		return status;
	}

	public Boolean createNewFolder(String outputPath, String folderName) {
		Boolean status = true;
		try {
			File theDir = new File(outputPath + "/" + folderName);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				logger.debug("creating directory: ", theDir.getName());
				theDir.mkdir();
			}
		} catch (Exception e) {
			status = false;
			logger.error("Exception occurred while creating new folder. {}", e.getMessage());
		}
		return status;
	}

	public static Boolean uploadFileOnSFTPServer(String host,Integer port,String username,String password,String targetDir,File fileToUpload) {
		Boolean flag = false;
		//String type = "sftp";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		logger.info("preparing the host information for sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			logger.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			logger.info("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(targetDir);
			//File f = new File(fileName);
			channelSftp.put(new FileInputStream(fileToUpload), fileToUpload.getName());
			flag = true;
			logger.info("File transferred successfully to host.");
		} catch (Exception ex) {
			logger.error("Exception occurred while transfering the file to sftp server. error : {}",ex.getMessage());
		} finally {

			channelSftp.exit();
			logger.info("sftp Channel exited.");
			channel.disconnect();
			logger.info("Channel disconnected.");
			session.disconnect();
			logger.info("Host Session disconnected.");
		}
		return flag;
	}

	/*function to get local copy of file hosted on sftp server*/
	public static Boolean getFileFromSFTPServer(String host,Integer port,String username,String password,String remoteFile,String localFile) {
		Boolean flag = false;
		//String type = "sftp";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		logger.info("preparing the host information for sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			logger.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			logger.info("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			//channelSftp.cd(targetDir);

			channelSftp.get(remoteFile, localFile);
			flag = true;
			logger.info("File saved successfully from {} to {}",remoteFile,localFile);
		} catch (Exception ex) {
			logger.error("Exception occurred while getting the file from sftp server. error : {}",ex.getMessage());
		} finally {

			channelSftp.exit();
			logger.info("sftp Channel exited.");
			channel.disconnect();
			logger.info("Channel disconnected.");
			session.disconnect();
			logger.info("Host Session disconnected.");
		}
		return flag;
	}

	public Map<String, String> ReadKeyValueFromFile(String filepath, String delimtier, String section) {

		String str = null;
		Map<String, String> formdata = new HashMap<String, String>();
		//Map<String,Map<String,String>> formDatawithUser = new HashMap<String,Map<String,String>>();
		try {
			File file = new File(filepath);

			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;
			int count = 0;
			line = br.readLine();
			line.trim();
			do {
				if (line.equalsIgnoreCase("#" + section)) {
					count = count + 1;
					line = br.readLine();
				}
				if (count == 1) {

					String[] columns = line.split(delimtier, 2);
					String key = columns[0].trim();
					String val = columns[1].trim();
					formdata.put(key, val);
				}

			} while ((line = br.readLine()) != null && count <= 1);

		} catch (Exception e) {
			logger.error("Exception occurred while reading from file", e.getMessage());
		}
		return formdata;
	}

	public static String getFileNameWithoutExtension(String fileName) {
		String fileNameWithoutExtension = null;

		try {
			if (fileName.contains(".") && fileName.lastIndexOf(".") != 0)
				fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
		} catch (Exception e) {
			logger.error("Exception while getting Extension of File {}. {}", fileName, e.getStackTrace());
		}
		return fileNameWithoutExtension;
	}

	public static String getFileExtension(String fileName) {
		String fileExtension = null;

		try {
			if (fileName.contains(".") && fileName.lastIndexOf(".") != 0)
				fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (Exception e) {
			logger.error("Exception while getting Extension of File {}. {}", fileName, e.getStackTrace());
		}
		return fileExtension;
	}
}
