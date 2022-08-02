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
@TableName("sys_role")
public class Role implements Serializable {
    @TableId("id")
    private Long id;

    private String roleKey;

    private String status;

    private int delFlag;

    private Date createTime;

    private Date updateTime;

    private String remark;

}