package com.ncamc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncamc.entity.Menu;
import com.wisdge.cloud.dto.ApiResult;

import java.util.Map;

public interface SecurityService extends IService<Menu> {

    /**
     * 查询所有权限分页信息
     * @param params
     * @return
     */
    ApiResult findAllSecurity(Map<String,Object> params);

    /**
     * 添加权限标识
     * @param params
     * @return
     */
    ApiResult add(Map<String,Object> params);

    /**
     * 查询当前所选用户部门 权限信息
     * @param id
     * @return
     */
    ApiResult findSecurityByID(Long id);

    /**
     * 赋予权限
     * @param params
     * @return
     */
    ApiResult saveSecurity(Map<String, Object> params);

    /**
     * 查询当前所选用户部门 权限信息
     * @param id
     * @return
     */
    ApiResult findSecurityNickNameByID(Long id);

    /**
     * 修改权限x
     * @param prams
     * @return
     */
    ApiResult updateSecurity(Map<String, Object> prams);

    /**
     * 删除当前部门权限
     * @param id
     * @return
     */
    ApiResult delSecurity(Long id);
}