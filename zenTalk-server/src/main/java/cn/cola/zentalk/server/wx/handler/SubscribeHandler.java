package cn.cola.zentalk.server.wx.handler;

import cn.cola.zentalk.server.wx.utils.WxUtils;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 新订阅处理器
 * 参考:<a href="https://github.com/binarywang/WxJava"></a>
 *
 * @author ColaBlack
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Value("${wx.mp.callback}")
    private String callBackUrl;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) {
        return WxUtils.getUserAuth(wxMpXmlMessage, wxMpService,callBackUrl);
    }

}
