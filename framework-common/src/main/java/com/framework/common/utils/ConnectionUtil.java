package com.framework.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ConnectionUtil {

	public static Object httpObjectConnection(String url, Object paramter)
			throws IOException, ClassNotFoundException {
		Object result = null;
		URL curl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) curl
				.openConnection();
		connection.setDoOutput(true);
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setRequestProperty("Content-Type", "application/json");
		OutputStream os = connection.getOutputStream();
		os.write(JSONObject.toJSONString(paramter).getBytes("UTF-8"));
		os.close();
		int response_code = connection.getResponseCode();
		if (response_code == HttpURLConnection.HTTP_OK) {
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = in.read(b)) != -1) {
				baos.write(b, 0, n);
			}

			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			result = ois.readObject();
		}

		return result;

	}

	public static String httpTextConnection(String url,
			Map<String, Object> paramter) throws IOException,
			ClassNotFoundException {
		StringBuffer content = new StringBuffer();

		URL curl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) curl
				.openConnection();
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		int response_code = connection.getResponseCode();
		if (response_code == HttpURLConnection.HTTP_OK) {
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
			return content.toString();
		}

		return content.toString();

	}
}
