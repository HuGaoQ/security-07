package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginService extends IService<User> {

    ResponseResult register(User user);

    ResponseResult listPage(Map<String, Object> params);

    ResponseResult login(User user);

    ResponseResult getUsername(HttpServletRequest request);

    User selectById(Long id);

    Object findById(Long id);

    Object deleteByPrimaryKey(Long id);

    ResponseResult exit(HttpServletRequest request);

}