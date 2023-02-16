package com.ncamc.internal;

import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import com.wisdge.cloud.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class TokenService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取LoginUser
     * @return
     */
    public LoginUser getLoginUser() {
        String key = getRedisKey();
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("没有该用户请从新登录");
        }
        return loginUser;
    }

    /**
     * 删除缓存中的key
     * @return
     */
    public ApiResult apiResult() {
        String key = getRedisKey();
        redisCache.deleteObject(key);
        return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_OUT_OK);
    }

    /**
     * 获取缓存中存储用户的key
     * @return
     */
    public String getRedisKey() {
        String token = redisCache.getCacheObject(Constant.LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("没有该数据");
        }
        String key = null;
        Long uid = null;
        try {
            uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析失败");
        }
        key = Constant.LOGIN_USER + uid;
        return key;
    }

}