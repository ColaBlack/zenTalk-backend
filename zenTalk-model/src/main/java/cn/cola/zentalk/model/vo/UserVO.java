package cn.cola.zentalk.model.vo;

import cn.cola.zentalk.model.po.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户VO
 *
 * @author ColaBlack
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别 0.未知 1.男 2.女
     */
    private Integer gender;

    /**
     * 在线状态 1在线 2离线
     */
    private Integer activeStatus;

    /**
     * 最后上下线时间
     */
    private Date lastOnlineTime;

    /**
     * ip信息(json格式)
     */
    private String ipInfo;

    /**
     * 装扮id
     */
    private Long itemId;

    /**
     * 用户角色 0.正常 1.管理员 2.黑名单用户
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public UserVO(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.userAvatar = user.getUserAvatar();
        this.gender = user.getGender();
        this.activeStatus = user.getActiveStatus();
        this.lastOnlineTime = user.getLastOnlineTime();
        this.ipInfo = user.getIpInfo();
        this.itemId = user.getItemId();
        this.role = user.getRole();
        this.createTime = user.getCreateTime();
        this.updateTime = user.getUpdateTime();
    }
}