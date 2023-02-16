package com.ncamc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ncamc.entity.User;
import com.ncamc.service.LoginService;
import com.wisdge.cloud.dto.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
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

    @ApiOperation("注册")
    @PostMapping("/register")
    public ApiResult register(User user) {
        return loginService.register(user);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResult login(User user) {
        return loginService.login(user);
    }

    @ApiOperation("获取用户名称")
    @GetMapping("/getUsername")
    public ApiResult getUsername(HttpServletRequest request) {
        return loginService.getUsername(request);
    }

    @ApiOperation("获取用户最后登录时间")
    @GetMapping("/getLoginTime")
    public ApiResult getLoginTime(HttpServletRequest request) {
        ApiResult apiResult = loginService.getUsername(request);
        return loginService.getLoginTime(apiResult);
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "original_password", value = "原密码", required = true, dataTypeClass = String.class, example = "原密码"),
            @ApiImplicitParam(name = "new_password", value = "新密码", required = true, dataTypeClass = String.class, example = "新密码")
    })
    public ApiResult updatePassword(@RequestBody Map<String, Object> params) {
        return loginService.updatePassword(params);
    }

    @ApiOperation("查询用户分页信息")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataTypeClass = String.class, example = "用户名")
    })
    public ApiResult list(@RequestBody Map<String, Object> params) {
        Page<User> page = new Page<>(MapUtils.getIntValue(params, "pageNo"), MapUtils.getIntValue(params, "pageSize"));
        Map<String, Object> map = new HashMap<>();
        map.put("username", MapUtils.getString(params, "username"));
        return loginService.listPage(page, params);
    }

    @ApiOperation("根据ID查询该用户")
    @GetMapping("/findById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataTypeClass = Integer.class, example = "ID")
    })
    public ApiResult select(Long id) {
        return loginService.selectById(id);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public ApiResult update(@RequestBody User user) {
        return loginService.updateUser(user);
    }

    @ApiOperation("删除用户信息")
    @GetMapping("/del")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataTypeClass = Integer.class, example = "ID")
    })
    public ApiResult del(Long id) {
        return loginService.deleteByPrimaryKey(id);
    }

    @ApiOperation("退出")
    @GetMapping(value = "/logout")
    public ApiResult exit(HttpServletRequest request) {
        return loginService.exit(request);
    }

}