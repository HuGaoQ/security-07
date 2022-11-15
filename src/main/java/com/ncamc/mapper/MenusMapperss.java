package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.Menu;

import java.util.List;

public interface MenusMapperss extends BaseMapper<Menu> {

    /**
     * 查询当前用户所持有的权限
     */
    List<String> SelectParamsByUserId(Long id);

}