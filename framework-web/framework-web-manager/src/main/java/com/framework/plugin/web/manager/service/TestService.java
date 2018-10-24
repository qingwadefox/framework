package com.framework.plugin.web.system.service;


import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;

@Appcode(code = "test", description = "test", name = "test")
public class TestService {

    @Appfunction(code = "test", name = "test", paramNames = { "test" }, description = "test")
    public String test(String test) {
        return test;
    }
}
