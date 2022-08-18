package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginService extends IService<User> {

    /**
     * 注册用户
     * @param user
     * @return
     */
    ResponseResult register(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult login(User user);

    /**
     * 获取用户名称
     * @param request
     * @return
     */
    ResponseResult getUsername(HttpServletRequest request);

    /**
     * 分页查询用户
     * @param params
     * @return
     */
    ResponseResult listPage(Map<String, Object> params);

    /**
     * 根据ID查询该用户
     * @param id
     * @return
     */
    User selectById(Long id);

    /**
     * 根据ID删除该条数据
     * @param id
     * @return
     */
    Object deleteByPrimaryKey(Long id);

    /**
     * 退出
     * @param request
     * @return
     */
    ResponseResult exit(HttpServletRequest request);

}