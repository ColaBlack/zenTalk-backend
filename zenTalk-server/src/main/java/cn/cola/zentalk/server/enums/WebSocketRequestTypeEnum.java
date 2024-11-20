package cn.cola.zentalk.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * websocket请求类型枚举
 *
 * @author ColaBlack
 */
@AllArgsConstructor
@Getter
public enum WebSocketRequestTypeEnum {
    /**
     * 心跳请求
     */
    HEARTBEAT(0, "心跳信息"),
    /**
     * 登录请求
     */
    LOGIN(1, "请求登录二维码"),
    /**
     * 用户认证请求
     */
    AUTHORIZE(2, "用户认证");

    /**
     * 请求类型编号
     */
    private final Integer type;

    /**
     * 请求类型描述
     */
    private final String desc;

    /**
     * 缓存类型和枚举的映射关系
     */
    private static final Map<Integer, WebSocketRequestTypeEnum> CACHE = Arrays
            .stream(WebSocketRequestTypeEnum.values())
            .collect(Collectors.toMap(WebSocketRequestTypeEnum::getType, Function.identity()));


    /**
     * 根据请求类型编号获取枚举对象
     * @param type 请求类型编号
     * @return 请求类型枚举对象
     */
    public static WebSocketRequestTypeEnum findEnumByType(Integer type) {
        return CACHE.get(type);
    }
}
