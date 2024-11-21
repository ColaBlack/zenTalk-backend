package cn.cola.zentalk.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * WebSocket服务器启动类
 *
 * @author ColaBlack
 */
@ComponentScan(basePackages = {"cn.cola.zentalk"})
@MapperScan(basePackages = "cn.cola.zentalk.server.mapper")
@SpringBootApplication(scanBasePackages = {"cn.cola.zentalk"})
public class WebSocketServerMain {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketServerMain.class, args);
    }

}