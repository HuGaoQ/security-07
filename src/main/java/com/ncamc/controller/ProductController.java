package com.ncamc.controller;

import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;
import com.ncamc.service.ProductService;
import com.ncamc.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCache redisCache;

    public static final String CACHE_KEY_USER = "user:";

    @ApiOperation("查询分页信息")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "prdIns", value = "产品名称", defaultValue = "", dataTypeClass = String.class, example = "机构名称")
    })
    public ResponseResult list(@RequestBody Map<String, Object> params) {
        return productService.listPage(params);
    }

    @ApiOperation("数据库新增记录")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public ResponseResult add(@RequestBody Product product) {
        try {
            boolean b = productService.save(product);
            if (b) {
                product = productService.selectByPrimaryKey(product.getId());
                redisCache.setCacheObject(CACHE_KEY_USER + product.getId(), product);
            }
            return new ResponseResult(HttpStatus.OK.value(),"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),"添加失败");
        }
    }

    @ApiOperation("查询1条记录")
    @RequestMapping(value = "/order/findUserById/{id}", method = RequestMethod.GET)
    public Product select(@PathVariable Long id) {
        return productService.findUserById(id);
    }

    @PostMapping("jieshou")
    @ApiOperation("接收数据")
    public Map<String, Object> select(@RequestBody Map<String, Object> map) {
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
        System.out.println(map.get("sex"));
        return map;
    }

}