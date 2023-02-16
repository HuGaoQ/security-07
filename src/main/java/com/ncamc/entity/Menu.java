package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_menu")
public class Menu implements Serializable {

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 菜单名
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 路由地址
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组建路径
     */
    @TableField(value = "component")
    private String component;

    /**
     * 菜单状态（0正常  1.停用）
     */
    @TableField(value = "visaible")
    private String visaible;

    /**
     * 菜单状态（0正常  1.停用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除（0.未删除  1.已删除）
     */
    @TableLogic
    @TableField(value = "del_flag")
    private int delFlag;

    /**
     * 评论
     */
    @TableField(value = "remark")
    private String remark;
}