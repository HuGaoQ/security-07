package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private Date createTime;

    private Date loginTime;

    private Integer number;

    private String status;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}