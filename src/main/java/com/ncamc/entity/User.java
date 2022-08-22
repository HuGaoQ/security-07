package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "sys_user")
public class User implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 账户
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户名
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 登陆时间
     */
    @TableField(value = "login_time")
    private String loginTime;

    /**
     * 登录次数
     */
    @TableField(value = "number")
    private Integer number;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 是否删除（0.未删除  1.已删除）
     */
    @TableLogic
    @TableField(value = "del_flag")
    private Integer delFlag;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}