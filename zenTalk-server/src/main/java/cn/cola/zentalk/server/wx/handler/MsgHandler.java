package cn.cola.zentalk.server.wx.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息处理器
 * 参考:<a href="https://github.com/binarywang/WxJava"></a>
 *
 * @author ColaBlack
 */
@Component
public class MsgHandler extends AbstractHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) {
        return null;
    }

}
