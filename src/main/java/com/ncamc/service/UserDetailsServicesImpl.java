package com.ncamc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.User;
import com.ncamc.internal.Constant;
import com.ncamc.mapper.MenusMapperss;
import com.ncamc.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 自定义用户登录
 */
@Slf4j
@Service
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenusMapperss menusMapperss;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getDelFlag, Constant.INT_ZERO);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException(Constant.STR_ACCOUNT_OR_PASSWORD_ERROR);
        }
        List<String> list = menusMapperss.SelectParamsByUserId(user.getId());
        return new LoginUser(user, list);
    }
}