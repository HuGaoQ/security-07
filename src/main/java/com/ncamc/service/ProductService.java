package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;


public interface ProductService extends IService<Product> {

    ResponseResult listPage(String current,String size);

    Product selectByPrimaryKey(Integer id);

    Product findUserById(Long id);

}