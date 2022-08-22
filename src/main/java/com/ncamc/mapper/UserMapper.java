package com.ncamc.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.User;

public interface UserMapper extends BaseMapper<User> {
    /**
     * 修改登录次数
     * @param number
     * @param id
     * @return
     */
    int updateNumberById(@Param("number") Integer number, @Param("id") Long id);

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    int updateStatusById(@Param("status") String status, @Param("id") Long id);

}