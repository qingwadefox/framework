package org.qingfox.framework.microservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.scanner.PackageScanner;
import org.qingfox.framework.microservice.annotations.Register;

public abstract class MicroserviceServer implements IServer {

    private static final ILogger logger = LoggerFactory.getLogger(MicroserviceServer.class);

    /**
     * 服务端口
     */
    private Integer port;

    /**
     * 扫描包
     */
    private String scanPackage;

    /**
     * 服务类
     */
    private List<Class<?>> serviceList;

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
        scanPackage();
        onStart();
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
        List<String> classList = scanner.scan();
        if (classList == null || classList.isEmpty()) {
            logger.info("classList is empty");
            return;
        }
        logger.info("classList size - ", classList.size());

        serviceList = new ArrayList<>();

        for (String classStr : classList) {
            try {
                Class<?> _class = Class.forName(classStr, false, this.getClass().getClassLoader());
                Register register = _class.getAnnotation(Register.class);
                if (register == null) {
                    continue;
                }
                logger.info("add service class - ", classStr);
                serviceList.add(_class);
            } catch (ExceptionInInitializerError | ClassNotFoundException | UnsatisfiedLinkError | NoClassDefFoundError e) {
                logger.warn(e, "load class error - ", classStr);
                continue;
            }

        }
    }

    protected abstract void onStart();

    protected List<Class<?>> getServiceList() {
        return serviceList;
    }

}
