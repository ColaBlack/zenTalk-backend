package cn.cola.zentalk.server.wx.service.impl;

import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.exception.BusinessException;
import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.server.dao.UserDao;
import cn.cola.zentalk.server.user.service.UserService;
import cn.cola.zentalk.server.websocket.service.WsService;
import cn.cola.zentalk.server.wx.adapter.TextAdapter;
import cn.cola.zentalk.server.wx.service.WxMsgService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信消息服务实现
 *
 * @author ColaBlack
 */
@Service
@Slf4j
public class WxMsgServiceImpl implements WxMsgService {

    /**
     * 等待授权的用户Map
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHOR_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callBackUrl;

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    private WsService wsService;

    /**
     * 处理用户扫码消息
     *
     * @param wxMessage   微信消息
     * @param wxMpService 微信服务
     * @return 消息对象
     */
    @Override
    public WxMpXmlOutMessage scanMsg(WxMpXmlMessage wxMessage, WxMpService wxMpService) {
        String openId = wxMessage.getFromUser();
        Integer code = getEventKey(wxMessage);
        if (code == null) {
            return null;
        }
        User user = userDao.findUserByOpenId(openId);
        if (user == null) {
            // 未注册去注册
            User newUser = User.builder().openId(openId).build();
            Long id = userService.register(newUser);
            wsService.scanToLogin(code, id);
        } else if (user.getUserAvatar() == null) {
            // 未授权去授权
            WAIT_AUTHOR_MAP.put(openId, code);
            return getUserAuth(wxMessage, wxMpService);
        } else {
            // 未登录去登录
            wsService.scanToLogin(code, user.getUserId());
        }

        return null;
    }

    /**
     * 授权用户
     *
     * @param userInfo 用户信息
     */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.findUserByOpenId(openid);
        if (StringUtils.isAnyBlank(user.getUserAvatar())) {
            user.setUserAvatar(userInfo.getHeadImgUrl());
            user.setNickname(userInfo.getNickname());
            userDao.updateById(user);
        }
        Integer code = WAIT_AUTHOR_MAP.remove(openid);
        wsService.scanToLogin(code, user.getUserId());
    }


    /**
     * 需要引导用户打开的页面的地址模版
     */
    public static final String URL_TEMPLATE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";

    /**
     * 获取用户授权
     *
     * @param wxMpXmlMessage 消息
     * @param wxMpService    微信服务
     * @return 用户授权的url
     */
    private WxMpXmlOutMessage getUserAuth(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService) {
        String authorUrl;
        try {
            authorUrl = String.format(URL_TEMPLATE, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callBackUrl + "/api/wx/portal/public/callBack", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信回调地址异常");
        }

        return TextAdapter.build("请点击下面的链接登录：<a href=\"" + authorUrl + "\">登录</a>", wxMpXmlMessage);
    }

    /**
     * 获取事件key
     *
     * @param wxMpXmlMessage 消息
     * @return key
     */
    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey().replace("qrscene_", "");
            return Integer.parseInt(eventKey);
        } catch (Exception e) {
            log.error("获取事件key异常", e);
            return null;
        }
    }
}
