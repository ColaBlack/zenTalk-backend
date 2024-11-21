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
 * 用户扫码处理器
 * 参考:<a href="https://github.com/binarywang/WxJava"></a>
 *
 * @author ColaBlack
 */
@Component
public class ScanHandler extends AbstractHandler {

    @Value("${wx.mp.callback}")
    private String callBackUrl;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage mpXmlMessage, Map<String, Object> map,
                                    WxMpService mpService, WxSessionManager wxSessionManager) {
        return WxUtils.getUserAuth(mpXmlMessage, mpService, callBackUrl);
    }

}
