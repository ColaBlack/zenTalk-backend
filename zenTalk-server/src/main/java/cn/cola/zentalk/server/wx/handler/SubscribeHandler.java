package cn.cola.zentalk.server.wx.handler;

import cn.cola.zentalk.server.wx.adapter.TextAdapter;
import cn.cola.zentalk.server.wx.service.WxMsgService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 新订阅处理器
 * 参考:<a href="https://github.com/binarywang/WxJava"></a>
 *
 * @author ColaBlack
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Resource
    private WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) {
        logger.info("新订阅:用户id{}", wxMpXmlMessage.getFromUser());
        WxMpXmlOutMessage res = wxMsgService.scanMsg(wxMpXmlMessage, wxMpService);
        return res == null ? TextAdapter.build("欢迎订阅", wxMpXmlMessage) : res;
    }

}
