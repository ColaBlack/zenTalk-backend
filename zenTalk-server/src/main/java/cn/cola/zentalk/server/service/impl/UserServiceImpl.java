package cn.cola.zentalk.server.service.impl;

import cn.cola.zentalk.server.mapper.UserMapper;
import cn.cola.zentalk.server.model.po.User;
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

}




