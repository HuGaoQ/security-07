package com.ncamc.controller;

import com.ncamc.service.SecurityService;
import com.wisdge.cloud.dto.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api("权限security接口")
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @ApiOperation("查询所有权限分页信息")
    @PostMapping("/findAllSecurity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataTypeClass = String.class, example = "用户名")
    })
    public ApiResult findAllSecurity(@RequestBody Map<String,Object> params){
        return securityService.findAllSecurity(params);
    }

    @ApiOperation("添加权限标识")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuName", value = "权限名称", required = true, dataTypeClass = String.class, example = "权限名称"),
            @ApiImplicitParam(name = "perms", value = "权限标识", required = true, dataTypeClass = String.class, example = "权限标识")
    }
    )
    public ApiResult add(@RequestBody Map<String,Object> params){
        return securityService.add(params);
    }

    @ApiOperation("查询当前所选用户部门 权限信息")
    @GetMapping("/findSecurityByID")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "用户ID", required = true,dataTypeClass = Integer.class,example = "用户ID")
    )
    public ApiResult findSecurityByID(Long id){
        return securityService.findSecurityByID(id);
    }

    @ApiOperation("赋予权限")
    @PostMapping("/saveSecurity")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rid", value = "角色ID", required = true, dataTypeClass = List.class, example = "角色ID"),
            @ApiImplicitParam(name = "mid", value = "权限ID", required = true, dataTypeClass = List.class, example = "权限ID")
    })
    public ApiResult saveSecurity(@RequestParam Map<String,Object> prams){
        return securityService.saveSecurity(prams);
    }

    @ApiOperation("查询当前所选用户部门 权限信息")
    @GetMapping("/findSecurityByNickNameID")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "用户ID", required = true,dataTypeClass = Integer.class,example = "用户ID")
    )
    public ApiResult findSecurityNickNameByID(Long id){
        return securityService.findSecurityNickNameByID(id);
    }

    @ApiOperation("修改权限")
    @PostMapping("/updateSecurity")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rid", value = "部门ID", required = true, dataTypeClass = String.class, example = "部门ID"),
            @ApiImplicitParam(name = "menu", value = "权限", required = true, dataTypeClass = String.class, example = "权限")
    })
    public ApiResult updateSecurity(@RequestParam Map<String,Object> prams){
        return securityService.updateSecurity(prams);
    }

    @ApiOperation("删除当前部门权限")
    @GetMapping("/delSecurity")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "用户ID", required = true,dataTypeClass = Integer.class,example = "用户ID")
    )
    public ApiResult delSecurity(Long id){
        return securityService.delSecurity(id);
    }

}