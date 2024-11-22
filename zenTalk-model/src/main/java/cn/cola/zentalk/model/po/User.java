package cn.cola.zentalk.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户PO
 *
 * @author ColaBlack
 * @TableName user
 */
@TableName(value = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别 0.未知 1.男 2.女
     */
    private Integer gender;

    /**
     * 微信openid
     */
    private String openId;

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
     * 是否删除 0.未删除 1.已删除
     */
    @TableLogic
    private Integer hasDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}