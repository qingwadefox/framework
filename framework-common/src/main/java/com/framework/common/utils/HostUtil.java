package com.framework.common.utils;

import java.net.Socket;

public class HostUtil {
	public static boolean isOpen(String ip, Integer port) {
		try {
			// if (!InetAddress.getByName(ip).isReachable(3000)) {
			// return false;
			// } else {
			Socket s = new Socket(ip, port);
			s.close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		// try {
		// System.out.println(InetAddress.getByName("61.174.40.245").isReachable(3000));
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		System.out.println(isOpen("61.174.40.245", 32947));
	}
}
