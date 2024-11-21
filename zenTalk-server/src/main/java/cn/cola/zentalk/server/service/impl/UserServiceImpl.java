package cn.cola.zentalk.server.service.impl;

import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.server.mapper.UserMapper;
import cn.cola.zentalk.server.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-20 19:53:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 登录成功，获取token
     *
     * @param uid 用户id
     * @return 返回token
     */
    @Override
    public String login(Long uid) {
        return "";
    }

    /**
     * 刷新token有效期
     *
     * @param token token
     */
    @Override
    public void renewalToken(String token) {

    }

    /**
     * 如果token有效，返回userId
     *
     * @param token token
     * @return 返回userId
     */
    @Override
    public Long validUserId(String token) {
        return 0L;
    }
}




