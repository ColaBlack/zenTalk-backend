package cn.cola.zentalk.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录数据传输对象
 *
 * @author ColaBlack
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

}
