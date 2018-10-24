package com.framework.common.utils;

import java.io.File;
import java.io.IOException;

public class LogUtil {
	public static File createLogFile(String filePath) throws IOException {
		File file = new File(filePath);
		FileUtil.createTextFile(file, "");
		return file;
	}

	public static void writeLog(File file, String message) {
		try {
			FileUtil.appendTextFile(file, "/n【INFO】 :  " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeErrorLog(File file, String message) {
		try {
			FileUtil.appendTextFile(file, "/n【ERROR】 :  " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
