package cn.cola.zentalk.server.wx.handler;

import cn.cola.zentalk.server.wx.service.WxMsgService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户扫码处理器
 * 参考:<a href="https://github.com/binarywang/WxJava"></a>
 *
 * @author ColaBlack
 */
@Component
public class ScanHandler extends AbstractHandler {

    @Resource
    private WxMsgService wxMsgService;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage mpXmlMessage, Map<String, Object> map,
                                    WxMpService mpService, WxSessionManager wxSessionManager) {
        return wxMsgService.scanMsg(mpXmlMessage, mpService);
    }

}
