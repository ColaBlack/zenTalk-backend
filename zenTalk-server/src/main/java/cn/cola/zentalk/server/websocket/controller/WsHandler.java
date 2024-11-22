package cn.cola.zentalk.server.websocket.controller;

import cn.cola.zentalk.common.base.WsBaseRequest;
import cn.cola.zentalk.common.enums.WebSocketRequestTypeEnum;
import cn.cola.zentalk.server.websocket.service.WsService;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket事件处理器，类似于controller层
 *
 * @author ColaBlack
 */
@Slf4j
public class WsHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * WebSocket服务
     */
    private WsService wsService;

    /**
     * 建立WebSocket连接处理
     *
     * @param ctx 通道上下文
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("WebSocket连接建立，通道ID：{}", ctx.channel().id());
        wsService = SpringUtil.getBean(WsService.class);
        wsService.connect(ctx.channel());
    }

    /**
     * 事件处理器
     *
     * @param ctx 通道上下文
     * @param evt 事件对象
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("WebSocket握手成功");
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("WebSocket读超时，即将下线该用户");
                wsService.userOffLine(ctx.channel());
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        WsBaseRequest request = JSONUtil.toBean(text, WsBaseRequest.class);
        switch (WebSocketRequestTypeEnum.findEnumByType(request.getType())) {
            case AUTHORIZE:
                System.out.println("WebSocket握手成功");
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                wsService.getLoginCode(ctx.channel());
                break;
            default:
                log.error("未知的请求类型");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("未知的请求类型"));
                break;
        }
    }

    /**
     * 用户主动下线
     *
     * @param ctx 上下文
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        wsService.userOffLine(ctx.channel());
    }
}
