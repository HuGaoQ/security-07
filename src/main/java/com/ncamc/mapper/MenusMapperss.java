package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.Menu;

import java.util.List;

public interface MenusMapperss extends BaseMapper<Menu> {

    List<String> SelectParamsByUserId(Long id);

}