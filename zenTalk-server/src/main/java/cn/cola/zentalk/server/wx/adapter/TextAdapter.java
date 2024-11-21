package cn.cola.zentalk.server.wx.adapter;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 发送给用户的微信公众号信息封装
 *
 * @author ColaBlack
 */
public class TextAdapter {

    /**
     * 将context文本封装成微信消息
     *
     * @param content   待封装的文本
     * @param wxMessage 微信请求对象
     * @return 消息对象
     */
    public static WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage) {
        return WxMpXmlOutMessage
                .TEXT()
                .content(content)
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
    }
}
