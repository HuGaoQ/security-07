package com.ncamc.internal;

public interface Constant {

    /**
     * header中的token
     */
    String JWT_HEADER_TOKEN = "token";

    /**
     * 当前请求的用户
     */
    String JWT_REQUEST_USER = "staffId";

    /**
     * 空字符串
     */
    String STR_EMPTY = "";

    /**
     * 账号或者密码错误
     */
    String STR_ACCOUNT_OR_PASSWORD_ERROR = "账号或者密码错误";

    /**
     * 查询成功
     */
    String STR_FIND_OK = "查询成功";

    /**
     * 入住成功
     */
    String STR_RGISTER_OK = "入住成功";

    /**
     * 删除成功
     */
    String STR_DEL = "删除成功";

    /**
     * 入住失败
     */
    String STR_RGISTER_DEL = "入住失败";

    /**
     * 登录成功
     */
    String STR_LOGIN = "登录成功";

    /**
     * 退出成功
     */
    String STR_OUT = "退出成功";

    /**
     * 数字 零
     */
    int INT_ZERO = 0;

    /**
     * 数字 壹
     */
    String INT_ONE = "1";
}
