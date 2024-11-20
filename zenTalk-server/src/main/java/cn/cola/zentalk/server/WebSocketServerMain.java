package cn.cola.zentalk.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WebSocket服务器启动类
 *
 * @author ColaBlack
 */
@SpringBootApplication(scanBasePackages = {"cn.cola.zentalk"})
public class WebSocketServerMain {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketServerMain.class, args);
    }

}