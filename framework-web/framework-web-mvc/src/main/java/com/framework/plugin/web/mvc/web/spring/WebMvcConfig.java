package com.framework.plugin.web.common.web.spring;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.framework.plugin.web.common.web.spring.handler.ExceptionHandler;
import com.framework.plugin.web.common.web.spring.listener.FileUploadListener;
import com.framework.plugin.web.common.web.spring.utils.SpringUtil;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/common/**").addResourceLocations(
                "/common/");
        registry.addResourceHandler("/system/**").addResourceLocations(
                "/system/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new ExceptionHandler());
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        MappingJackson2HttpMessageConverter mj2hmc = new MappingJackson2HttpMessageConverter();
        super.requestMappingHandlerAdapter().getMessageConverters().add(mj2hmc);
        return super.requestMappingHandlerAdapter();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxInMemorySize(40960);
        multipartResolver.setMaxUploadSize(10485760000L);
        FileUploadListener uploadListener = new FileUploadListener();

        try {
            multipartResolver.setUploadTempDir(SpringUtil.applicationContext
                    .getResource(ConfigPath.TEMPFILEUPLOAD_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartResolver;
    }

}
