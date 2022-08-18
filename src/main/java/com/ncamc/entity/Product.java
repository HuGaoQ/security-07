package com.ncamc.entity;

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
    private String prdName;

    /**
     * 产品代码
     */
    private String prdDm;

    /**
     * 净值
     */
    private String net;

    /**
     * 份额
     */
    private String fbalance;

    /**
     * 金额
     */
    private String favalable;

    /**
     * 机构名称
     */
    private String insName;

    /**
     * 机构代码
     */
    private String insDm;

    /**
     * 日期
     */
    private String newDate;

    /**
     * 当前时间
     */
    private String newTime;

}