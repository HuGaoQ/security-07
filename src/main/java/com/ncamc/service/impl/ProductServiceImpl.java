package com.ncamc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.entity.Pages;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;
import com.ncamc.mapper.ProductMapper;
import com.ncamc.service.ProductService;
import com.ncamc.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public ResponseResult listPage(Map<String, Object> params) {
        Page<Product> page = null;
        Integer pageNo = Integer.parseInt(params.get("pageNo").toString());
        Integer pageSize = Integer.parseInt(params.get("pageSize").toString());
        if (ObjectUtils.isEmpty(params.get("prdIns").toString())) {
            page = new Page<>(pageNo, pageSize);
            productMapper.selectPage(page, null);
            return new ResponseResult(HttpStatus.OK.value(), "查询成功", new Pages<>(page.getPages(), page.getTotal(), page.getRecords()));
        } else {
            String prdIns = params.get("prdIns").toString();
            page = new Page<>(pageNo, pageSize);
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(prdIns), Product::getPrdName, prdIns).or().like(StringUtils.isNotBlank(prdIns), Product::getInsName, prdIns);
            productMapper.selectPage(page, wrapper);
            return new ResponseResult(HttpStatus.OK.value(), "查询成功", new Pages<>(page.getPages(), page.getTotal(), page.getRecords()));
        }
    }

    @Override
    public Product selectByPrimaryKey(Integer id) {
        return productMapper.selectById(id);
    }

    @Override
    public Product findById(Long id) {
        Product product = null;
        String key = CACHE_KEY_USER + id;
        product = redisCache.getCacheObject(key);
        if (Objects.isNull(product)) {
            product = productMapper.selectById(id);
            if (Objects.isNull(product)) {
                return product;
            } else {
                redisCache.setCacheObject(key, product);
            }
        }
        return product;
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        if (id != null){
            return productMapper.deleteById(id);
        }
        return 0;
    }

}