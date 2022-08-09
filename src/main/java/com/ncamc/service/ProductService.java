package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;

import java.util.Map;


public interface ProductService extends IService<Product> {

    ResponseResult listPage(Map<String, Object> params);

    Product selectByPrimaryKey(Integer id);

    Product findUserById(Long id);

}