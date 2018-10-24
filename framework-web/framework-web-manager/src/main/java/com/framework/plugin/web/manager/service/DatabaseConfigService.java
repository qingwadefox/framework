package com.framework.plugin.web.system.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.framework.common.utils.ClassUtil;
import com.framework.common.utils.PropertiesUtil;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.common.web.spring.ConfigPath;

@Service
@Appcode(code = "DatabaseConfigService")
public class DatabaseConfigService {

    @Appfunction(code = "doEdit", paramNames = { "type", "ip", "port", "name",
            "username", "password" }) 
    public void doEdit(String type, String ip, String port, String name,
            String username, String password) throws IOException {
        File file = new File(ClassUtil.getClassPath()).getParentFile();
        file = new File(file.getPath() + File.separator
                + ConfigPath.DATABASECONFIG_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs(); 
            file.createNewFile();
        }
        Properties p = PropertiesUtil.loadProperties(file);
        p.setProperty("type", type);
        p.setProperty("ip", ip);
        p.setProperty("port", port);
        p.setProperty("name", name);
        p.setProperty("username", username);
        p.setProperty("password", password);
        PropertiesUtil.saveProperty(file, p);
    }

    @Appfunction(code = "doInfo")
    public Map<Object, Object> doInfo() {
        Map<Object, Object> result = new HashMap<Object, Object>();
        File file = new File(ClassUtil.getClassPath()).getParentFile();
        file = new File(file.getPath() + File.separator
                + ConfigPath.DATABASECONFIG_PATH);
        if (file.exists()) {
            result = PropertiesUtil.loadMap(file);
        }

        return result;

    }
}
