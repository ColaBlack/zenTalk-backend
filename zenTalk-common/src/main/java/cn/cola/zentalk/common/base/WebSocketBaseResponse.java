package cn.cola.zentalk.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础响应类型
 *
 * @author ColaBlack
 */
@Data
public class WebSocketBaseResponse<T> implements Serializable {

    /**
     * 响应类型
     *
     * @see cn.cola.zentalk.common.enums.WebSocketResponseTypeEnum
     */
    private int type;

    /**
     * 业务状态码
     *
     * @see cn.cola.zentalk.common.enums.ErrorCode
     */
    private int code;

    /**
     * 响应数据
     */
    private T data;

}
