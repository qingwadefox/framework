package org.qingfox.framework.microservice;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.scanner.PackageScanner;
import org.qingfox.framework.microservice.annotations.Register;

public abstract class MicroserviceServer implements IServer {

  private static final ILogger logger = LoggerFactory.getLogger(MicroserviceServer.class);

  private Integer port;

  private String scanPackage;

  private List<String> classList;

  @Override
  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public Integer getPort() {
    return this.port;
  }

  @Override
  public String getScanPackage() {
    return scanPackage;
  }

  @Override
  public void setScanPackage(String scanPackage) {
    this.scanPackage = scanPackage;
  }

  @Override
  public void start() throws IOException {
    Validate.notNull(port, "port is null");
    logger.info("port - ", port);
    beforeStart();
    scanPackage();
    Validate.notEmpty(classList, "classList is empty");
    registerServices(classList);
  }

  @Override
  public void stop() {
  }

  /**
   * 开始扫描包
   * 
   * @throws IOException
   */
  private void scanPackage() throws IOException {
    Validate.notEmpty(scanPackage, "scanPackage is null");
    logger.info("scanPackage - ", scanPackage);
    PackageScanner scanner = new PackageScanner(scanPackage);
    classList = scanner.scan();
    logger.info("classList size - ", classList.size());
  }

  /**
   * 注册服务
   * 
   * @param classList
   */
  private void registerServices(List<String> classList) {
    for (String classStr : classList) {
      logger.debug("class - ", classStr);
      try {
        Class<?> _class = Class.forName(classStr, false, this.getClass().getClassLoader());
        Register register = _class.getAnnotation(Register.class);
        if (register != null) {
          logger.debug(classStr, " is not service class");
        }
        logger.debug("start register class - ", classStr);
        registerService(_class);
        logger.debug("register class - ", classStr, " success");
      } catch (ExceptionInInitializerError | ClassNotFoundException | UnsatisfiedLinkError | NoClassDefFoundError e) {
        logger.warn(e, "load or register class error - ", classStr);
        continue;
      }
    }
  }

  protected abstract void registerService(Class<?> _class);

  protected abstract void beforeStart();

}
