package cn.cola.zentalk.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 基础请求类型
 *
 * @author ColaBlack
 */
@Data
public class BaseRequest implements Serializable {
    /**
     * 请求类型
     *
     * @see cn.cola.zentalk.common.enums.WebSocketRequestTypeEnum
     */
    private int type;

    /**
     * 请求数据
     */
    private String data;
}
