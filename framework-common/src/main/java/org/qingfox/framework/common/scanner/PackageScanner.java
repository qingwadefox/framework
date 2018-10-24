package org.qingfox.framework.common.scanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by whf on 8/28/15.
 */
public class PackageScanner {
	/**
	 * 包名
	 */
	private String pkgName;

	/**
	 * 包对应的路径名
	 */
	private String pkgPath;

	/**
	 * 注解的class对象
	 */
	private Class<Annotation> anClazz;

	private ClassLoader cl;

	public PackageScanner(String pkgName) {
		this.pkgName = pkgName;
		this.pkgPath = PathUtils.packageToPath(pkgName);

		cl = Thread.currentThread().getContextClassLoader();
	}

	public PackageScanner(String pkgName, ClassLoader classLoader) {
		this.pkgName = pkgName;
		this.pkgPath = PathUtils.packageToPath(pkgName);

		cl = classLoader;
	}

	public PackageScanner(String pkgName, Class<Annotation> anClazz) {
		this(pkgName);

		this.anClazz = anClazz;
	}

	/**
	 * 执行扫描操作.
	 *
	 * @return
	 * @throws IOException
	 */
	public List<String> scan() throws IOException {
		List<String> list = loadResource();
		if (null != this.anClazz) {
			list = filterComponents(list);
		}

		return list;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
		this.pkgPath = PathUtils.packageToPath(pkgName);
	}

	public void setAnnocation(Class<Annotation> an) {
		this.anClazz = an;
	}

	private List<String> loadResource() throws IOException {
		List<String> list = new ArrayList<String>();

		Enumeration<URL> urls = cl.getResources(pkgPath);
		while (urls.hasMoreElements()) {
			URL u = urls.nextElement();

			ResourceType type = determineType(u);
//5240 4700 1735 8411
			switch (type) {
				case JAR :
					String path = PathUtils.distillPathFromJarURL(u.getPath());
					list.addAll(scanJar(path));
					break;
				case FILE :
					list.addAll(scanFile(u.getPath(), pkgName));
					break;
				default :
					break;
			}
		}

		return list;
	}

	/**
	 * 根据URL判断是JAR包还是文件目录
	 * 
	 * @param url
	 * @return
	 */
	private ResourceType determineType(URL url) {
		if (url.getProtocol().equals(ResourceType.FILE.getTypeString())) {
			return ResourceType.FILE;
		}

		if (url.getProtocol().equals(ResourceType.JAR.getTypeString())) {
			return ResourceType.JAR;
		}

		throw new IllegalArgumentException("不支持该类型:" + url.getProtocol());
	}

	/**
	 * 扫描JAR文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private List<String> scanJar(String path) throws IOException {
		@SuppressWarnings("resource")
		JarFile jar = new JarFile(path);

		List<String> classNameList = new ArrayList<>(20);

		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();

			if ((name.startsWith(pkgPath)) && (name.endsWith(ResourceType.CLASS_FILE.getTypeString()))) {
				name = PathUtils.trimSuffix(name);
				name = PathUtils.pathToPackage(name);

				classNameList.add(name);
			}
		}

		return classNameList;
	}

	/**
	 * 扫描文件目录下的类
	 * 
	 * @param path
	 * @return
	 */
	private List<String> scanFile(String path, String basePkg) {
		File f = new File(path);

		List<String> classNameList = new ArrayList<>(10);

		// 得到目录下所有文件(目录)
		File[] files = f.listFiles();
		if (null != files) {
			int LEN = files.length;

			for (int ix = 0; ix < LEN; ++ix) {
				File file = files[ix];

				// 判断是否还是一个目录
				if (file.isDirectory()) {
					// 递归遍历目录
					List<String> list = scanFile(file.getAbsolutePath(), PathUtils.concat(basePkg, ".", file.getName()));
					classNameList.addAll(list);

				} else if (file.getName().endsWith(ResourceType.CLASS_FILE.getTypeString())) {
					// 如果是以.class结尾
					String className = PathUtils.trimSuffix(file.getName());
					// 如果类名中有"$"不计算在内
					if (-1 != className.lastIndexOf("$")) {
						continue;
					}

					// 命中
					String result = PathUtils.concat(basePkg, ".", className);
					classNameList.add(result);
				}
			}
		}

		return classNameList;
	}

	/**
	 * 过虑掉没有指定注解的类
	 * 
	 * @param classList
	 * @return
	 */
	private List<String> filterComponents(List<String> classList) {
		List<String> newList = new ArrayList<>(20);

		for (String name : classList) {
			try {
				Class<?> clazz = Class.forName(name);
				Annotation an = clazz.getAnnotation(this.anClazz);
				if (null != an) {
					newList.add(name);
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return newList;
	}
}
