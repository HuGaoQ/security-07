package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_user")
public class User implements Serializable {

    @TableId(value = "id")
    private Long id;

    private String username;

    private String nickName;

    private String password;

    private String createTime;

    private String loginTime;

    private Integer number;

    private String status;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}