package cn.cola.zentalk.common.utils;

import cn.cola.zentalk.common.constant.UserConstant;
import cn.hutool.crypto.SecureUtil;

/**
 * 密码加密工具类
 *
 * @author ColaBlack
 */
public class EncryptUtils {

    public static String encryptPassword(String password) {
        String salt = UserConstant.SALT;
        return SecureUtil.sha256(salt + password);
    }
}
