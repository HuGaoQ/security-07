package com.ncamc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncamc.config.JwtProperties;
import com.ncamc.entity.LoginUser;
import com.ncamc.entity.Menu;
import com.ncamc.entity.Role;
import com.ncamc.entity.User;
import com.ncamc.internal.Constant;
import com.ncamc.mapper.SecurityMapper;
import com.ncamc.mapper.UserMapper;
import com.ncamc.service.SecurityService;
import com.ncamc.utils.JwtUtils;
import com.ncamc.utils.RedisCache;
import com.wisdge.cloud.dto.ApiResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SecurityServiceImpl extends ServiceImpl<SecurityMapper,Menu> implements SecurityService {

    @Autowired
    private SecurityMapper securityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisCache redisCache;

    public static final String LOGIN_TOKEN = "login_token:";
    public static final String LOGIN_USER = "login_user:";

    /**
     * 查询所有权限分页信息
     * @param params
     * @return
     */
    @Override
    public ApiResult findAllSecurity(Map<String,Object> params) {
        Page<User> page = new Page<>(MapUtils.getIntValue(params, "pageNo"), MapUtils.getIntValue(params, "pageSize"));
        String username = MapUtils.getString(params, "username");
        return ApiResult.ok(HttpStatus.OK.value(), Constant.STR_FIND_OK,securityMapper.findAllSecurity(page,username));
    }

    /**
     * 添加权限标识
     * @param params
     * @return
     */
    @Override
    public ApiResult add(Map<String,Object> params) {
        ApiResult apiResult = null;
        String token = redisCache.getCacheObject(LOGIN_TOKEN);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("没有该数据");
        }
        Long uid = null;
        try {
            uid = JwtUtils.getInfoFromId(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析失败");
        }
        String key = LOGIN_USER + uid;
        LoginUser loginUser = redisCache.getCacheObject(key);
        User user = loginUser.getUser();
        if (Objects.isNull(user)) {
            throw new RuntimeException("没有该用户请从新登录");
        }
        String perms = MapUtils.getString(params, "perms");
        String path = "";
        if (perms.length() == 16){
            path = perms.substring(7, 11);
        }else {
            path = perms.substring(7, 10);
        }
        String component_ = perms.replace(":", "/");
        String component = component_.replace("list", "index");
        List<Menu> menuList = securityMapper.selectList(null);

        Menu menu = new Menu();
        menu.setId((long) (menuList.size()+1));
        menu.setMenuName(MapUtils.getString(params,"menuName"));
        menu.setPath(path);
        menu.setComponent(component);
        menu.setPerms(perms);
        menu.setIcon("#");
        menu.setCreateBy(user.getNickName());
        menu.setUpdateBy(user.getNickName());
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());

        apiResult = new ApiResult(HttpStatus.OK.value(), Constant.STR_ADD_OK,securityMapper.insert(menu));
        return apiResult;
    }

    @Override
    public ApiResult findSecurityByID(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        User user = userMapper.selectOne(queryWrapper);

        Role role = securityMapper.getOneRole(user.getNickName());
        List<Menu> menu = securityMapper.getAllMenu();

        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("role",role);
        map.put("menu",menu);

        return ApiResult.ok(HttpStatus.OK.value(),Constant.STR_FIND_OK,map);
    }

    /**
     * 赋予权限
     * @param prams
     * @return
     */
    @Override
    public ApiResult saveSecurity(Map<String, Object> prams) {
        ApiResult apiResult = null;
        //部门id
        int rid = MapUtils.getIntValue(prams, "rid");
        //权限id
        int mid = MapUtils.getIntValue(prams, "mid");
        try {
            //根据部门id查询所拥有的权限id
            String menuID = securityMapper.findMenuID(rid);
            if (!String.valueOf(mid).equals(menuID)){
                //往sys_role_menu添加关联
                securityMapper.saveRoleMenu(rid,mid);
                apiResult = new ApiResult(HttpStatus.OK.value(),Constant.STR_ADD_OK);
            }else {
                apiResult = new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),Constant.STR_NOT_AND_YY);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return apiResult;
    }

    @Override
    public ApiResult findSecurityNickNameByID(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        User user = userMapper.selectOne(queryWrapper);

        Role role = securityMapper.getOneRole(user.getNickName());
//        String menu = securityMapper.getOneMenu(role.getId());
        List<Menu> menu = securityMapper.getAllMenu();

        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("role",role);
        map.put("menu",menu);

        return ApiResult.ok(HttpStatus.OK.value(),Constant.STR_FIND_OK,map);
    }

    /**
     * 修改权限
     * @param prams
     * @return
     */
    @Override
    public ApiResult updateSecurity(Map<String, Object> prams) {
        ApiResult apiResult = null;
        int rid = MapUtils.getIntValue(prams, "rid");
        String menuID = securityMapper.findMenuID(rid);
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName,MapUtils.getString(prams,"menu"));
        Menu menu = securityMapper.selectOne(queryWrapper);
        if (!menuID.equals(menu.getId())){
            //修改sys_role_menu表关联
            apiResult = new ApiResult<>(HttpStatus.OK.value(),Constant.STR_UPDATE_OK,securityMapper.updateRoleMenu(rid,menu.getId()));
        }else {
            apiResult = new ApiResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),Constant.STR_NOT_AND_YY);
        }
        return apiResult;
    }

    /**
     * 删除当前部门权限
     * @param id
     * @return
     */
    @Override
    public ApiResult delSecurity(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }

        String roleID = securityMapper.findRoleID(user.getNickName());
        return ApiResult.ok(HttpStatus.OK.value(),Constant.STR_DEL_OK,securityMapper.deleteSecurityById(roleID));
    }
}