package com.ncamc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import java.util.*;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static final String LOGIN_TOKEN = "login_token:";
    public static final String LOGIN_USER = "login_user:";

    public static final String CACHE_KEY_LOGIN = "login:";

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public ApiResult register(User user) {
        ApiResult apiResult = null;
        try {
            if (!StringUtils.isEmpty(user)) {
                SimpleDateFormat createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<User> users = userMapper.FindAll();
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setCreateTime(createTime.format(new Date()));
                user.setLoginTime(loginTime.format(new Date()));
                user.setId(Long.parseLong(String.valueOf(users.size() + 1)));
                userMapper.insert(user);
                apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_ADD_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            apiResult = new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constant.STR_ADD_ERROR);
        }

        return apiResult;
    }

    /**
     * 登录
     * @param users
     * @return
     */
    @SneakyThrows
    @Override
    public ApiResult login(User users) {
        ApiResult apiResult = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
        //这个方法回去调用UserDetailsServicesImpl
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //获取authenticate中返回的用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //判断是否为空 如果为空代表账号或者密码错误
        if (Objects.isNull(loginUser)) {
            return ApiResult.ok(HttpStatus.FOUND.value(), Constant.STR_ACCOUNT_OR_PASSWORD_ERROR);
        }
        //获取具体用户信息
        User user = loginUser.getUser();
        //判断状态是否为0  0代表正常 1代表停用
        if ("0".equals(user.getStatus())) {
            //判断登录次数是否超过五次 如果超过停用账户
            if (user.getNumber() < 5) {
                //添加登录时间
                Map<String, Object> map = new HashMap<>();
                map.put("name", user.getUsername());
                map.put("status", user.getStatus());
                userMapper.saveLoginTime(map);
                //修改登录次数
                userMapper.updateNumberById(user.getNumber(), user.getId());
                //将当前用户和token添加redis缓存
                String uid = loginUser.getUser().getId().toString();
                //通过ID生成token
                String token = JwtUtils.generateToken(uid, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                String key = LOGIN_USER + uid;
                redisCache.setCacheObject(key, loginUser);
                redisCache.setCacheObject(LOGIN_TOKEN, token);
                apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_LOGIN_OK, token);
            } else {
                //设置状态字段为1
                user.setStatus(Constant.INT_ONE);
                //修改登录这状态
                userMapper.updateStatusById(user.getStatus(), user.getId());
                apiResult = new ApiResult(HttpStatus.MOVED_PERMANENTLY.value(), Constant.STR_EXCEED_LOGIN_NUMBER, null);
            }
        } else {
            apiResult = new ApiResult(HttpStatus.MOVED_PERMANENTLY.value(), Constant.STR_STOP, null);
        }
        return apiResult;
    }

    /**
     * 获取用户名称
     * @param request
     * @return
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
     * 获取用户最后登录时间
     * @param apiResult
     * @return
     */
    @Override
    public ApiResult getLoginTime(ApiResult apiResult) {
        String username = (String) apiResult.getData();
        return ApiResult.ok(Constant.STR_FIND_OK, userMapper.getLoginTime(username));
    }

    /**
     * 修改密码
     * @param params
     * @return
     */
    @Override
    public ApiResult updatePassword(Map<String, Object> params) {
        ApiResult apiResult = null;
        String original_password = MapUtils.getString(params, "original_password");
        String new_password = MapUtils.getString(params, "new_password");

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
        User user = loginUser.getUser();
        if (Objects.isNull(user)) {
            throw new RuntimeException("没有该用户请从新登录");
        }

        if (bCryptPasswordEncoder.matches(original_password, user.getPassword())) {
            if (!new_password.equals(original_password)) {
                apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_UPDATE_OK, userMapper.updatePasswordById(bCryptPasswordEncoder.encode(new_password), user.getId()));
            } else {
                apiResult = new ApiResult(HttpStatus.NOT_IMPLEMENTED.value(), Constant.STR_NEWPASSWPRD_NOT_OPASSWORD_CONSISTENT);
            }
        } else {
            apiResult = new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constant.STR_OPASSWORD_NOT_CONSISTENT);
        }
        return apiResult;
    }

    /**
     * 查询用户分页信息
     * @param page
     * @param params
     * @return
     */
    @Override
    public ApiResult listPage(Page<User> page, Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("username").toString())) {
            return ApiResult.ok(Constant.STR_FIND_OK, userMapper.selectPage(page, null));
        } else {
            String username = MapUtils.getString(params, "username");
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(User::getUsername, username);
            return ApiResult.ok(Constant.STR_FIND_OK, userMapper.selectPage(page, wrapper));
        }
    }

    /**
     * 根据ID查询该用户
     * @param id
     * @return
     */
    @Override
    public ApiResult selectById(Long id) {
        return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_FIND_OK, userMapper.selectById(id));
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public ApiResult updateUser(User user) {
        try {
            UpdateWrapper wrapper = new UpdateWrapper<>();
            wrapper.eq("id", user.getId());
            User users = userMapper.selectById(user.getId());
            if (!(user.getPassword().equals(users.getPassword()))) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            int count = userMapper.update(user, wrapper);
            if (count == 1) {
                redisCache.deleteObject(CACHE_KEY_LOGIN + user.getId());
                redisCache.setCacheObject(CACHE_KEY_LOGIN + user.getId(), user);
            }
            return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_UPDATE_OK, count);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.ok(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constant.STR_UPDATE_ERROR);
        }
    }

    /**
     * 根据ID删除该条数据
     * @param id
     * @return
     */
    @Override
    public ApiResult deleteByPrimaryKey(Long id) {
        return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_DEL_OK, userMapper.deleteById(id));
    }

    /**
     * 退出
     * @param request
     * @return
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
            apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_OUT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResult;
    }

}