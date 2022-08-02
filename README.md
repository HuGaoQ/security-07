架构: SpringBoot  2.1.5
jdk: 1.8
技术: mysql-lombok-mybatisPlus-redis-jwt-swagger2-Dockerfile

sql:
sys_user: 密码：123
    CREATE TABLE `sys_user` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `username` varchar(255) DEFAULT NULL COMMENT '用户名',
      `nick_name` varchar(255) DEFAULT NULL COMMENT '名称',
      `password` varchar(255) DEFAULT NULL COMMENT '密码',
      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `login_time` datetime DEFAULT NULL COMMENT '修改时间',
      `number` int(11) DEFAULT '0' COMMENT '登录次数',
      `status` char(1) DEFAULT '0' COMMENT '状态（0正常  1停用）',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
                                                                                                                                                                       
    INSERT INTO `demo`.`sys_user`(`id`, `username`, `nick_name`, `password`, `create_time`, `login_time`, `number`, `status`) VALUES (1, 'lisi', '李四', '$2a$10$OPbK4oMdzhNlbM6YmO/3Set9pbBUhvt/o4MtY4AezkWeFllihWEwS', '2022-06-16 10:50:49', '2022-06-16 10:50:49', 0, '0');
    INSERT INTO `demo`.`sys_user`(`id`, `username`, `nick_name`, `password`, `create_time`, `login_time`, `number`, `status`) VALUES (2, 'admin', '管理员', '$2a$10$yFddd3MT1zI2jA2rYpcV9O3brwwOkIeqs5k3hGG2/R/vmXntL76lC', '2022-06-16 10:50:49', '2022-06-16 10:50:49', 5, '1');
    INSERT INTO `demo`.`sys_user`(`id`, `username`, `nick_name`, `password`, `create_time`, `login_time`, `number`, `status`) VALUES (3, 'zhangsan', '张三', '$2a$10$Kf9azuDG9fTBNcV2/VVM/uSbImbonncDLc2mfD8/MTnnhc5AcvA.u', '2022-06-16 10:50:49', '2022-06-16 10:50:49', 1, '0');

sys_user_role:
    CREATE TABLE `sys_user_role` (
      `user_id` int(200) DEFAULT NULL,
      `role_id` int(200) DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    INSERT INTO `demo`.`sys_user_role`(`user_id`, `role_id`) VALUES (1, 2);
    INSERT INTO `demo`.`sys_user_role`(`user_id`, `role_id`) VALUES (3, 2);
    INSERT INTO `demo`.`sys_user_role`(`user_id`, `role_id`) VALUES (2, 1);

sys_role:
    CREATE TABLE `sys_role` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `name` varchar(255) DEFAULT NULL,
      `role_key` varchar(255) DEFAULT NULL COMMENT '角色权限字符串',
      `status` char(1) DEFAULT '0' COMMENT '角色状态（0正常  1停用）',
      `del_flag` int(11) DEFAULT '0',
      `create_by` bigint(255) DEFAULT NULL,
      `create_time` datetime DEFAULT NULL,
      `update_by` bigint(200) DEFAULT NULL,
      `update_time` datetime DEFAULT NULL,
      `remark` varchar(500) DEFAULT NULL COMMENT '备注',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
    
    INSERT INTO `demo`.`sys_role`(`id`, `name`, `role_key`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'CEO', 'ceo', '0', 0, NULL, NULL, NULL, NULL, NULL);
    INSERT INTO `demo`.`sys_role`(`id`, `name`, `role_key`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 'Coder', 'coder', '0', 0, NULL, NULL, NULL, NULL, NULL);


sys_role_menu:
    CREATE TABLE `sys_role_menu` (
      `role_id` bigint(200) DEFAULT NULL COMMENT '角色ID',
      `menu_id` bigint(255) DEFAULT NULL COMMENT '权限ID'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    INSERT INTO `demo`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 1);
    INSERT INTO `demo`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 2);
    INSERT INTO `demo`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (2, 2);

sys_menu:
    CREATE TABLE `sys_menu` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
      `menu_name` varchar(255) DEFAULT NULL COMMENT '菜单名',
      `path` varchar(255) DEFAULT NULL COMMENT '路由地址',
      `component` varchar(255) DEFAULT NULL COMMENT '组建路径',
      `visaible` char(1) DEFAULT '0' COMMENT '菜单状态（0正常  1.停用）',
      `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常  1.停用）',
      `perms` varchar(255) DEFAULT NULL COMMENT '权限标识',
      `icon` varchar(255) DEFAULT NULL,
      `create_by` bigint(20) DEFAULT NULL,
      `create_time` datetime DEFAULT NULL,
      `update_by` bigint(20) DEFAULT NULL,
      `update_time` datetime DEFAULT NULL,
      `del_flag` int(11) DEFAULT '0' COMMENT '是否删除（0.未删除  1.已删除）',
      `remark` varchar(255) DEFAULT NULL COMMENT '备注',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

    INSERT INTO `demo`.`sys_menu`(`id`, `menu_name`, `path`, `component`, `visaible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1, '部门管理', 'dept', 'system/dept/index', '0', '0', 'system:dept:list', '#', NULL, NULL, NULL, NULL, 0, NULL);
    INSERT INTO `demo`.`sys_menu`(`id`, `menu_name`, `path`, `component`, `visaible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (2, '测试', 'test', 'system/test/index', '0', '0', 'system:test:list', '#', NULL, NULL, NULL, NULL, 0, NULL);

sys_prd:
    CREATE TABLE `sys_prd` (
      `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `prd_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
      `prd_dm` varchar(255) DEFAULT NULL COMMENT '产品代码',
      `net` varchar(255) DEFAULT NULL COMMENT '净值',
      `fbalance` varchar(255) DEFAULT NULL COMMENT '份额',
      `favalable` varchar(255) DEFAULT NULL COMMENT '金额',
      `ins_name` varchar(255) DEFAULT NULL COMMENT '机构名称',
      `ins_dm` varchar(255) DEFAULT NULL COMMENT '机构代码',
      `new_date` varchar(255) DEFAULT NULL COMMENT '日期',
      `new_time` varchar(255) DEFAULT NULL COMMENT '当前时间',
      PRIMARY KEY (`ID`)
    ) ENGINE=InnoDB AUTO_INCREMENT=766865411 DEFAULT CHARSET=utf8;
    
    INSERT INTO `demo`.`sys_prd`(`ID`, `prd_name`, `prd_dm`, `net`, `fbalance`, `favalable`, `ins_name`, `ins_dm`, `new_date`, `new_time`) VALUES (1, '产品test1', '1', '0.8812', '123.1', '123.1', '机构test', '001', '2022-04-08', '2022-04-08 00:00:00');
    INSERT INTO `demo`.`sys_prd`(`ID`, `prd_name`, `prd_dm`, `net`, `fbalance`, `favalable`, `ins_name`, `ins_dm`, `new_date`, `new_time`) VALUES (2, '产品test2', '1', '0.8812', '123.1', '123.1', '机构test', '001', '2022-04-08', '2022-04-08 00:00:00');
    INSERT INTO `demo`.`sys_prd`(`ID`, `prd_name`, `prd_dm`, `net`, `fbalance`, `favalable`, `ins_name`, `ins_dm`, `new_date`, `new_time`) VALUES (3, '产品test3', '1', '0.8812', '123.1', '123.1', '机构test', '001', '2022-04-08', '2022-04-08 00:00:00');