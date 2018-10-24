package com.framework.plugin.web.common.web.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.framework.plugin.web.common.stereotype.Appcode;

@EnableWebMvc
@Configuration
@ComponentScan(includeFilters = { @Filter(value = Component.class),
        @Filter(value = Controller.class), @Filter(value = Appcode.class),
        @Filter(value = Service.class) })
public class ServletConfig {

}
