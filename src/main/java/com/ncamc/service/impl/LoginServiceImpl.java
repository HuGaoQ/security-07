package com.ncamc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.Pages;
import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;
import com.ncamc.mapper.UserMapper;
import com.ncamc.service.LoginService;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseResult register(User user) {
        ResponseResult responseResult = null;
        try {
            if (!StringUtils.isEmpty(user)) {
                SimpleDateFormat createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<User> users = userMapper.selectList(null);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setCreateTime(createTime.format(new Date()));
                user.setLoginTime(loginTime.format(new Date()));
                user.setId(Long.parseLong(String.valueOf(users.size()+1)));
                userMapper.insert(user);
                responseResult = new ResponseResult(HttpStatus.OK.value(), "入住成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "入住失败", null);
        }

        return responseResult;
    }

    /**
     * 登录
     */
    @SneakyThrows
    @Override
    public ResponseResult login(User users) {
        ResponseResult responseResult = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if (Objects.isNull(loginUser)) {
            return new ResponseResult(HttpStatus.FOUND.value(), "账号或者密码错误", null);
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
                responseResult = new ResponseResult(HttpStatus.OK.value(), "登录成功", token);
            } else {
                user.setStatus("1");
                userMapper.updateStatusById(user.getStatus(), user.getId());
                responseResult = new ResponseResult(HttpStatus.MOVED_PERMANENTLY.value(), "该账户已停用，限登录五次，请联系管理员", null);
            }
        } else {
            responseResult = new ResponseResult(HttpStatus.MOVED_PERMANENTLY.value(), "该账户已停用，请联系管理员", null);
        }
        return responseResult;
    }

    /**
     * 获取用户名称
     */
    @Override
    public ResponseResult getUsername(HttpServletRequest request) {
        String token = redisCache.getCacheObject(LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("认证失败");
        }
        Long uid = null;
        try {
            uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        String key = LOGIN_USER + uid;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("没有该用户请从新登录");
        }
        return new ResponseResult(HttpStatus.OK.value(), "查询成功", loginUser.getUser().getUsername());
    }

    /**
     * 分页查询用户
     */
    @Override
    public ResponseResult listPage(Map<String, Object> params) {
        Page<User> page = null;
//        int pageNo = Integer.parseInt(params.get("pageNo").toString());
        int pageNo = 1;
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        if (ObjectUtils.isEmpty(params.get("username").toString())) {
            page = new Page<>(pageNo, pageSize);
            userMapper.selectPage(page, null);
        } else {
            String username = params.get("username").toString();
            page = new Page<>(pageNo, pageSize);
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(User::getUsername, username);
            userMapper.selectPage(page, wrapper);
        }
        return new ResponseResult(HttpStatus.OK.value(), "查询成功", new Pages<>(page.getPages(), page.getTotal(), page.getRecords()));
    }

    /**
     * 根据ID查询该用户
     */
    @Override
    public User selectById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据ID删除该条数据
     */
    @Override
    public Object deleteByPrimaryKey(Long id) {
        if (id != null) {
            return userMapper.deleteById(id);
        }
        return 0;
    }

    /**
     * 退出
     */
    @Override
    public ResponseResult exit(HttpServletRequest request) {
        ResponseResult responseResult = null;
        String token = redisCache.getCacheObject(LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("认证失败");
        }
        try {
            Long uid = null;
            try {
                uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token非法");
            }
            String key = LOGIN_USER + uid;
            redisCache.deleteObject(key);
            responseResult = new ResponseResult(HttpStatus.OK.value(), "退出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

}