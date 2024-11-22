package cn.cola.zentalk.server.dao;

import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.server.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户数据访问
 *
 * @author ColaBlack
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    /**
     * 按openId查询用户
     *
     * @param openId 微信openId
     * @return 用户
     */
    public User findUserByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }
}
