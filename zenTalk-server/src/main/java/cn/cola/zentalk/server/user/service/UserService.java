package cn.cola.zentalk.server.user.service;

import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.model.vo.UserVO;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-11-20 19:53:15
 */
public interface UserService {

    /**
     * 注册服务
     *
     * @param newUser 新用户
     * @return 新用户id
     */
    Long register(User newUser);

    /**
     * 登录服务
     *
     * @param userVO 用户VO对象
     * @return token
     */
    String login(UserVO userVO);
}