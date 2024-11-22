package cn.cola.zentalk.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录返回信息
 *
 * @author ColaBlack
 */
@Data
@AllArgsConstructor
public class LoginVO implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * token
     */
    private String token;
}
