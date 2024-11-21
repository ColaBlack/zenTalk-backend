package cn.cola.zentalk.server.websocket.service;

import io.netty.channel.Channel;

/**
 * WebSocket服务接口
 *
 * @author ColaBlack
 */
public interface WsService {
    /**
     * 建立连接，将channel存储到一个hashmap中
     *
     * @param channel 通道
     */
    void connect(Channel channel);

    /**
     * 获取登录验证码
     *
     * @param channel 登录频道
     */
    void getLoginCode(Channel channel);

    /**
     * 用户下线
     *
     * @param channel 通道
     */
    void userOffLine(Channel channel);
}
