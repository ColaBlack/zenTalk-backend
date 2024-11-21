package cn.cola.zentalk.server.user.controller;

import cn.cola.zentalk.common.base.BaseResponse;
import cn.cola.zentalk.common.constant.UserConstant;
import cn.cola.zentalk.common.enums.ErrorCode;
import cn.cola.zentalk.common.exception.BusinessException;
import cn.cola.zentalk.common.exception.ThrowUtils;
import cn.cola.zentalk.common.utils.JwtUtils;
import cn.cola.zentalk.common.utils.ResultUtils;
import cn.cola.zentalk.model.dto.user.LoginDTO;
import cn.cola.zentalk.model.dto.user.RegisterDTO;
import cn.cola.zentalk.model.vo.UserVO;
import cn.cola.zentalk.server.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 用户控制器
 *
 * @author ColaBlack
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 发送验证码
     */
    @GetMapping("/sendEmail")
    public BaseResponse<String> sendEmail(
            @RequestParam(value = "userAccount") String userAccount,
            @RequestParam(value = "email") String email) {
        return ResultUtils.success(userService.sendCode(userAccount, email));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody RegisterDTO registerDTO) {
        String userAccount = registerDTO.getUserAccount();
        String password = registerDTO.getPassword();
        String checkPassword = registerDTO.getCheckPassword();
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();
        String ret;
        try {
            ret = userService.register(userAccount, password, checkPassword, email, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(ret);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody LoginDTO loginDTO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        String userAccount = loginDTO.getUserAccount();
        String password = loginDTO.getPassword();
        UserVO ret = userService.login(userAccount, password, request, response);
        return ResultUtils.success(ret);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String ret = userService.logout(request, response);
        return ResultUtils.success(ret);
    }

    /**
     * 获取登录用户信息
     */
    @GetMapping("/get/UserVO")
    public BaseResponse<UserVO> getUserVO(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        ThrowUtils.throwIf(cookies == null || cookies.length == 0, ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        Cookie cookie = Arrays.stream(cookies)
                .filter(
                        c -> c.getName()
                                .equals(UserConstant.USER_LOGIN_STATE))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录"));
        boolean hasPassVaild = userService.validLoginStatus(cookie.getValue());
        ThrowUtils.throwIf(!hasPassVaild, ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        UserVO ret = JwtUtils.verifyAndGetUserVO(cookie.getValue());
        return ResultUtils.success(ret);
    }
}