package com.ncamc.internal;

public interface Constant {

    /**
     * header中的token
     */
    String JWT_HEADER_TOKEN = "token";

    /**
     * 登录存储的token
     */
    String LOGIN_TOKEN = "login_token:";

    /**
     * 登录存储的用户
     */
    String LOGIN_USER = "login_user:";

    /**
     * 前缀login：
     */
    String CACHE_KEY_LOGIN = "login:";

    /**
     * 前缀user:
     */
    String CACHE_KEY_USER = "user:";

    /**
     * 空字符串
     */
    String STR_EMPTY = "";

    /**
     * 原密码不一致
     */
    String STR_OPASSWORD_NOT_CONSISTENT = "原密码不一致";

    /**
     * 新密码不能和原密码一致
     */
    String STR_NEWPASSWPRD_NOT_OPASSWORD_CONSISTENT = "新密码不能和原密码一致";

    /**
     * 超过登录次数
     */
    String STR_EXCEED_LOGIN_NUMBER = "该账户已停用，限登录五次，请联系管理员";

    /**
     * 账户停用
     */
    String STR_STOP = "该账户已停用，请联系管理员";

    /**
     * 查询成功
     */
    String STR_FIND_OK = "查询成功";

    /**
     * 添加成功
     */
    String STR_ADD_OK = "添加成功";

    /**
     * 修改成功
     */
    String STR_UPDATE_OK = "修改成功";

    /**
     * 删除成功
     */
    String STR_DEL_OK = "删除成功";

    /**
     * 登录成功
     */
    String STR_LOGIN_OK = "登录成功";

    /**
     * 退出成功
     */
    String STR_OUT_OK = "退出成功";

    /**
     * 修改失败
     */
    String STR_UPDATE_ERROR = "修改失败";

    /**
     * 添加失败
     */
    String STR_ADD_ERROR = "添加失败";

    /**
     * 账号或者密码错误
     */
    String STR_ACCOUNT_OR_PASSWORD_ERROR = "账号或者密码错误";

    /**
     * 添加部门成功
     */
    String STR_ADD_ROLE_OK = "添加部门成功该部门已经存在权限";

    /**
     * 添加权限成功
     */
    String STR_ADD_MENU_OK = "添加权限成功该用户已经存在部门";

    /**
     * 该部门已经存在该权限，请重新修改权限
     */
    String STR_NOT_AND_YY = "该部门已经存在相同权限，请重新修改权限";

    /**
     * 数字 零
     */
    int INT_ZERO = 0;

    /**
     * 数字 壹
     */
    String INT_ONE = "1";
}
