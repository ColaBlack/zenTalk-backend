package cn.cola.zentalk.server.websocket.service.impl;

import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.enums.WebSocketResponseTypeEnum;
import cn.cola.zentalk.common.exception.BusinessException;
import cn.cola.zentalk.model.po.WebSocketInfo;
import cn.cola.zentalk.server.websocket.service.WebSocketService;
import cn.cola.zentalk.server.wx.utils.WsReturnUtils;
import cn.hutool.core.util.RandomUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务实现类
 *
 * @author ColaBlack
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * 同时管理了游客和在线用户的channel
     */
    private static final ConcurrentHashMap<Channel, WebSocketInfo> ONLINE_MAP = new ConcurrentHashMap<>();

    /**
     * 过期时间
     */
    public static final int EXPIRE_TIME = 60 * 60 * 24;

    /**
     * 待登录用户缓存最大数
     */
    public static final int MAXIMUM_SIZE = 1000;

    @Resource
    private WxMpService wxMpService;

    private static final Cache<Integer, Channel> LOGIN_MAP = Caffeine
            .newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(Duration.ofSeconds(EXPIRE_TIME))
            .build();

    /**
     * 建立连接，将channel存储到一个hashmap中
     *
     * @param channel 通道
     */
    @Override

    public void connect(Channel channel) {
        ONLINE_MAP.put(channel, new WebSocketInfo());
    }

    /**
     * 获取登录验证码
     */
    @Override
    public void getLoginCode(Channel channel) {
        // 生成随机验证码
        int code = generateCode(channel);
        WxMpQrCodeTicket qrCodeTicket;
        try {
            qrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, EXPIRE_TIME);
        } catch (WxErrorException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统内部和微信交互失败");
        }
        String url = qrCodeTicket.getUrl();
        WsReturnUtils.success(channel, WebSocketResponseTypeEnum.SENT_LOGIN_URL, url);
    }

    private int generateCode(Channel channel) {
        int code;
        do {
            code = RandomUtil.randomInt(100000, 999999);
        } while (!Objects.isNull(LOGIN_MAP.getIfPresent(code)));
        return code;
    }
}
