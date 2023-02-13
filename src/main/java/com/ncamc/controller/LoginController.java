package com.ncamc.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ncamc.entity.User;
import com.ncamc.internal.Constant;
import com.ncamc.service.LoginService;
import com.ncamc.utils.RedisCache;
import com.wisdge.cloud.dto.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static final String CACHE_KEY_LOGIN = "login:";

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

    @ApiOperation("查询名称")
    @GetMapping("/getUsername")
    public ApiResult getUsername(HttpServletRequest request) {
        return loginService.getUsername(request);
    }

    @ApiOperation("查询用户分页信息")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "username", value = "用户名", dataTypeClass = String.class, example = "用户名")
    })
    public ApiResult list(@RequestBody Map<String, Object> params) {
        Page<User> page = new Page<>(MapUtils.getIntValue(params,"pageNo"),MapUtils.getIntValue(params,"pageSize"));
        Map<String,Object> map = new HashMap<>();
        map.put("username",MapUtils.getString(params,"username"));
        return loginService.listPage(page,params);
    }

    @ApiOperation("查询1条记录")
    @GetMapping("/findById")
    @PreAuthorize("hasAuthority('system:dept:list')")
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
        try {
            UpdateWrapper wrapper = new UpdateWrapper<>();
            wrapper.eq("id", user.getId());
            ApiResult apiResult = loginService.selectById(user.getId());
            User userId =(User) apiResult.getData();
            if (!(user.getPassword().equals(userId.getPassword()))) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            boolean b = loginService.update(user, wrapper);
            if (b) {
                redisCache.deleteObject(CACHE_KEY_LOGIN + user.getId());
                redisCache.setCacheObject(CACHE_KEY_LOGIN + user.getId(), user);
            }
            return ApiResult.ok(HttpStatus.OK.value(), "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.ok(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改失败");
        }
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

    @ApiOperation("登出")
    @GetMapping(value = "/logout")
    public ApiResult exit(HttpServletRequest request) {
        return ApiResult.ok(Constant.STR_EMPTY,loginService.exit(request));
    }

}