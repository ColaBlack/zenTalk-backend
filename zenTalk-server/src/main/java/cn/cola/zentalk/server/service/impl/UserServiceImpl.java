package cn.cola.zentalk.server.service.impl;

import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.model.vo.UserVO;
import cn.cola.zentalk.server.mapper.UserMapper;
import cn.cola.zentalk.server.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-20 19:53:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 发送验证码服务
     *
     * @param userAccount 账号
     * @param email       邮箱
     * @return 发送结果
     */
    @Override
    public String sendCode(String userAccount, String email) {
        return "";
    }

    /**
     * 注册服务
     *
     * @param userAccount   账号
     * @param password      密码
     * @param checkPassword 再次输入的确认密码
     * @param email         邮箱
     * @param code          验证码
     * @return 注册结果
     */
    @Override
    public String register(String userAccount, String password, String checkPassword, String email, String code) {
        return "";
    }

    /**
     * 登录服务
     *
     * @param userAccount 账号
     * @param password    密码
     * @param request     servlet请求对象，用于从cookie中清除旧的jwt
     * @param response    servlet响应对象，用于将jwt存入cookie
     * @return 登录结果
     */
    @Override
    public UserVO login(String userAccount, String password, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * 注销服务
     *
     * @param request  servlet请求对象，用于获取旧cookie
     * @param response servlet响应对象，用于清除cookie
     * @return 注销结果
     */
    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }
}




