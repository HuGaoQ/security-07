package com.ncamc.controller;

import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;
import com.ncamc.service.ProductService;
import com.ncamc.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-08 09:44
 */
@Slf4j
@Api(description = "产品product接口")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCache redisCache;

    public static final String CACHE_KEY_USER = "user:";

    @ApiOperation("查询分页信息")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseResult list(@RequestBody Map<String,Object> map){
        return productService.listPage(map);
    }

    @ApiOperation("数据库新增3条记录")
    @RequestMapping(value = "/order/add",method = RequestMethod.POST)
    public String add(){
        for (int i = 1; i <= 3; i++) {
            Product product = new Product(null,"产品test0"+i,"1","0.8812","123.1","123.1","机构test","001",new Date(),new Date());
            boolean b = productService.save(product);
            if (b){
                product = productService.selectByPrimaryKey(product.getId());
                redisCache.setCacheObject(CACHE_KEY_USER+product.getId(),product);
            }
        }
        return "success add";
    }

    @ApiOperation("查询1条记录")
    @RequestMapping(value = "/order/findUserById/{id}",method = RequestMethod.GET)
    public Product select(@PathVariable Long id){
        return productService.findUserById(id);
    }

    @PostMapping("jieshou")
    @ApiOperation("接收数据")
    public Map<String,Object> select(@RequestBody Map<String,Object> map){
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
        System.out.println(map.get("sex"));
        return map;
    }

}