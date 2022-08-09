package com.ncamc.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.User;

public interface UserMapper extends BaseMapper<User> {

    int updateNumberById(@Param("number") Integer number, @Param("id") Long id);

    int updateStatusById(@Param("status") String status, @Param("id") Long id);

}