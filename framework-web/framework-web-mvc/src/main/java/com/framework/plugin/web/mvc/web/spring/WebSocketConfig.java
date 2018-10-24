package com.framework.plugin.web.common.web.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.framework.plugin.web.common.web.spring.handler.DefaultWebSocketHandler;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements
        WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(defaultWebSocketHandler(), "/websocket")
                .withSockJS();

        // registry.addHandler(defaultWebSocketHandler(), "/websocket-sockjs")

        // registry.addHandler(echoWebSocketHandler(), "/sockjs/echo-issue4")
        // .withSockJS().setHttpMessageCacheSize(20000);

    }

    @Bean
    public WebSocketHandler defaultWebSocketHandler() {
        return new DefaultWebSocketHandler();
    }

    // Allow serving HTML files through the default Servlet

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
