package com.ncamc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ncamc.entity.Menu;
import com.ncamc.entity.Role;
import com.ncamc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SecurityMapper extends BaseMapper<Menu> {

    /**
     * 查询所有权限分页信息
     * @param page
     * @param username
     * @return
     */
    IPage<Map<String,Object>> findAllSecurity(@Param("page") Page<User> page, @Param("username") String username);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> getAllRole();

    /**
     * 查询所有权限
     * @return
     */
    List<Menu> getAllMenu();

    /**
     * 往sys_user_role添加关联
     * @param uid
     * @param rid
     */
    int saveUserRole(@Param("uid") Long uid, @Param("rid") int rid);

    /**
     * 往sys_role_menu添加关联
     * @param rid
     * @param mid
     */
    int saveRoleMenu(@Param("rid") int rid, @Param("mid") int mid);

    /**
     * 根据用户名查询所在部门
     * @param nickName
     * @return
     */
    String findRoleID(@Param("nickName") String nickName);

    /**
     * 根据部门id查询所拥有的权限id
     * @param roleID
     * @return
     */
    String findMenuID(@Param("roleID") Integer roleID);

    /**
     * 根据用户名查询所在部门
     * @param nickName
     * @return
     */
    Role getOneRole(@Param("nickName") String nickName);

    /**
     *
     * @param rid
     * @return
     */
    String  getOneMenu(@Param("rid") Long rid);

    /**
     * 修改sys_role_menu表关联
     * @param mid
     * @return
     */
    int updateRoleMenu(@Param("rid") Integer rid, @Param("mid") Long mid);

    /**
     * 删除当前部门权限
     * @param roleID
     * @return
     */
    int deleteSecurityById(@Param("roleID") String roleID);
}