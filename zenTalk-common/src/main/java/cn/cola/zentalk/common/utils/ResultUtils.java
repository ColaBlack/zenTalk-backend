package cn.cola.zentalk.common.utils;


import cn.cola.zentalk.common.base.BaseResponse;
import cn.cola.zentalk.common.enums.ErrorCode;

import javax.lang.model.type.NullType;

/**
 * 返回工具类
 *
 * @author ColaBlack
 */
public class ResultUtils {

    /**
     * 请求成功返回工具
     *
     * @param data 返回给前端的数据
     * @param <T>  返回的数据类型
     * @return 成功返回的结果
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, "ok");
    }

    /**
     * 请求失败返回工具
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 失败返回的结果
     */
    public static BaseResponse<NullType> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 请求失败返回工具
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @return 失败返回的结果
     */
    public static BaseResponse<NullType> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
