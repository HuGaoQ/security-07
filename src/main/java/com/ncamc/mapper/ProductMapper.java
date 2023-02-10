package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ncamc.entity.Product;
import com.ncamc.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 多表分页模糊条件查询
     */
    String wrapperSql = "select * from sys_product p left join sys_factory f on p.factory_id = f.id ${ew.customSqlSegment}";
    String wrapperIdSql = "select * from sys_product p left join sys_factory f on p.factory_id = f.id where f.id = #{id} ${ew.customSqlSegment}";

    @Select(wrapperSql)
    Page<Map<String, Object>> page(Page<Map<String, Object>> page, @Param("ew")QueryWrapper<Product> productQueryWrapper);

    @Select(wrapperSql)
    Page<Map<String, Object>> pages(Page<Map<String, Object>> page,  @Param("ew")QueryWrapper<User> userQueryWrapper);

    @Select(wrapperIdSql)
    Page<Map<String, Object>> pageByIdAndLikeName(Page<Map<String, Object>> page, QueryWrapper<Product> productQueryWrapper,@Param("id") Integer id);
}