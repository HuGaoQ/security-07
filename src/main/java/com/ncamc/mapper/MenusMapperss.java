package com.ncamc.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncamc.entity.Menu;

import java.util.List;

public interface MenusMapperss extends BaseMapper<Menu> {

    List<String> SelectPermsByUserId(Long id);

}