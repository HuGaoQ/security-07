package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.Product;

import java.util.Map;

public interface ProductMapper extends BaseMapper<Product> {

    void findName(Map<String, Object> params);

}