package cn.cola.zentalk.server.model.vo;

import cn.cola.zentalk.server.enums.WebSocketRequestTypeEnum;
import lombok.Data;

/**
 * 基础返回类型
 *
 * @author ColaBlack
 */
@Data
public class WebSocketBaseRequest {
    /**
     * 请求类型
     *
     * @see WebSocketRequestTypeEnum
     */
    private Integer type;

    /**
     * 请求数据
     */
    private String data;
}
