package com.ncamc.service.impl;

import ch.qos.logback.core.status.StatusUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.ResponseResult;
import com.ncamc.entity.User;
import com.ncamc.mapper.UserMapper;
import com.ncamc.service.LoginService;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import com.ncamc.utils.ServletUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import java.util.Objects;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:26
 */
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

    @SneakyThrows
    @Override
    public ResponseResult login(User user_) {
        ResponseResult responseResult = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user_.getUsername(),user_.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("账号或者密码错误");
        }
        User user = loginUser.getUser();
        if (user.getStatus().equals("0")){
            if (user.getNumber() < 5){
                userMapper.updateNumberById(user.getNumber(),user.getId());
                String uid = loginUser.getUser().getId().toString();
                String token = JwtUtils.generateToken(uid, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                String key = "login:"+uid;
                redisCache.setCacheObject(key,loginUser);
                responseResult = new ResponseResult(HttpStatus.OK.value(),"登录成功",token);
            }else {
                user.setStatus("1");
                userMapper.updateStatusById(user.getStatus(),user.getId());
                responseResult = new ResponseResult(HttpStatus.MOVED_PERMANENTLY.value(),"该账户已停用，限登录五次，请联系管理员");
            }
        }else {
            responseResult = new ResponseResult(HttpStatus.MOVED_PERMANENTLY.value(),"该账户已停用，请联系管理员");
        }
        return responseResult;
    }

    @Override
    public ResponseResult getUsername(HttpServletRequest request) {
        ResponseResult responseResult = null;
        String key = ServletUtils.longUid(request);
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (Objects.isNull(loginUser)){
            throw  new RuntimeException("没有该用户请从新登录");
        }
        return responseResult.ok(loginUser.getUser().getUsername());
    }

    @Override
    public ResponseResult exit(HttpServletRequest request) {
        ResponseResult responseResult = null;
        try {
            String key = ServletUtils.longUid(request);
            redisCache.deleteObject(key);
            responseResult = new ResponseResult(HttpStatus.OK.value(),"退出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }


}