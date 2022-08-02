package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface LoginService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult getUsername(HttpServletRequest request);

    ResponseResult exit(HttpServletRequest request);

}