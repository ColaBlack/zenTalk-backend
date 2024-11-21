package cn.cola.zentalk.server.utils;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 邮箱工具类
 *
 * @author ColaBlack
 */
@Component
public class MailUtils {

    /**
     * 通用发送邮件
     *
     * @param title    邮件标题
     * @param content  邮件内容
     * @param receiver 收件人邮箱地址
     */
    private void sendMail(String title, String content, String receiver) {
        MailUtil.send(receiver, title, content, false);
    }

    /**
     * 发送验证码
     *
     * @param receiver 收件人邮箱地址
     * @return 验证码
     */
    public int sendVerificationCode(String receiver) {
        Random random = new Random(new java.util.Date().getTime());
        int code = random.nextInt(900000) + 100000;
        String title = "【林友汇】你的验证码";
        String text = "欢迎注册林友汇，您的验证码为：" + code + "。请在10分钟内输入验证码完成注册。";
        sendMail(title, text, receiver);
        return code;
    }
}
