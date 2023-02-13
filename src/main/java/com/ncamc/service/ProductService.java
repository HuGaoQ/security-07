package com.ncamc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.Product;
import com.wisdge.cloud.dto.ApiResult;

import java.util.Map;


public interface ProductService extends IService<Product> {

    /**
     * 多表分页模糊条件查询
     * @param params
     * @return
     */
    ApiResult getProductList(Page<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 查询分页信息
     * @param params
     * @return
     */
    ApiResult listPage(Page<Product> page, Map<String, Object> params);

    /**
     * 新增产品信息
     * @return
     */
    ApiResult add(Product product);

    /**
     * 根据ID查询该产品信息
     * @param id
     * @return
     */
    ApiResult findById(Long id);

    /**
     * 修改产品信息
     * @param product
     * @return
     */
    ApiResult updateProduct(Product product);

    /**
     * 根据ID删除该产品
     * @param id
     * @return
     */
    ApiResult deleteByPrimaryKey(Long id);
}