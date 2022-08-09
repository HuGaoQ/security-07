package com.ncamc.expression;

import com.alibaba.fastjson.JSON;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.ResponseResult;
import com.ncamc.service.impl.LoginServiceImpl;
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

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginServiceImpl loginService = new LoginServiceImpl();
        String key = ServletUtils.longUid(request);
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(loginUser)) {
            // 删除用户缓存记录
            redisCache.deleteObject(key);
        }
        ServletUtils.renderString(response, JSON.toJSONString(ResponseResult.error(HttpStatus.OK.value(), "退出成功")));
    }
}