package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
public class Menu implements Serializable {

    @TableId("id")
    private Long id;

    private String menuName;

    private String path;

    private String component;

    private String visaible;

    private String status;

    private String perms;

    private String icon;

    private Date createTime;

    private Date updateTime;

    private int delFlag;

    private String remark;
}