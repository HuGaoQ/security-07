package com.ncamc.internal;

import java.util.Arrays;
import java.util.List;

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
     * 配置不过滤的路径，支持模糊查询swagger
     */
    String NOT_FILTER_STR = "/swagger|/v2|/v3";

    /**
     * 空字符串
     */
    String STR_EMPTY = "";
    /**
     * 数字 零
     */
    int INT_ZERO = 0;
    /**
     * 一亿
     */
    int INT_BILLION = 100000000;
    /**
     * 阶段 年度
     */
    int STAGE_YEAR = 1;
    /**
     * 阶段 季度
     */
    int STAGE_QUARTER = 2;
    /**
     * 阶段 月度
     */
    int STAGE_MONTH = 3;
    /**
     * 交易类型 申购
     */
    int STATEMENT_TYPE_PURCHASING  = 1;
    /**
     * 交易类型 赎回
     */
    int STATEMENT_TYPE_REDEMING = 2;
    /**
     * 操作类型 更新
     */
    int OPERATOR_TYPE_UPDATE = 1;
    /**
     * 操作类型 删除
     */
    int OPERATOR_TYPE_DEL = 2;
    /**
     * 交易市场 上海
     */
    String MARKET_CODE_SS = "SS";

    /**
     * 客户信息表
     */
    String TB_CUSTOMER = "xh_customer";
    /**
     * 客户信息表 列 客户名称
     */
    String TB_CUSTOMER_COLUMN_NAME = "customerName";
    /**
     * 客户信息表 列 客户全称
     */
    String TB_CUSTOMER_COLUMN_FULL_NAME = "customerFullname";
    /**
     * 客户信息表主键
     */
    List<String> TB_CUSTOMER_PK = Arrays.asList("customer_code");
    /**
     * 法人信息表
     */
    String TB_CORPORATION = "xh_corporation";
    /**
     * 法人信息表主键
     */
    List<String> TB_CORPORATION_PK = Arrays.asList("corporation_code");

    /**
     * 联系人表
     */
    String TB_ATTENTION = "xh_attention";
    /**
     * 联系人表主键
     */
    List<String> TB_ATTENTION_PK = Arrays.asList("account_code");
    /**
     * 客户经理所属关系表
     */
    String TB_CUSTOMER_MANAGER = "xh_customer_manager";
    /**
     * 客户经理所属关系表主键
     */
    List<String> TB_CUSTOMER_MANAGER_PK = Arrays.asList("customer_code","manager_code");
    /**
     * 联系人客户所属关系表
     */
    String TB_CUSTOMER_ATTENTION = "xh_customer_attention";
    /**
     * 联系人客户所属关系表主键
     */
    List<String> TB_CUSTOMER_ATTENTION_PK = Arrays.asList("customer_code","attn_code");
    /**
     * 交易沟通记录表
     */
    String TB_TRADE_COMMUNICATION = "xh_trade_communicate";
    /**
     * 交易沟通记录表主键
     */
    List<String> TB_TRADE_COMMUNICATION_PK = Arrays.asList("id");
    /**
     *按钮选中标志
     */
    String SELECT_OPTION_FLAG = "1";

    //region 字典常量
    /**
     * 机构类型
     */
    String ORG_CHAR_CODE = "orgChar_002";
    /**
     * 个人类型
     */
    String ORG_CHAR_SELF_CODE = "orgChar_001";
    /**
     * 认购code
     */
    String STMT_SUB_CODE = "stmt_130";
    /**
     * 申购code
     */
    String STMT_PUR_CODE = "stmt_122";
    /**
     * 赎回code
     */
    String STMT_RED_CODE = "stmt_124";
    /**
     * 强制赎回code
     */
    String STMT_OBLIGE_RED_CODE = "stmt_142";
    /**
     * 机构类型 code
     */
    String ORG_TYPE_CODE = "orgChar_001";
    /**
     * 客户经理A角色code
     */
    String MGR_MAIN_ROLE_TYPE = "mgrRole_001";

    /**
     * 交易沟通-新意向
     */
    String TRADE_FLAG_NEW_INTENTION = "tradeType_001";
    /**
     * 交易沟通-意向确认
     */
    String TRADE_FLAG_INTENTION_CONFIRM = "tradeType_002";
    /**
     * 交易沟通-资金到账
     */
    String TRADE_FLAG_FUND_ARRIVE = "tradeType_003";
    /**
     * 交易沟通-交易确认
     */
    String TRADE_FLAG_COMPLETE = "tradeType_004";
    /**
     * 交易沟通-意向取消
     */
    String TRADE_FLAG_CANCEL = "tradeType_005";
    /**
     * 方向-认购
     */
    String DIRECTION_SUBSCRIBE_CODE = "020";
    /**
     * 方向-申购
     */
    String DIRECTION_APPLY_FOR_CODE = "022";
    /**
     * 方向-赎回
     */
    String DIRECTION_REDEMPTION_CODE = "024";

    /**
     * A角色
     */
    String ROLE_TYPE_DIC_A = "roleType_001";

    /**
     * B角色
     */
    String ROLE_TYPE_DIC_B = "roleType_002";

    /**
     * C角色
     */
    String ROLE_TYPE_DIC_C = "roleType_003";

    /**
     * 开户信息状态：未确认
     */
    String CONFIRM_STATUS_NOT_CONFIRM = "confirmStatus_001";

    /**
     * 开户信息状态：已确认
     */
    String CONFIRM_STATUS_CONFIRMED = "confirmStatus_002";

    /**
     * 开户信息状态：已废弃
     */
    String CONFIRM_STATUS_DEL = "confirmStatus_003";

    /**
     * 状态：是
     */
    String STATUS_YES = "status_001";

    /**
     * 状态：否
     */
    String STATUS_NO = "status_002";

    /**
     * 邮件发送配置类型：申购
     */
    String SEND_TYPE_SUBSCRIBE = "singType_001";

    /**
     * 邮件发送配置类型：赎回
     */
    String SEND_TYPE_REDEMPTION = "singType_002";

    /**
     * 邮件发送配置类型：对账
     */
    String SEND_TYPE_RECONCILIATION = "singType_003";

    /**
     * 发送类型：开放账户
     */
    String SEND_TYPE_OPEN_ACCOUNT = "singType_004";

    /**
     * emailStatus_001	未发送
     * emailStatus_002	已发送
     * emailStatus_003	已删除
     */

    /**
     * 电子签章邮件发送状态：未发送
     */
    String EMAIL_SEND_STATUS_NOT_SEND = "emailStatus_001";

    /**
     * 电子签章邮件发送状态：已发送
     */
    String EMAIL_SEND_STATUS_SEND = "emailStatus_002";

    /**
     * 电子签章邮件发送状态：已删除
     */
    String EMAIL_SEND_STATUS_DELETE = "emailStatus_003";

    //endregion
}
