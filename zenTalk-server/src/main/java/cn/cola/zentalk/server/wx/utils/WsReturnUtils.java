package cn.cola.zentalk.server.wx.utils;

import cn.cola.zentalk.common.base.WebSocketBaseResponse;
import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.enums.WebSocketResponseTypeEnum;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocket消息推送
 *
 * @author ColaBlack
 */
public class WsReturnUtils {

    public static void success(Channel channel, WebSocketResponseTypeEnum enums, Object data) {
        WebSocketBaseResponse<?> response = new WebSocketBaseResponse<>(enums.getType(), ErrorCode.SUCCESS.getCode(), data);
        TextWebSocketFrame frame = new TextWebSocketFrame(JSONUtil.toJsonStr(response));
        channel.writeAndFlush(frame);
    }

}