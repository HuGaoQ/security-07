package com.ncamc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.User;
import com.wisdge.cloud.dto.ApiResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginService extends IService<User> {

    /**
     * 注册用户
     * @param user
     * @return
     */
    ApiResult register(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    ApiResult login(User user);

    /**
     * 获取用户名称
     * @param request
     * @return
     */
    ApiResult getUsername(HttpServletRequest request);

    /**
     * 分页查询用户
     * @param params
     * @return
     */
    ApiResult listPage(Page<User>page, Map<String, Object> params);

    /**
     * 根据ID查询该用户
     *
     * @param id
     * @return
     */
    ApiResult selectById(Long id);

    /**
     * 根据ID删除该条数据
     * @param id
     * @return
     */
    ApiResult deleteByPrimaryKey(Long id);

    /**
     * 退出
     * @param request
     * @return
     */
    ApiResult exit(HttpServletRequest request);

}