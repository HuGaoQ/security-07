package com.ncamc.expression;

import com.alibaba.fastjson.JSON;
import com.ncamc.entity.ResponseResult;
import com.ncamc.utils.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限不足处理类
 */
@Component
public class AuthenticationEntryHanderImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "您的权限不足",null);
        String json = JSON.toJSONString(result);
        ServletUtils.renderString(response, json);
    }
}