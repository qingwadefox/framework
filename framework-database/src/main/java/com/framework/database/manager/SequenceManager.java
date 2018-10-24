package com.framework.database.manager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.framework.common.utils.ClassUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.PropertiesUtil;

public class SequenceManager {

	private final static String DEFAULT_CONFPATH = "conf" + File.separator + "sequence.properties";

	private final static String DEFAULT_SEQNAME = "defalut_seq";

	private String propertiesPath;

	private Map<String, String> sequenceMap;

	public SequenceManager() {
		try {
			initProperties(ClassUtil.getClassPath() + DEFAULT_CONFPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SequenceManager(String path) throws IOException {
		initProperties(path);
	}

	private void initProperties(String path) throws IOException {

		if (FileUtil.notExistsFile(path)) {
			try {
				FileUtil.createTextFile(path, DEFAULT_SEQNAME + "=0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		sequenceMap = PropertiesUtil.loadMap(path);
		propertiesPath = path;

	}

	public Long nextLong() {
		return nextLong(DEFAULT_SEQNAME);
	}

	public Long nextLong(String seqName) {
		if (sequenceMap.get(seqName) == null) {
			sequenceMap.put(seqName, "0");
		}
		Long seq = Long.parseLong(sequenceMap.get(seqName));
		seq += 1;
		PropertiesUtil.setProperty(propertiesPath, seqName, seq + "");
		sequenceMap.put(seqName, seq + "");
		return seq;
	}

	public Integer nextInt() {
		return nextInt(DEFAULT_SEQNAME);
	}

	public Integer nextInt(String seqName) {
		if (sequenceMap.get(seqName) == null) {
			sequenceMap.put(seqName, "0");
		}
		Integer seq = Integer.parseInt(sequenceMap.get(seqName));
		seq += 1;
		PropertiesUtil.setProperty(propertiesPath, seqName, seq + "");
		sequenceMap.put(seqName, seq + "");
		return seq;
	}

	public static void main(String[] args) {
		SequenceManager sequenceManager = new SequenceManager();
		System.out.println(sequenceManager.nextInt());
	}
}
