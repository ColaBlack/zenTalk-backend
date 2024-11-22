package cn.cola.zentalk.server.websocket.service.impl;

import cn.cola.zentalk.common.constant.WsConstant;
import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.enums.WsResponseEnum;
import cn.cola.zentalk.common.exception.BusinessException;
import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.model.po.WebSocketInfo;
import cn.cola.zentalk.server.dao.UserDao;
import cn.cola.zentalk.server.user.service.UserService;
import cn.cola.zentalk.server.websocket.service.WsService;
import cn.cola.zentalk.server.wx.utils.WsReturnUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.annotation.Lazy;
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
public class WsServiceImpl implements WsService {

    /**
     * 同时管理了游客和在线用户的channel
     */
    private static final ConcurrentHashMap<Channel, WebSocketInfo> ONLINE_MAP = new ConcurrentHashMap<>();


    @Resource
    @Lazy
    private WxMpService wxMpService;

    private static final Cache<Integer, Channel> LOGIN_MAP = Caffeine
            .newBuilder()
            .maximumSize(WsConstant.MAXIMUM_SIZE)
            .expireAfterWrite(Duration.ofSeconds(WsConstant.CODE_EXPIRE_TIME))
            .build();

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

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
            qrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, WsConstant.CODE_EXPIRE_TIME);
        } catch (WxErrorException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统内部和微信交互失败");
        }
        String url = qrCodeTicket.getUrl();
        WsReturnUtils.success(channel, WsResponseEnum.SENT_LOGIN_URL, url);
    }

    /**
     * 用户下线
     *
     * @param channel 通道
     */
    @Override
    public void userOffLine(Channel channel) {
        ONLINE_MAP.remove(channel);
        channel.close();
    }

    /**
     * 扫码登录
     *
     * @param code   授权码
     * @param userId 用户id
     */
    @Override
    public void scanToLogin(Integer code, Long userId) {
        Channel channel = LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getById(userId);
        LOGIN_MAP.invalidate(code);
        // todo 获取token
        String token = userService.login(userId);
        // todo 将token等返回
        WsReturnUtils.success(channel, WsResponseEnum.LOGIN_SUCCESS, JSONUtil.toJsonStr(token));
    }

    private int generateCode(Channel channel) {
        int code;
        do {
            code = RandomUtil.randomInt(100000, 999999);
        } while (!Objects.isNull(LOGIN_MAP.getIfPresent(code)));
        return code;
    }
}
