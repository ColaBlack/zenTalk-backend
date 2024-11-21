package cn.cola.zentalk.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册数据传输对象
 *
 * @author ColaBlack
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;
}