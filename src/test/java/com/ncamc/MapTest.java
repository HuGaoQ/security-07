package com.ncamc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-08-18 15:31
 */
@Slf4j
@SpringBootTest
public class MapTest {

    @Test
    public void test(){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> maps = new HashMap<>();

        map.put("h","胡");
        map.put("g","高");
        map.put("q","强");

        maps.put("hgq",map);

        Map<String,Object> map1 = (Map<String, Object>) maps.get("hgq");

        System.out.println(maps.get("hgq"));
        System.out.println(map1.get("h"));
        System.out.println(map1.get("g"));
        System.out.println(map1.get("q"));
    }

}