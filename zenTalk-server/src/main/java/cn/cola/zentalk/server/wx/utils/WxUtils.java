package cn.cola.zentalk.server.wx.utils;

import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.exception.BusinessException;
import cn.cola.zentalk.server.wx.adapter.TextAdapter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 微信工具类
 *
 * @author ColaBlack
 */
public class WxUtils {

    /**
     * 需要引导用户打开的页面的地址模版
     */
    public static final String URL_TEMPLATE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";

    /**
     * 获取用户授权
     *
     * @param wxMpXmlMessage 消息
     * @param wxMpService    微信服务
     * @param callBackUrl    回调地址
     * @return 用户授权的url
     */
    public static WxMpXmlOutMessage getUserAuth(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService, String callBackUrl) {
        String authorUrl;
        try {
            authorUrl = String.format(URL_TEMPLATE, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callBackUrl + "/api/wx/portal/public/callBack", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信回调地址异常");
        }

        return TextAdapter.build("请点击下面的链接登录：<a href=\"" + authorUrl + "\">登录</a>", wxMpXmlMessage);
    }
}
