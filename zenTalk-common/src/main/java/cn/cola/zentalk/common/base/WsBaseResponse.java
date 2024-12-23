package cn.cola.zentalk.common.base;

import cn.cola.zentalk.common.enums.WsResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础响应类型
 *
 * @author ColaBlack
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsBaseResponse<T> implements Serializable {

    /**
     * 响应类型
     *
     * @see WsResponseEnum
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
