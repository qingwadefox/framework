package com.framework.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;

public class ProcessUtil {

	private static final ILogger logger = LoggerFactory.getLogger(ProcessUtil.class);

	public final static int OS_UNKNOW = 0;
	public final static int OS_WINDOWS = 1;
	public final static int OS_LINUX = 2;
	public final static int OS_POSIXUNIX = 3;

	/**
	 * 获取本机系统版本 .
	 * 
	 * @return
	 * @author zhengwei 2015年10月3日 zhengwei
	 */
	public static int getOS() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("linux") != -1 || osName.indexOf("sun os") != -1 || osName.indexOf("sunos") != -1 || osName.indexOf("solaris") != -1 || osName.indexOf("mpe/ix") != -1 || osName.indexOf("freebsd") != -1 || osName.indexOf("irix") != -1 || osName.indexOf("digital unix") != -1 || osName.indexOf("unix") != -1 || osName.indexOf("mac os x") != -1) {
			return OS_LINUX;
		} else if (osName.indexOf("windows") != -1) {
			return OS_WINDOWS;
		} else if (osName.indexOf("hp-ux") != -1 || osName.indexOf("aix") != -1) {
			return OS_POSIXUNIX;
		} else {
			return OS_UNKNOW;
		}
	}

	/**
	 * 获取当前进程PID.
	 * 
	 * @return
	 */
	public static String getPID() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		return name.substring(0, name.indexOf('@'));
	}

	/**
	 * 根据PID查看线程是否存在 .
	 * 
	 * @param pid
	 * @return
	 * @author zhengwei 2015年10月3日 zhengwei
	 */
	public static boolean existProc(String pid) {
		String cmd = null;
		switch (getOS()) {
			case OS_UNKNOW :
				return false;
			case OS_LINUX :
				cmd = "ps -p " + pid;
				break;
			case OS_WINDOWS :
				cmd = String.format("cmd.exe /C wmic process get ProcessId | findstr /C:\"%s \" ", pid);
				break;
			default :
				break;
		}

		logger.debug("执行进程查看指令，" + cmd + "...");
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(isr);
			int lineCount = 0;
			boolean exist = false;
			while (bufferedReader.readLine() != null) {
				lineCount++;
				if (lineCount == 2) {
					exist = true;
					break;
				}
			}
			logger.debug("执行进程查看指令：成功");
			return exist;
		} catch (IOException e) {
			logger.error("执行进程查看指令：失败");
			return false;
		}
	}

	/**
	 * kill进程
	 * 
	 * @param pid
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void killPorcess(String pid) throws IOException, InterruptedException {
		String[] cmds = new String[]{"kill", pid};
		ProcessUtil.exec(cmds);
	}

	/**
	 * kill9进程
	 * 
	 * @param pid
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void kill9Porcess(String pid) throws IOException, InterruptedException {
		String[] cmds = new String[]{"kill", "-9", pid};
		ProcessUtil.exec(cmds);
	}

	/**
	 * 执行java程序
	 * 
	 * @param mainClass
	 * @param libDirs
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Process javaexec(Class<?> mainClass, File... libDirs) throws IOException, InterruptedException {
		List<String> jarList = new ArrayList<String>();
		String classPath = System.getenv("CLASS_PATH");
		if (StringUtils.isNotEmpty(classPath)) {
			jarList.add(classPath);
		}
		for (File libDir : libDirs) {
			List<File> fileList = FileUtil.getFiles(libDir);
			jarList.add(libDir.getPath());
			for (File file : fileList) {
				String path = file.getPath();
				if (path.endsWith(".jar") && jarList.indexOf(path) == -1) {
					jarList.add(file.getPath());
				}
			}
		}
		String cp = null;
		switch (getOS()) {
			case OS_WINDOWS :
				cp = StringUtils.join(jarList, ";");
				break;
			case OS_LINUX :
				cp = StringUtils.join(jarList, ":");
				break;
			default :
				break;
		}
		if (StringUtils.isEmpty(cp)) {
			throw new RuntimeException("无法识别操作系统指令!");
		}
		return exec("java", "-cp", StringUtils.join(jarList, ";"), mainClass.getName());
	}

	public static Process javaexec(Class<?> mainClass, File[] libDirs, String... param) throws IOException, InterruptedException {
		List<String> jarList = new ArrayList<String>();
		String classPath = System.getenv("CLASS_PATH");
		if (StringUtils.isNotEmpty(classPath)) {
			jarList.add(classPath);
		}
		for (File libDir : libDirs) {
			List<File> fileList = FileUtil.getFiles(libDir);
			jarList.add(libDir.getPath());
			for (File file : fileList) {
				String path = file.getPath();
				if (path.endsWith(".jar") && jarList.indexOf(path) == -1) {
					jarList.add(file.getPath());
				}
			}
		}
		String cp = null;
		switch (getOS()) {
			case OS_WINDOWS :
				cp = StringUtils.join(jarList, ";");
				break;
			case OS_LINUX :
				cp = StringUtils.join(jarList, ":");
				break;
			default :
				break;
		}
		if (StringUtils.isEmpty(cp)) {
			throw new RuntimeException("无法识别操作系统指令!");
		}

		String[] cmd = {"java", "-cp", cp, mainClass.getName()};
		cmd = ArrayUtils.addAll(cmd, param);
		return exec(cmd);
	}

	/**
	 * 执行CMD指令
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Process exec(String... cmd) throws IOException, InterruptedException {
		logger.debug("开始执行【", StringUtils.join(cmd, " "), "】");
		Process process = Runtime.getRuntime().exec(cmd);
		class StreamGobbler extends Thread {
			InputStream is;
			String type; // 输出流的类型ERROR或OUTPUT

			StreamGobbler(InputStream is, String type) {
				this.is = is;
				this.type = type;
			}

			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						System.out.println(type + ">" + line);
						System.out.flush();
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
		errorGobbler.start();
		StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
		outGobbler.start();
		return process;
	}

}
