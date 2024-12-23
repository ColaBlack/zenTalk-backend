package cn.cola.zentalk.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * WebSocket响应类型枚举
 *
 * @author ColaBlack
 */
@AllArgsConstructor
@Getter
public enum WsResponseEnum {
    /**
     * 登录二维码返回
     */
    SENT_LOGIN_URL(1, "登录二维码返回"),

    /**
     * 用户扫描二维码成功等待授权
     */
    LOGIN_SCAN_SUCCESS(2, "用户扫描成功等待授权"),

    /**
     * 用户登录成功返回用户信息
     */
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息");
//
//    /**
//     * 新消息
//     */
//    MESSAGE(4, "新消息", WSMessage.class),
//
//    /**
//     * 上下线通知
//     */
//    ONLINE_OFFLINE_NOTIFY(5, "上下线通知", WSOnlineOfflineNotify.class),
//
//    /**
//     * 使前端的token失效，意味着前端需要重新登录
//     */
//    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
//
//    /**
//     * 拉黑用户
//     */
//    BLACK(7, "拉黑用户", WSBlack.class),
//
//    /**
//     * 消息标记
//     */
//    MARK(8, "消息标记", WSMsgMark.class),
//
//    /**
//     * 消息撤回
//     */
//    RECALL(9, "消息撤回", WSMsgRecall.class),
//
//    /**
//     * 好友申请
//     */
//    APPLY(10, "好友申请", WSFriendApply.class),
//
//    /**
//     * 成员变动
//     */
//    MEMBER_CHANGE(11, "成员变动", WSMemberChange.class);

    /**
     * 响应类型
     */
    private final int type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 缓存类型和枚举的映射关系
     */
    private static final Map<Integer, WsResponseEnum> CACHE = Arrays
            .stream(WsResponseEnum.values())
            .collect(Collectors.toMap(WsResponseEnum::getType, Function.identity()));

    /**
     * 根据请求类型编号获取枚举对象
     *
     * @param type 请求类型编号
     * @return 请求类型枚举对象
     */
    public static WsResponseEnum getEnumByType(Integer type) {
        return CACHE.get(type);
    }
}
