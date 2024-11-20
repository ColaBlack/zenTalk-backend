package cn.cola.zentalk.server.websocket;

import cn.cola.zentalk.server.enums.WebSocketRequestTypeEnum;
import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket处理器
 *
 * @author ColaBlack
 */
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(NettyWebSocketServerHandler.class);

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
                //todo 用户下线
                ctx.channel().close();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        WebSocketRequestTypeEnum wsBaseReq = JSONUtil.toBean(text, WebSocketRequestTypeEnum.class);
        switch (WebSocketRequestTypeEnum.findEnumByType(wsBaseReq.getType())) {
            case AUTHORIZE:
            case HEARTBEAT:
                break;
            case LOGIN:
                System.out.println("请求二维码");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("123"));
                break;
            default:
                log.error("未知的请求类型");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("未知的请求类型"));
                break;
        }
    }
}
