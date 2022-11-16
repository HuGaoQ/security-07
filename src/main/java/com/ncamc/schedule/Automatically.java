package com.ncamc.schedule;

import com.ncamc.config.ApplicationContextProvider;
import com.ncamc.entity.User;
import com.ncamc.mapper.UserMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 自动检测用户是否被锁定
 */
@Slf4j
public class Automatically implements Runnable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取Bean实例
     */
    public Automatically(){
        userMapper = ApplicationContextProvider.getBean(UserMapper.class);
        passwordEncoder = ApplicationContextProvider.getBean(PasswordEncoder.class);
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            List<User> users = userMapper.selectList(null);
            for (User user : users) {
                String status = user.getStatus();
                Integer number = user.getNumber();
                if (status.equals("0")){
                    log.info(user.getUsername()+"账户没有被锁定");
                }
                if (status.equals("1") || number == 5) {
                    log.info(user.getUsername()+"账户被锁定");
                    log.info("正在自动解锁该账户");
                    Thread.sleep(2000);
                    log.info("重置状态中");
                    Thread.sleep(2000);
                    user.setStatus("0");
                    userMapper.updateStatusById(user.getStatus(),user.getId());
                    log.info("状态重置完成");
                    Thread.sleep(2000);
                    log.info("重置登陆次数中");
                    Thread.sleep(2000);
                    user.setNumber(0);
                    userMapper.chongZhiNumberById(user.getNumber(),user.getId());
                    log.info("登录次数重置完成");
                    Thread.sleep(2000);
                    log.info("重置密码中");
                    Thread.sleep(2000);
                    user.setPassword(passwordEncoder.encode("123"));
                    userMapper.updatePasswordById(user.getPassword(),user.getId());
                    log.info("密码重置完成");
                    Thread.sleep(2000);
                    log.info(user.getUsername()+"账户重置成功，欢迎再次使用");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}