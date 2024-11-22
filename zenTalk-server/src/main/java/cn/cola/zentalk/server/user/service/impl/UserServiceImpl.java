package cn.cola.zentalk.server.user.service.impl;


import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.server.dao.UserDao;
import cn.cola.zentalk.server.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-20 19:53:15
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 注册服务
     *
     * @param newUser 新用户
     * @return 新用户id
     */
    @Override
    @Transactional
    public Long register(User newUser) {
        userDao.save(newUser);
        return newUser.getUserId();
    }

    /**
     * 登录服务
     *
     * @param userId 用户id
     * @return token
     */
    @Override
    public String login(Long userId) {
        return "12313";
    }
}