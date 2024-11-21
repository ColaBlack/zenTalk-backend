package cn.cola.zentalk.server.service;


import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.model.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-11-20 19:53:15
 */
public interface UserService extends IService<User> {

    /**
     * 发送验证码服务
     *
     * @param userAccount 账号
     * @param email       邮箱
     * @return 发送结果
     */
    public String sendCode(String userAccount, String email);

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
    public String register(
            String userAccount,
            String password,
            String checkPassword,
            String email,
            String code);

    /**
     * 登录服务
     *
     * @param userAccount 账号
     * @param password    密码
     * @param request     servlet请求对象，用于从cookie中清除旧的jwt
     * @param response    servlet响应对象，用于将jwt存入cookie
     * @return 登录结果
     */
    public UserVO login(
            String userAccount,
            String password,
            HttpServletRequest request,
            HttpServletResponse response);

    /**
     * 注销服务
     *
     * @param request  servlet请求对象，用于获取旧cookie
     * @param response servlet响应对象，用于清除cookie
     * @return 注销结果
     */
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response);

}