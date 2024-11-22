package cn.cola.zentalk.server.wx.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 微信消息服务
 *
 * @author ColaBlack
 */
public interface WxMsgService {

    /**
     * 处理用户扫码消息
     *
     * @param wxMessage   微信消息
     * @param wxMpService 微信服务
     * @return 消息对象
     */
    WxMpXmlOutMessage scanMsg(WxMpXmlMessage wxMessage, WxMpService wxMpService);

    /**
     * 授权用户
     *
     * @param userInfo 用户信息
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
