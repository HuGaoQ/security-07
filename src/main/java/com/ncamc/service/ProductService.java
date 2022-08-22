package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;

import java.util.Map;


public interface ProductService extends IService<Product> {

    /**
     * 多表分页模糊条件查询
     * @param params
     * @return
     */
    ResponseResult getProductList(Map<String, Object> params);

    /**
     * 查询分页信息
     * @param params
     * @return
     */
    ResponseResult listPage(Map<String, Object> params);

    /**
     * 根据ID查询该产品信息
     * @param id
     * @return
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * 根据ID查询该产品信息
     * @param id
     * @return
     */
    Product findById(Long id);

    /**
     * 根据ID删除该产品
     * @param id
     * @return
     */
    Integer deleteByPrimaryKey(Long id);
}