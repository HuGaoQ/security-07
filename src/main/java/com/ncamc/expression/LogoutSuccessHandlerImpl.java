package com.ncamc.expression;

import com.alibaba.fastjson.JSON;
import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.ResponseResult;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import com.ncamc.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-07 16:53
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjIiLCJleHAiOjE2NjAwMzYwMjJ9.kMl2tps9ZlE93wovktWfcYLSLJ-65yTUKy74NXXs8TTEY6Uw5w-6oMn-8zZvR7oNHOL7LSbG7_ENN9sbztl3FwWMy2Qdm_1Gk12NeOl7r7URmQCGSBoz-KKzMpOjcY4wWzFjY9tav6c4-7UFNMKyls6QCCGobbXviSdT0UMuOvw";
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
        String key = "login:" + uid;
//        String key = ServletUtils.longUid(request);
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(loginUser)) {
            // 删除用户缓存记录
            redisCache.deleteObject(key);
        }
        ServletUtils.renderString(response, JSON.toJSONString(ResponseResult.error(HttpStatus.OK.value(), "退出成功")));
    }
}