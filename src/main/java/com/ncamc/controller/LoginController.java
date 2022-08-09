package com.ncamc.controller;

import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;
import com.ncamc.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 09:36
 * An unhandled exception occurred while precessing the request.Exception:
 * Throw ExceptionKFC Crazy Thursday need $50.
 */
@Slf4j
@Api("用户user接口")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("hello")
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public String hello() {
        return "Because live you everyday";
    }

    @ApiOperation("查询用户信息")
    @GetMapping("/list")
    public ResponseResult list() {
        List<User> list = loginService.list();
        return new ResponseResult(200, "查询成功", list);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseResult login(User user) {
        return loginService.login(user);
    }

    @ApiOperation("查询名称")
    @GetMapping("/getUsername")
    public ResponseResult getUsername(HttpServletRequest request) {
        return loginService.getUsername(request);
    }

    @ApiOperation("登出")
    @GetMapping(value = "/logout")
    public ResponseResult exit(HttpServletRequest request) {
        return loginService.exit(request);
    }

}