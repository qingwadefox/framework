package org.qingfox.framework.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties loadProperties(String filePath) throws IOException {
		Properties p = new Properties();
		try {
			p.load(FileUtil.getInputStream(filePath));
			return p;
		} catch (IOException e) {
			throw e;
		}

	}

	public static Properties loadProperties(File file) throws IOException {
		return loadProperties(file.getPath());
	}

	public static Map<String, String> loadMap(String filePath) throws IOException {
		Map<String, String> result = new HashMap<String, String>();
		Properties p = loadProperties(filePath);
		Iterator<Entry<Object, Object>> it = p.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			result.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return result;
	}

	public static Map<String, String> loadMap(File file) throws IOException {
		return loadMap(file.getPath());
	}

	public static void saveProperty(File file, Properties p) {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			p.store(fos, "update by " + PropertiesUtil.class.toString() + ".saveProperty");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveProperty(String filePath, Map<String, String> map) {
		File file = new File(filePath);
		saveProperty(file, map);
	}

	public static void saveProperty(File file, Map<String, String> map) {
		InputStream fis = null;
		OutputStream fos = null;

		try {
			Properties p = new Properties();
			fis = new FileInputStream(file);
			p.load(fis);
			fos = new FileOutputStream(file);

			Iterator<Entry<String, String>> it = map.entrySet().iterator();

			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				p.setProperty(entry.getKey(), entry.getValue());
			}
			p.store(fos, "update by " + PropertiesUtil.class.toString() + ".setProperty");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void setProperty(String filePath, String key, String value) {
		File file = new File(filePath);
		setProperty(file, key, value);
	}

	public static void setProperty(File file, String key, String value) {
		InputStream fis = null;
		OutputStream fos = null;

		try {
			Properties p = new Properties();
			fis = new FileInputStream(file);
			p.load(fis);
			fos = new FileOutputStream(file);
			p.setProperty(key, value);
			p.store(fos, "update by " + PropertiesUtil.class.toString() + ".setProperty");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
