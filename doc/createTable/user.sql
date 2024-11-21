create database if not exists zentalk;

use zentalk;

-- 用户表
-- auto-generated definition
create table user
(
    user_id          bigint unsigned auto_increment comment '用户id'
        primary key,
    nickname         varchar(20)                         null comment '用户昵称',
    password         varchar(256)                        not null comment '用户密码',
    email            varchar(256)                        not null comment '用户邮箱',
    user_avatar      varchar(255)                        null comment '用户头像',
    gender           int       default 0                 null comment '性别 0.未知 1.男 2.女',
    active_status    int       default 2                 null comment '在线状态 1在线 2离线',
    last_online_time timestamp default CURRENT_TIMESTAMP not null comment '最后上下线时间',
    ip_info          text                                null comment 'ip信息(json格式)',
    item_id          bigint                              null comment '装扮id',
    role             int       default 0                 null comment '用户角色 0.正常 1.管理员 2.黑名单用户',
    has_deleted      tinyint   default 0                 null comment '是否删除 0.未删除 1.已删除',
    create_time      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_nickname
        unique (nickname)
)
    comment '用户表' collate = utf8mb4_unicode_ci
                     row_format = DYNAMIC;

create index idx_active_status_last_opt_time
    on user (active_status, last_online_time);

create index idx_create_time
    on user (create_time);

create index idx_update_time
    on user (update_time);

