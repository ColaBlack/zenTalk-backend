package cn.cola.zentalk.server.user.service;


import cn.cola.zentalk.common.constant.UserConstant;
import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.exception.ThrowUtils;
import cn.cola.zentalk.common.utils.EncryptUtils;
import cn.cola.zentalk.common.utils.JwtUtils;
import cn.cola.zentalk.model.po.User;
import cn.cola.zentalk.model.vo.UserVO;
import cn.cola.zentalk.server.mapper.UserMapper;
import cn.cola.zentalk.server.utils.MailUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ColaBlack
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-20 19:53:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private MailUtils mailUtils;


    /**
     * 发送验证码服务
     *
     * @param userAccount 账号
     * @param email       邮箱
     * @return 发送结果
     */
    @Override
    public String sendCode(String userAccount, String email) {
        // 从redis缓存中查看是否已经发送了验证码
        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("verification_code");
        ThrowUtils.throwIf(mapCache.containsKey(email), ErrorCode.FORBIDDEN_ERROR, "验证码已发送，请稍后再试");

        // 验证账号是否合法
        ThrowUtils.throwIf(
                !userAccount.matches(UserConstant.ACCOUNT_REGEX), ErrorCode.PARAMS_ERROR, "账号不合法"
        );
        // 验证邮箱是否合法
        ThrowUtils.throwIf(
                !email.matches(UserConstant.EMAIL_REGEX), ErrorCode.PARAMS_ERROR, "邮箱不合法"
        );

        // 生成验证码并发送至邮箱
        int verificationCode = mailUtils.sendVerificationCode(email);

        // 将验证码存入redis缓存
        mapCache.put(email, verificationCode, 10, TimeUnit.MINUTES);
        return "验证码已发送至邮箱";
    }

    /**
     * 注册服务
     *
     * @param nickname      账号
     * @param password      密码
     * @param checkPassword 再次输入的确认密码
     * @param email         邮箱
     * @param code          验证码
     * @return 注册结果
     */
    @Override
    public String register(String nickname, String password, String checkPassword, String email, String code) throws Exception {
        // 验证邮箱是否合法
        ThrowUtils.throwIf(
                !email.matches(UserConstant.EMAIL_REGEX), ErrorCode.PARAMS_ERROR, "邮箱不合法"
        );

        // 从redis缓存中查看验证码是否正确
        RMapCache<Object, Integer> mapCache = redissonClient.getMapCache("verification_code");
        ThrowUtils.throwIf(
                !mapCache.containsKey(email), ErrorCode.FORBIDDEN_ERROR, "验证码不存在或已过期"
        );
        ThrowUtils.throwIf(
                mapCache.get(email) != Integer.parseInt(code), ErrorCode.FORBIDDEN_ERROR, "验证码错误"
        );

        // 验证账号是否合法
        ThrowUtils.throwIf(
                !nickname.matches(UserConstant.ACCOUNT_REGEX), ErrorCode.PARAMS_ERROR, "账号不合法"
        );
        // 验证密码是否合法
        ThrowUtils.throwIf(
                !password.matches(UserConstant.PASSWORD_REGEX), ErrorCode.PARAMS_ERROR, "密码不合法"
        );
        // 验证确认密码是否正确
        ThrowUtils.throwIf(
                !password.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入的密码不一致"
        );

        String encryptPassword = EncryptUtils.encryptPassword(password);
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(encryptPassword);
        user.setEmail(email);

        // 加锁，防止并发注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("nickname", nickname);
        val lock = redissonClient.getLock("registerLock${nickname}");
        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                // 验证账号是否已存在
                ThrowUtils.throwIf(
                        this.baseMapper.exists(queryWrapper), ErrorCode.PARAMS_ERROR, "用户名已存在"
                );

                this.baseMapper.insert(user);
            } else {
                // 获取锁失败，可以抛出异常或返回错误
                throw new Exception("获取锁失败，请稍后重试");
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        // 注册成功，清除验证码缓存
        mapCache.remove(email);

        return "注册成功";
    }

    /**
     * 登录服务
     *
     * @param nickname 账号
     * @param password 密码
     * @param request  servlet请求对象，用于从cookie中清除旧的jwt
     * @param response servlet响应对象，用于将jwt存入cookie
     * @return 登录结果
     */
    @Override
    public UserVO login(String nickname, String password, HttpServletRequest request, HttpServletResponse response) {
        validLoginParam(nickname, password);

        String encryptPassword = EncryptUtils.encryptPassword(password);

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("nickname", nickname)
                .eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);

        ThrowUtils.throwIf(user == null, ErrorCode.FORBIDDEN_ERROR, "账号或密码错误");


        UserVO userVO = new UserVO(user);
        // 生成jwt
        String jwt = JwtUtils.generateToken(userVO);

        // 清除旧的jwt
        for (Cookie cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), UserConstant.USER_LOGIN_STATE)) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        // 将jwt存入redis
        redissonClient.getMapCache(UserConstant.USER_LOGIN_STATE)
                .put(user.getUserId().toString(), jwt, UserConstant.JWT_EXPIRE, TimeUnit.SECONDS);

        // 将jwt存入cookie
        Cookie cookie = new Cookie(UserConstant.USER_LOGIN_STATE, jwt);
        cookie.setPath("/api");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return userVO;
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
        // 获取旧的jwt
        Cookie[] cookies = request.getCookies();
        String oldToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), UserConstant.USER_LOGIN_STATE)) {
                    oldToken = cookie.getValue();
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // 清除redis缓存
        RMapCache<String, String> tokenMap = redissonClient.getMapCache(UserConstant.USER_LOGIN_STATE);
        UserVO userVO = JwtUtils.verifyAndGetUserVO(oldToken);
        if (userVO != null) {
            tokenMap.remove(userVO.getUserId().toString());
        }
        return "注销成功";
    }

    /**
     * 验证登录状态
     * @param token 登录token
     * @return 验证结果
     */
    @Override
    public boolean validLoginStatus(String token) {
        // 验证token是否在redis缓存中
        RMapCache<String, String> tokenMap = redissonClient.getMapCache(UserConstant.USER_LOGIN_STATE);
        UserVO userVO = JwtUtils.verifyAndGetUserVO(token);
        if (userVO != null) {
            return tokenMap.containsKey(userVO.getUserId().toString());
        }
        return false;
    }

    /**
     * 验证登录参数
     *
     * @param userAccount 账号
     * @param password    密码
     */
    private void validLoginParam(String userAccount, String password) {
        // 验证账号是否合法
        ThrowUtils.throwIf(
                !userAccount.matches(UserConstant.ACCOUNT_REGEX), ErrorCode.PARAMS_ERROR, "账号不合法"
        );
        // 验证密码是否合法
        ThrowUtils.throwIf(
                !password.matches(UserConstant.PASSWORD_REGEX), ErrorCode.PARAMS_ERROR, "密码不合法"
        );
    }
}