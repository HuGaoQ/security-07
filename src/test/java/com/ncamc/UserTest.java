package com.ncamc;

import com.ncamc.config.JwtProperties;
import com.ncamc.entity.User;
import com.ncamc.mapper.UserMapper;
import com.ncamc.utils.CodeUtile;
import com.ncamc.utils.CollectionUtils;
import com.ncamc.utils.JwtUtils;
import io.lettuce.core.cluster.SlotHash;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:09
 */
@Slf4j
@SpringBootTest
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void test1() {
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    /**
     * MD5加密&&解密
     */
    @Test
    public void test2(){
        String salt = CodeUtile.generateSalt();
        String md5Hex = CodeUtile.md5Hex("123", salt);
        log.info("salt:"+salt);
        log.info("md5HeX："+md5Hex);
        String shaHex = CodeUtile.md5Hex("123", salt);
        log.info("shaHeX："+shaHex);
    }

    /**
     * 生成token&&解析token
     */
    @Test
    public void test3() {
        String token = JwtUtils.generateToken("1", jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        Long id = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        System.out.println(id);
    }

    @Test
    public void test4() {
        System.out.println(SlotHash.getSlot("A"));
        System.out.println(SlotHash.getSlot("B"));
        System.out.println(SlotHash.getSlot("C"));
        System.out.println(SlotHash.getSlot("hello"));
    }

    @Test
    public void test5() {
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        list1.add("4");
        list1.add("9");
        List<String> list2 = new ArrayList<>();
        list2.add("3");
        list2.add("4");
        list2.add("5");
        list2.add("6");

        Collection diff1 = CollectionUtils.diff(list1, list2);// list1中有，list2中没有的元素
        System.out.println(diff1.toString());
        Collection diff2 = CollectionUtils.diff(list2, list1);// list1中没有，list2中有的元素
        System.out.println(diff2.toString());
    }
}