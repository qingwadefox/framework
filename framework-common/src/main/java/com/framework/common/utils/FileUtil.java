package com.framework.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;

public class FileUtil {

  public final static String UTF8 = "UTF-8";
  public final static String GBK = "GBK";
  public final static String UTF16 = "UTF-16";
  public final static String UNICODE = "Unicode";

  public static String pathSeparator(String path) {
    if (path.endsWith("/") || path.endsWith(File.separator)) {
      return path;
    } else {
      return path + File.separator;
    }
  }

  public static void clearText(File file) throws IOException {
    FileUtils.write(file, "");
  }

  public static void appendLine(File file, String line) throws IOException {
    FileUtils.write(file, line + "\n", true);
  }

  public static <T> void readLine(File file, ReadLine<T> readLine) throws Exception {
    BufferedReader reader = null;
    String temp = null;
    int line = 0;
    try {
      reader = new BufferedReader(new FileReader(file));
      while ((temp = reader.readLine()) != null) {
        readLine.nextLine(temp, line);
        line++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 获取文件列表(包括文件夹)
   * 
   * @param filePath
   * @return
   */
  public static List<File> getFileAndDirs(String filePath) {
    List<File> fileList = new ArrayList<File>();
    File file = new File(filePath);
    if (file.isDirectory()) {
      fileList.add(file);
      for (File _file : file.listFiles()) {
        fileList.addAll(getFileAndDirs(_file.getPath()));
      }

    } else {
      fileList.add(file);
    }
    return fileList;
  }

  /**
   * 获取文件列表(包括文件夹)
   * 
   * @param file
   * @return
   */
  public static List<File> getFileAndDirs(File file) {
    return getFileAndDirs(file.getPath());
  }

  /**
   * 获取文件列表(不包括文件夹)
   * 
   * @param filePath
   * @return
   */
  public static List<File> getFiles(String filePath) {
    List<File> fileList = new ArrayList<File>();
    File file = new File(filePath);
    if (file.isDirectory()) {
      for (File _file : file.listFiles()) {
        fileList.addAll(getFiles(_file.getPath()));
      }

    } else {
      fileList.add(file);
    }
    return fileList;
  }

  /**
   * 获取文件
   * 
   * @param filePath
   * @return
   */
  public static File getFile(String filePath) {
    return getFiles(filePath).get(0);
  }

  /**
   * 是否存在文件
   * 
   * @param filePath
   */
  public static boolean existsFile(String filePath) {
    return getFiles(filePath).get(0).exists();
  }

  /**
   * 是否不存在文件
   * 
   * @param filePath
   * @return
   */
  public static boolean notExistsFile(String filePath) {
    return !existsFile(filePath);
  }

  /**
   * 获取文件列表(不包括文件夹)
   * 
   * @param file
   * @return
   */
  public static List<File> getFiles(File file) {
    return getFiles(file.getPath());
  }

  /**
   * 设置文件列表(不包括文件夹)
   * 
   * @param filePath
   * @param fileList
   */
  public static void setFiles(String filePath, List<File> fileList) {
    File file = new File(filePath);
    if (file.isDirectory()) {
      for (File _file : file.listFiles()) {
        try {
          setFiles(_file, fileList);
        } catch (Exception e) {
          continue;
          // e.printStackTrace();
        }
      }

    } else {
      fileList.add(file);
    }
  }

  /**
   * 设置文件列表(不包括文件夹)
   * 
   * @param file
   * @param fileList
   */
  public static void setFiles(File file, List<File> fileList) {
    setFiles(file.getPath(), fileList);
  }

  /**
   * 获取文件MD5值
   * 
   * @param file
   * @return
   */
  public static String getMD5(File file) {
    if (!file.isFile()) {
      return null;
    }
    MessageDigest digest = null;
    FileInputStream in = null;
    byte buffer[] = new byte[1024];
    int len;
    try {
      digest = MessageDigest.getInstance("MD5");
      in = new FileInputStream(file);
      while ((len = in.read(buffer, 0, 1024)) != -1) {
        digest.update(buffer, 0, len);
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    BigInteger bigInt = new BigInteger(1, digest.digest());
    return bigInt.toString(16);
  }

  /**
   * 文件拷贝
   * 
   * @param srcFile
   * @param objFile
   * @throws IOException
   */
  public static void copyFile(File srcFile, File objFile) throws IOException {
    if (srcFile.exists()) {
      if (objFile.getParentFile().exists() == false) {
        objFile.getParentFile().mkdirs();
      }
      FileInputStream fi = null;
      FileOutputStream fo = null;
      try {
        fi = new FileInputStream(srcFile);
        fo = new FileOutputStream(objFile);
        byte[] buffer = new byte[1024];
        int byteread = 0;

        while ((byteread = fi.read(buffer)) != -1) {
          fo.write(buffer, 0, byteread);
        }
      } catch (IOException e) {
        throw new IOException();
      } finally {
        try {
          fi.close();
          fo.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 文件拷贝
   * 
   * @param srcFile
   * @param objFile
   * @throws IOException
   */
  public static void copyFile(File srcFile, File objFile, JProgressBar progressBar) throws IOException {

    if (srcFile.exists()) {
      long fileSize = srcFile.length();
      progressBar.setMaximum(99);
      if (objFile.getParentFile().exists() == false) {
        objFile.getParentFile().mkdirs();
      }
      FileInputStream fi = null;
      FileOutputStream fo = null;
      try {
        fi = new FileInputStream(srcFile);
        fo = new FileOutputStream(objFile);
        byte[] buffer = new byte[1024];
        long bytesum = 0;
        int byteread = 0;

        while ((byteread = fi.read(buffer)) != -1) {

          int value = (int) (((float) bytesum / fileSize) * 100);
          progressBar.setValue(value);
          bytesum += byteread;
          fo.write(buffer, 0, byteread);
        }
      } catch (IOException e) {
        throw new IOException();
      } finally {
        try {
          fi.close();
          fo.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 创建文本文件
   * 
   * @param file
   * @param content
   * @throws IOException
   */
  public static void createTextFile(File file, String content) throws IOException {
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    if (!file.exists()) {
      file.createNewFile();
    }

    FileWriter writer = new FileWriter(file);
    writer.write(content);
    writer.flush();
    writer.close();
  }

  /**
   * 创建文本文件
   * 
   * @param filePath
   * @param content
   * @throws IOException
   */
  public static void createTextFile(String filePath, String content) throws IOException {
    File file = new File(filePath);
    createTextFile(file, content);
  }

  /**
   * 创建对象文件
   * 
   * @param file
   * @param object
   * @throws IOException
   */
  public static void createObjectFile(File file, Object object) throws IOException {
    ByteArrayOutputStream baos = null;
    FileOutputStream fos = null;
    BufferedOutputStream stream = null;
    ObjectOutputStream oos = null;
    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      fos = new FileOutputStream(file);
      stream = new BufferedOutputStream(fos);
      stream.write(baos.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        stream.close();
        fos.close();
        oos.close();
        baos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static Object getObject(File file) throws IOException, ClassNotFoundException, FileNotFoundException {
    FileInputStream fis = null;
    ByteArrayOutputStream baos = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    Object object = null;
    try {
      fis = new FileInputStream(file);
      baos = new ByteArrayOutputStream(1000);
      byte[] b = new byte[1000];
      int n;
      while ((n = fis.read(b)) != -1) {
        baos.write(b, 0, n);
      }

      bais = new ByteArrayInputStream(baos.toByteArray());
      ois = new ObjectInputStream(bais);
      object = ois.readObject();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        ois.close();
        bais.close();
        baos.close();
        fis.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return object;

  }

  /**
   * 添加文本
   * 
   * @param file
   * @param content
   * @throws IOException
   */
  public static void appendTextFile(File file, String content) throws IOException {
    FileWriter writer = new FileWriter(file, true);
    writer.write(content);
    writer.flush();
    writer.close();
  }

  /**
   * 获取文件编码
   * 
   * @param file
   * @return
   */
  public static String getCode(File file) {
    InputStream inputStream = null;
    String code = "";
    try {
      inputStream = new FileInputStream(file);
      byte[] head = new byte[3];
      inputStream.read(head);
      code = GBK;
      if (head[0] == -1 && head[1] == -2)
        code = UTF16;
      if (head[0] == -2 && head[1] == -1)
        code = UNICODE;
      if (head[0] == -17 && head[1] == -69 && head[2] == -65)
        code = UTF8;

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return code;
  }

  public static void changeCode(File file, String code) {
    File objFile = null;
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      StringBuffer content = new StringBuffer();
      String str = "";
      while ((str = in.readLine()) != null) {
        content.append(str);
        content.append("\n");
      }
      in.close();
      file.delete();

      objFile = new File(file.getPath() + ".bak");

      Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(objFile), code));
      out.write(content.toString());
      out.close();

      objFile.renameTo(file);

    } catch (Exception e) {
      if (objFile != null && objFile.exists()) {
        objFile.delete();
      }
      e.printStackTrace();
    }
  }

  public static InputStream getInputStream(String filePath) {
    try {
      return new BufferedInputStream(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  // PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static String getDatePath(int datetype) {
    Calendar calendar = Calendar.getInstance();
    switch (datetype) {
    case DateUtil.YEAR:
      return DateUtil.format(calendar, File.separator + "yyyy" + File.separator);
    case DateUtil.MONTH:
      return DateUtil.format(calendar, File.separator + "yyyy" + File.separator + "MM" + File.separator);
    case DateUtil.DAY:
      return DateUtil.format(calendar, File.separator + "yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator);
    default:
      return "";

    }

  }

  public static void downloadFile(String urlStr, File file) throws IOException {
    DataInputStream dataInputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      URL url = new URL(urlStr);
      dataInputStream = new DataInputStream(url.openStream());
      fileOutputStream = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int length;

      while ((length = dataInputStream.read(buffer)) > 0) {
        fileOutputStream.write(buffer, 0, length);
      }
      dataInputStream.close();
      fileOutputStream.close();
    } catch (IOException e) {
      throw e;
    } finally {
      if (dataInputStream != null) {
        dataInputStream.close();
      }
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }

  }

  public static void objectToFile(Object obj, File file) throws IOException {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      fos = new FileOutputStream(file);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(obj);
    } catch (FileNotFoundException e) {
      throw e;
    } finally {
      if (oos != null) {
        oos.close();
      }

      if (fos != null) {
        fos.close();
      }
    }

  }

  public static Object fileToObject(File file) throws Exception {
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream(file));
      return in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw e;
    } finally {
      if (in != null) {
        in.close();
      }
    }

  }

  public static void main(String[] args) {
    System.out.println(FileUtil.getDatePath(DateUtil.DAY));
  }

}
