package cn.cola.zentalk.common.utils;

import cn.cola.zentalk.common.constant.UserConstant;
import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.exception.ThrowUtils;
import cn.cola.zentalk.model.vo.UserVO;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * JWT工具类
 *
 * @author ColaBlack
 */
@Configuration
public class JwtUtils {

    @Value("${zenTall.jwt.secret}")
    private String secret;

    /**
     * 根据 userVO 生成 jwt
     *
     * @param userVO 携带数据
     * @return jwt
     */
    public String generateToken(UserVO userVO) {
        JWT jwt = JWT.create();
        // 设置携带数据
        jwt.setPayload(UserConstant.USER_LOGIN_STATE, userVO);
        // 设置密钥
        jwt.setKey(secret.getBytes());
        // 设置过期时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + UserConstant.JWT_EXPIRE * 1000));
        return jwt.sign();
    }

    /**
     * token 校验
     *
     * @param token token
     * @return 是否通过校验
     */
    public boolean verify(String token) {
        return !StringUtils.isBlank(token) && JWT.of(token).setKey(secret.getBytes()).verify();
    }

    /**
     * token 校验，并获取 userDto
     *
     * @param token token
     * @return userVO
     */
    public UserVO verifyAndGetUserVO(String token) {
        // 验证数据
        if (!verify(token)) {
            return null;
        }
        // 获取jwt
        JWT jwt = JWT.of(token).setKey(secret.getBytes());
        Object payload = jwt.getPayload(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(payload == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(!(payload instanceof JSONObject), ErrorCode.NOT_LOGIN_ERROR);
        // 转换为 userVO
        return JSONUtil.toBean((JSONObject) payload, UserVO.class);
    }

}