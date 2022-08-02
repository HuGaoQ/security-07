package com.ncamc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;
import com.ncamc.mapper.ProductMapper;
import com.ncamc.service.ProductService;
import com.ncamc.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-08 09:55
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    public static final String CACHE_KEY_USER = "user:";

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult listPage(String current_,String size_) {
        Integer current = Integer.parseInt(current_);
        Integer size = Integer.parseInt(size_);
        Page<Product> page = new Page<>(current,size);
        productMapper.selectPage(page,null);
        List<Object> list = Arrays.asList(page.getPages(),page.getTotal(),page.getRecords());
        return new ResponseResult(HttpStatus.OK.value(),"查询成功",list);
    }

    @Override
    public Product selectByPrimaryKey(Integer id) {
        return productMapper.selectById(id);
    }

    @Override
    public Product findUserById(Long id) {
        Product product = null;
        String key = CACHE_KEY_USER + id;
        product = redisCache.getCacheObject(key);
        if (Objects.isNull(product)){
            product = productMapper.selectById(id);
            if (Objects.isNull(product)){
                return product;
            }else {
                redisCache.setCacheObject(key,product);
            }
        }
        return product;
    }
}