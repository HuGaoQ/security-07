package com.ncamc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("sys_prd")
@NoArgsConstructor
public class Product implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 产品名称
     */
    @TableField("prd_name")
    private String prdName;

    /**
     * 产品代码
     */
    @TableField("prd_dm")
    private String prdDm;

    /**
     * 净值
     */
    @TableField("net")
    private String net;

    /**
     * 份额
     */
    @TableField("fbalance")
    private String fbalance;

    /**
     * 金额
     */
    @TableField("favalable")
    private String favalable;

    /**
     * 机构名称
     */
    @TableField("ins_name")
    private String insName;

    /**
     * 机构代码
     */
    @TableField("ins_dm")
    private String insDm;

    /**
     * 日期
     */
    @TableField("new_date")
    private String newDate;

    /**
     * 当前时间
     */
    @TableField("new_time")
    private String newTime;

    /**
     * 是否删除（0.未删除  1.已删除）
     */
    @TableField("del_flag")
    private int delFlag;

}