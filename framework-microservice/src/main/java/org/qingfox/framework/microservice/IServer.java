package org.qingfox.framework.microservice;

import java.io.IOException;

public interface IServer {

  /**
   * 服务启动
   */
  public void start() throws IOException;

  /**
   * 服务停止
   */
  public void stop();

  /**
   * 设置端口
   * 
   * @param port
   */
  public void setPort(Integer port);

  /**
   * 获取端口
   * 
   * @return
   */
  public Integer getPort();

  /**
   * 获取扫描包路径
   * 
   * @return
   */
  public String getScanPackage();

  /**
   * 设置扫描包路径
   * 
   * @param scanPackage
   */
  public void setScanPackage(String scanPackage);
}
