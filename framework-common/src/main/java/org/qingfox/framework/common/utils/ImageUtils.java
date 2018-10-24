/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package org.qingfox.framework.common.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月13日 @功能说明：
 * 
 */

@SuppressWarnings("restriction")
public class ImageUtils {

  /**
   * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
   * 
   * @param imageFile
   * @return
   * @throws IOException
   */
  public static String getImageBase64(File imageFile) throws IOException {

    InputStream in = null;
    try {
      in = new FileInputStream(imageFile);
      byte[] data = new byte[in.available()];
      in.read(data);
      in.close();
      BASE64Encoder encoder = new BASE64Encoder();
      return encoder.encode(data);
    } catch (IOException e) {
      throw e;
    } finally {
      if (in != null) {
        in.close();
      }
    }

  }

  public static void setBase64Image(String imgStr, String imgFilePath) throws IOException {// 对字节数组字符串进行Base64解码并生成图片
    BASE64Decoder decoder = new BASE64Decoder();
    // Base64解码
    byte[] bytes = decoder.decodeBuffer(imgStr);
    for (int i = 0; i < bytes.length; ++i) {
      if (bytes[i] < 0) {// 调整异常数据
        bytes[i] += 256;
      }
    }
    // 生成jpeg图片
    OutputStream out = new FileOutputStream(imgFilePath);
    out.write(bytes);
    out.flush();
    out.close();
  }

  /**
   * 从URL获取图片
   * 
   * @param url
   * @return
   * @throws MalformedURLException
   * @throws IOException
   */
  public static BufferedImage getURLImage(String url) throws MalformedURLException, IOException {
    return ImageIO.read(new URL(url));
  }

  /**
   * 设置图片长宽
   * 
   * @param bufferImage
   * @param cutWidth
   * @param cutHeight
   * @return
   */
  public static BufferedImage changeImageSize(BufferedImage bufferImage, int cutWidth, int cutHeight) {
    BufferedImage newImage = new BufferedImage(cutWidth, cutHeight, bufferImage.getType());
    Graphics g = newImage.getGraphics();
    g.drawImage(bufferImage, 0, 0, cutWidth, cutHeight, null);
    g.dispose();
    return newImage;
  }

  /**
   * 获取base64编码
   * 
   * @param bufferImage
   * @param type
   * @return
   * @throws IOException
   */
  public static String getBufferImageBase64(BufferedImage bufferImage, String type) throws IOException {
    ByteArrayOutputStream baos = null;
    try {
      byte[] data = null;
      baos = new ByteArrayOutputStream();
      ImageIO.write(bufferImage, type, baos);
      data = baos.toByteArray();

      BASE64Encoder encoder = new BASE64Encoder();
      return encoder.encode(data);
    } catch (IOException e) {
      throw e;
    } finally {
      if (baos != null) {
        baos.close();
      }
    }
  }

  /**
   * 获取网络图片长度 .
   * 
   * @param url
   * @return
   * @throws MalformedURLException
   * @throws IOException
   * @author Administrator 2017年12月25日 Administrator
   */
  public int getUrlImageLen(String url) throws MalformedURLException, IOException {
    return new URL(url).openConnection().getContentLength();
  }

  public static File commpressPicForScale(File imageFile, long desFileSize, double accuracy) throws IOException {
    if (!imageFile.exists()) {
      return null;
    }
    File destFile = new File(imageFile.getParentFile().getPath() + File.separator + "compression_" + imageFile.getName());
    Thumbnails.of(imageFile.getPath()).scale(1f).toFile(destFile.getPath());
    commpressPicCycle(destFile.getPath(), desFileSize, accuracy);
    return destFile;
  }

  private static void commpressPicCycle(String desPath, long desFileSize, double accuracy) throws IOException {
    File srcFileJPG = new File(desPath);
    long srcFileSizeJPG = srcFileJPG.length();
    // 2、判断大小，如果小于500kb，不压缩；如果大于等于500kb，压缩
    if (srcFileSizeJPG <= desFileSize) {
      return;
    }
    // 计算宽高
    BufferedImage bim = ImageIO.read(srcFileJPG);
    int srcWdith = bim.getWidth();
    int srcHeigth = bim.getHeight();
    int desWidth = new BigDecimal(srcWdith).multiply(new BigDecimal(accuracy)).intValue();
    int desHeight = new BigDecimal(srcHeigth).multiply(new BigDecimal(accuracy)).intValue();

    Thumbnails.of(desPath).size(desWidth, desHeight).outputQuality(accuracy).toFile(desPath);
    commpressPicCycle(desPath, desFileSize, accuracy);
  }

  public static void download(String urlStr, File file) throws IOException {
    URL url = new URL(urlStr);
    DataInputStream dis = null;
    FileOutputStream fos = null;
    try {
      dis = new DataInputStream(url.openStream());
      fos = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int length;

      while ((length = dis.read(buffer)) > 0) {
        fos.write(buffer, 0, length);
      }

    } catch (IOException e) {
      throw e;
    } finally {
      if (dis != null) {
        dis.close();
      }
      if (fos != null) {
        fos.close();
      }
    }
  }
}
