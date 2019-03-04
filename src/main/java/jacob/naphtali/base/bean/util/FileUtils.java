package jacob.naphtali.base.bean.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	/**
	 * 匹配"\"或"/"
	 */
	public static final String PATH_REG = "[\\\\/]";
	public static final String DEFAULT_CHARSET = "utf8";

	public static String getExtName(String fullName) {
		if (StringUtils.isEmpty(fullName)) {
			return null;
		}
		String[] strs = fullName.split("\\.");
		if (strs.length > 0) {
			return strs[strs.length - 1];
		}
		return null;
	}

	/**
	 * 传入原文件名（带扩展名），返回文件名为系统时间到毫秒_(0~9999)随机数 例：20170410204237412_8753.doc
	 * 
	 * @author ChangJian
	 * @date 2017年4月10日
	 * @param oldName
	 * @return
	 */
	public static String getNewFileName(String oldName) {
		String newName = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date())
				+ "_"
				+ (int) (Math.random() * 10000)
				+ oldName.substring(oldName.lastIndexOf("."));
		return newName;
	}

	public static String getNameByPath(String path) {
		if (null == path) {
			return null;
		}
		String[] libs = path.split(PATH_REG);
		return libs[libs.length - 1];
	}

	public static void makeDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			makeDir(file.getParentFile());
			file.mkdir();
		}
	}
	
	public static void makeDir(String filePath) {
		makeDir(new File(filePath));
	}

	public static boolean newFile(String content, String path) {
		if (StringUtils.isEmpty(content)) {
			return false;
		}
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		String fileName = path.split("[\\/]")[path.split("[\\/]").length - 1];
		File file = new File(path.substring(0,
				path.length() - fileName.length()));
		if (!file.exists()) {
			makeDir(file);
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			fileWriter.write(content);
		} catch (IOException e) {
			try {
				fileWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			e.printStackTrace();
			return false;
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getString(File file) {
		if (null == file) {
			return null;
		}
		if (file.isFile()) {
			return getString(file, DEFAULT_CHARSET);
		}
		return null;
	}
	
	public static String getString(File file, String charsetName) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, charsetName);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + charsetName);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getString(InputStream inputStream) {
		return getString(inputStream, DEFAULT_CHARSET);
	}
	
	public static String getString(InputStream inputStream, String charsetName) {
		byte[] filecontent;
		try {
			filecontent = new byte[inputStream.available()];
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			inputStream.read(filecontent);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String str;
		try {
			str = new String(filecontent, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}

	public static String getString(String fileName) {
		File file = new File(fileName);
		return getString(file);
	}

	/**
	 * 复制单个文件
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath
	 */
	public static void copySingleFile(File file, String newPath, String newName) {
		if (null == file) {
			return;
		}
		if (!file.exists()) {
			return;
		}
		File newDir = new File(newPath);
		String newFilePath;
		if (StringUtils.isEmpty(newName)) {
			newFilePath = newDir.getAbsolutePath() + "/" + file.getName();
		} else {
			newFilePath = newDir.getAbsolutePath() + "/" + newName;
		}
		InputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} // 读入原文件
		OutputStream fs;
		try {
			fs = new FileOutputStream(newFilePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		byte[] buffer = new byte[1444];
		try {
			int byteread;
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			fs.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 复制单个文件
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath
	 */
	public static void copySingleFile(File file, String newPath) {
		copySingleFile(file, newPath, null);
	}
	
	/**
	 * 复制单个文件
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath
	 */
	public static void copySingleFile(String filePath, String newPath) {
		File file = new File(filePath);
		copySingleFile(file, newPath, null);
	}

	/**
	 * 复制单个文件
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath
	 */
	public static void copySingleFile(String filePath, String newPath, String newName) {
		File file = new File(filePath);
		copySingleFile(file, newPath, newName);
	}
	
	/**
	 * 复制文件/文件夹
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param oldFile
	 * @param newPath 必须是目录
	 */
	public static void copyToDirectory(File oldFile, String newPath) {
		//如果文件不存在，程序结束
		if (!oldFile.exists()) {
			return;
		}
		makeDir(newPath);
		//如果是单个文件，复返单个文件，程序结束
		if (oldFile.isFile()) {
			copySingleFile(oldFile, newPath);
			return;
		}
		File[] files = oldFile.listFiles();
		for (File file : files) {
			copyToDirectory(file, newPath + File.separator + oldFile.getName());
		}
	}
	
	/**
	 * 复制文件/文件夹
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param oldPath
	 * @param newPath 必须是目录
	 */
	public static void copyToDirectory(String oldPath, String newPath) {
		copyToDirectory(new File(oldPath), newPath);
	}

	/**
	 * 删除文件/目录
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 */
	public static void delete(File file) {
		if (null == file) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
		File[] files = file.listFiles();
		for (File subFile : files) {
			delete(subFile);
		}
		file.delete();
	}
	
	/**
	 * 删除文件/目录
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 */	
	public static void delete(String filePath) {
		delete(new File(filePath));
	}
	
	/**
	 * 移动文件/目录
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath 必须是目录
	 */
	public static void move(File file, String newPath) {
		copyToDirectory(file, newPath);
		delete(file);
	}
	
	/**
	 * 移动文件/目录
	 * @author ChangJian
	 * @data 2018年6月26日
	 * @param file
	 * @param newPath 必须是目录
	 */
	public static void move(String filePath, String newPath) {
		move(new File(filePath), newPath);
	}
}
