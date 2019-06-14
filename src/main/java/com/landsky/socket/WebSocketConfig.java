package com.landsky.socket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * 1, webSocket 的配置类, 需要实现接口 ServerApplicationConfig
 * 2, webSocket 类在扫描到之后根据需要在实现的方法中进行一定的过滤, 返回过滤后的才能被前端访问
 * 3, getAnnotatedEndpointClasses 基于注解的 webSocket 扫描方法
 * 4, getEndpointConfigs 基于 XML 配置文件的的 webSocket 扫描方法
 */
@Configuration
class WebSocketconfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
