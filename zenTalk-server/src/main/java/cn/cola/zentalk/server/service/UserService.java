package cn.cola.zentalk.server.service;


import cn.cola.zentalk.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-11-20 19:53:15
 */
public interface UserService extends IService<User> {

    /**
     * 登录成功，获取token
     *
     * @param uid 用户id
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 刷新token有效期
     *
     * @param token token
     */
    void renewalToken(String token);

    /**
     * 如果token有效，返回userId
     *
     * @param token token
     * @return 返回userId
     */
    Long validUserId(String token);
}
