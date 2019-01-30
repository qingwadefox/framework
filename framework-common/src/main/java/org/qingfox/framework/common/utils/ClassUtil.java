package org.qingfox.framework.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    public static List<Class<?>> getClassesByPkg(String pkg) throws UnsupportedEncodingException {
        String pkgPath = pkg.replace(".", File.separator);
        pkgPath = getClassPath() + pkgPath;

        List<Class<?>> classList = new ArrayList<Class<?>>();
        List<String> list = getClasses(pkgPath);
        for (String name : list) {
            try {
                classList.add(Class.forName(pkg + "." + name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }
        }
        return classList;
    }

    public static void main(String[] args) {
        System.out.println((char)65);
        System.out.println(isWrapClass(Long.class));
        System.out.println(isWrapClass(Integer.class));
        System.out.println(isWrapClass(String.class)); 
    }

    /**
     * 获取class类路径
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getClassPath() throws UnsupportedEncodingException {
        return URLDecoder.decode(ClassUtil.class.getResource("").getPath().replace("/", File.separator).replace(ClassUtil.class.getPackage().getName().replace(".", File.separator) + File.separator, ""), "UTF-8");
    }

    /**
     * 获取class类路径
     * 
     * @param path
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getClassPath(String path) {
        return URLDecoder.decode(ClassUtil.class.getResource(path).getPath().replace("/", File.separator));
    }

    /**
     * 
     * @param path
     * @return
     */
    public static List<String> getClasses(String path) {
        File dirFile = new File(path);
        String dirPath = dirFile.getPath();
        List<String> classList = new ArrayList<String>();
        List<File> fileList = FileUtil.getFiles(path);
        for (File file : fileList) {
            // class file
            if (file.getName().endsWith(".class")) {
                String classPath = file.getPath().replace(dirPath + File.separator, "").replace(File.separator, ".");
                classPath = classPath.substring(0, classPath.lastIndexOf(".class"));
                classList.add(classPath);
            }
            // jar file
            else if (file.getName().endsWith(".jar")) {
                try {
                    JarFile jarFile = new JarFile(file);
                    classList.addAll(getClasses(jarFile));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return classList;
    }

    public static List<String> getClasses(JarFile jarFile) {
        List<String> classList = new ArrayList<String>();
        JarEntry entry;
        Enumeration<JarEntry> enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            entry = enumeration.nextElement();
            String classPath = entry.getName();
            if (classPath.endsWith(".class")) {
                classPath = classPath.substring(0, classPath.lastIndexOf(".class")).replace("/", ".");
                classList.add(classPath);
            }
        }
        return classList;
    }

    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isWrapClass(Object object) {
        return isWrapClass(object.getClass());
    }
    
    
}
