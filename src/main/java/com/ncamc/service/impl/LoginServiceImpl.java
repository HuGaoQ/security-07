package com.ncamc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.User;
import com.ncamc.internal.Constant;
import com.ncamc.mapper.UserMapper;
import com.ncamc.service.LoginService;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import com.wisdge.cloud.dto.ApiResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:26
 */
@Slf4j
@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static final String LOGIN_TOKEN = "login_token:";
    public static final String LOGIN_USER = "login_user:";

    /**
     * 注册用户
     */
    @Override
    public ApiResult register(User user) {
        ApiResult apiResult = null;
        try {
            if (!StringUtils.isEmpty(user)) {
                SimpleDateFormat createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<User> users = userMapper.FindAll();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setCreateTime(createTime.format(new Date()));
                user.setLoginTime(loginTime.format(new Date()));
                user.setId(Long.parseLong(String.valueOf(users.size()+1)));
                userMapper.insert(user);
                apiResult = new ApiResult(HttpStatus.OK.value(),Constant.STR_RGISTER_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResult = new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constant.STR_RGISTER_DEL);
        }

        return apiResult;
    }

    /**
     * 登录
     */
    @SneakyThrows
    @Override
    public ApiResult login(User users) {
        ApiResult apiResult = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if (Objects.isNull(loginUser)) {
            return ApiResult.ok(HttpStatus.FOUND.value(), Constant.STR_ACCOUNT_OR_PASSWORD_ERROR);
        }
        User user = loginUser.getUser();
        if ("0".equals(user.getStatus())) {
            if (user.getNumber() < 5) {
                userMapper.updateNumberById(user.getNumber(), user.getId());
                String uid = loginUser.getUser().getId().toString();
                String token = JwtUtils.generateToken(uid, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                String key = LOGIN_USER+uid;
                redisCache.setCacheObject(key, loginUser);
                redisCache.setCacheObject(LOGIN_TOKEN, token);
                apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_LOGIN, token);
            } else {
                user.setStatus(Constant.INT_ONE);
                userMapper.updateStatusById(user.getStatus(), user.getId());
                apiResult = new ApiResult(HttpStatus.MOVED_PERMANENTLY.value(), "该账户已停用，限登录五次，请联系管理员", null);
            }
        } else {
            apiResult = new ApiResult(HttpStatus.MOVED_PERMANENTLY.value(), "该账户已停用，请联系管理员", null);
        }
        return apiResult;
    }

    /**
     * 获取用户名称
     */
    @Override
    public ApiResult getUsername(HttpServletRequest request) {
        String token = redisCache.getCacheObject(LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("没有该数据");
        }
        Long uid = null;
        try {
            uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析失败");
        }
        String key = LOGIN_USER + uid;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("没有该用户请从新登录");
        }
        return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_FIND_OK, loginUser.getUser().getUsername());
    }

    /**
     * 分页查询用户
     */
    @Override
    public ApiResult listPage(Page<User> page, Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("username").toString())) {
            return ApiResult.ok(Constant.STR_FIND_OK,userMapper.selectPage(page, null));
        } else {
            String username = MapUtils.getString(params, "username");
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(User::getUsername, username);
            return ApiResult.ok(Constant.STR_FIND_OK,userMapper.selectPage(page, wrapper));
        }
    }

    /**
     * 根据ID查询该用户
     */
    @Override
    public ApiResult selectById(Long id) {
        return ApiResult.ok(HttpStatus.OK.value(),Constant.STR_FIND_OK,userMapper.selectById(id));
    }

    /**
     * 根据ID删除该条数据
     */
    @Override
    public ApiResult deleteByPrimaryKey(Long id) {
        return ApiResult.ok(HttpStatus.OK.value(),Constant.STR_DEL,userMapper.deleteById(id));
    }

    /**
     * 退出
     */
    @Override
    public ApiResult exit(HttpServletRequest request) {
        ApiResult apiResult = null;
        String token = redisCache.getCacheObject(LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("没有该数据");
        }
        try {
            Long uid = null;
            try {
                uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("解析失败");
            }
            String key = LOGIN_USER + uid;
            redisCache.deleteObject(key);
            apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_OUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResult;
    }

}