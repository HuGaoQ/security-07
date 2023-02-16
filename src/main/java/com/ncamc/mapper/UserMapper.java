package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    /**
     * 修改登录次数
     */
    int updateNumberById(@Param("number") Integer number, @Param("id") Long id);

    /**
     * 修改状态
     */
    int updateStatusById(@Param("status") String status, @Param("id") Long id);

    /**
     * 修改登录次数
     */
    int chongZhiNumberById(@Param("number") Integer number, @Param("id") Long id);

    /**
     * 修改密码
     */
    Integer updatePasswordById(@Param("password") String password,@Param("id") Long id);

    /**
     * 查询所有用户
     */
    List<User> FindAll();

    /**
     * 添加登录时间
     */
    void saveLoginTime(@Param("params") Map<String,Object> map);

    /**
     * 获取用户最后登录时间
     * @return
     */
    String getLoginTime(@Param("username") String username);
}