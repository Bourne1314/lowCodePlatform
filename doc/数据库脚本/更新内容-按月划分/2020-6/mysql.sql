/**
 * 开发平台服务API定义模块复制到业务平台中，支持大屏在线定制开发
 * @param null
 * @return
 * @author zuogang
 * @date 2020/6/3 8:32
 */
CREATE TABLE `SYS_APP_INTERFACE`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `APP_ID` VARCHAR(50) COMMENT '应用ID',
  `NAME` VARCHAR(50) COMMENT '接口名',
  `IS_PAGE_GET` INT(1) COMMENT '是否分页方法,0否1是',
  `SQL_CONTENT` LONGTEXT COMMENT 'sql语句',
  `CREATE_TIME` DATETIME(6) COMMENT '创建时间',
  `DESCRIPTION` VARCHAR(200) COMMENT '描述',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_APP_INTERFACE_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `sys_group_app`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
COMMENT='应用接口信息表';

CREATE TABLE `SYS_APP_INTERFACE_INPUT`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `PARAM_KEY` VARCHAR(50) COMMENT '键',
  `PARAM_DEFAULT_VALUE` VARCHAR(100) COMMENT '缺省值',
  `PARAM_TYPE` VARCHAR(50) COMMENT '参数类型',
  `INTERFACE_ID` VARCHAR(50) COMMENT '接口主键',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_INPUT_INTERFACE_ID` FOREIGN KEY (`INTERFACE_ID`) REFERENCES `sys_app_interface`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
COMMENT='接口sql入参信息表';

CREATE TABLE `SYS_APP_INTERFACE_OUTPUT`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `PARAM_KEY` VARCHAR(50) COMMENT '键',
  `PARAM_TYPE` VARCHAR(50) COMMENT '参数类型',
  `INTERFACE_ID` VARCHAR(50) COMMENT '接口主键',
  `PARAM_LABEL` VARCHAR(100) COMMENT '字段注释',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_OUTPUT_INTERFACE_ID` FOREIGN KEY (`INTERFACE_ID`) REFERENCES `sys_app_interface`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
COMMENT='接口sql出参信息表';

/**
 * 报表添加数据源
 *
 * @author shanwj
 * @date 2020/6/3 9:31
 */
ALTER TABLE `REPORT_INFO` ADD COLUMN `DATASOURCE_ID` VARCHAR(2000) COMMENT '数据源id';

alter table `SYS_GROUP_APP` DROP foreign key FK_SYS_GROUP_APP_DS_ID;

/**
 *  为应用管理员添加接口管理菜单
 * @param null
 * @return
 * @author zuogang
 * @date 2020/6/3 14:10
 */
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('byasmd7ea4a4e54acdaff4yhmzq8370', '接口管理', 'platform', null,15, '11316279a4a9416a8e20019821e640af', null, null, null, '00030015', 0, 0, 'platForm.interfaceManage.Interface', 0);

insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('byasmd7ea4a4e54acdaff4yhmzq8370', '接口管理', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'interfaceManage/views/Interface', 1, 'fa fa-window-restore', 'byasmd7ea4a4e54acdaff4yhmzq8370', null, null, 15, null, '00030015', null, null, null, null, null, null, null, null, 0, 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('byasmd7ea4a4e54acdaff4yhmzq8370', 'byasmd7ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('byasmd7ea4a4e54acdaff4yhmzq8370', 'byasmd7ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


/**
 *  数据源添加到应用下
 * @author shanwj
 * @date 2020/6/4 14:37
 */
alter table `SYS_GROUP_DATASOURCE` DROP foreign key FK_SYS_GROUP_DATASOURCE_GID;

ALTER TABLE `SYS_GROUP_DATASOURCE` ADD COLUMN `APP_ID` VARCHAR(50) COMMENT '应用id';

ALTER TABLE `SYS_GROUP_DATASOURCE`
  ADD CONSTRAINT `SYS_GROUP_DATASOURCE_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `SYS_GROUP_APP`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE;



/**
 *  添加大屏相关表
 * @param null
 * @return
 * @author zuogang
 * @date 2020/6/4 15:26
 */
alter table `REPORT_TYPE` modify column `TYPE` INT(10) DEFAULT 1 COMMENT '类别类型 1报表2仪表盘3大屏';


CREATE TABLE `BLADE_VISUAL`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `TITLE` VARCHAR(500) COMMENT '大屏标题',
  `BACKGROUND_INFO` LONGTEXT COMMENT '大屏背景',
  `CATEGORY` VARCHAR(50) COMMENT '业务类型ID',
  `PASSWORD` VARCHAR(255) COMMENT '发布密码',
  `CREATE_USER` VARCHAR(50) COMMENT '创建人',
  `CREATE_TIME` DATETIME(6) COMMENT '创建时间',
  `UPDATE_USER` VARCHAR(50) COMMENT '修改人',
  `UPDATE_TIME` DATETIME(6) COMMENT '修改时间',
  `STATUS` INT(10) COMMENT '状态',
  `IS_DELETED` INT(10) COMMENT '是否已删除0否1是',
  `DETAIL` LONGTEXT COMMENT '配置json',
  `COMPONENT` LONGTEXT COMMENT '组件json',
  `APP_ID` VARCHAR(50) COMMENT '应用ID',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_VISUAL_CATEGORY` FOREIGN KEY (`CATEGORY`) REFERENCES `report_type`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `FK_VISUAL_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `sys_group_app`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
COMMENT='大屏信息数据表';

CREATE TABLE `jariaceplat`.`BLADE_VISUAL_MAP`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `NAME` VARCHAR(255) COMMENT '地图名称',
  `DATA` LONGTEXT COMMENT '地图数据',
  `APP_ID` VARCHAR(50) COMMENT '应用ID',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_VISUAL_MAP_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `jariaceplat`.`sys_group_app`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
COMMENT='可视化地图配置表';

/**
 * 数据源管理迁移到应用管理员下
 * @param null
 * @return
 * @author zuogang
 * @date 2020/6/8 8:21
 */
UPDATE sys_auth_role SET role_id='appadmin' WHERE auth_id in ('39e1b3ec3d6e483b86c4923ea64cfeee','39e1b3ec3d6e483b86c4923ea64cf111');
UPDATE sys_auth_role_v SET role_id='appadmin',lv_id='appadmin' WHERE auth_id in ('39e1b3ec3d6e483b86c4923ea64cfeee','39e1b3ec3d6e483b86c4923ea64cf111');

alter table SYS_GROUP_DATASOURCE drop column GROUP_ID;

ALTER TABLE `SYS_APP_INTERFACE` ADD COLUMN `DS_ID` VARCHAR(50) COMMENT '数据源ID';

ALTER TABLE `SYS_APP_INTERFACE`
  ADD CONSTRAINT `FK_APP_INTERFACE_DS_ID` FOREIGN KEY (`DS_ID`) REFERENCES `SYS_GROUP_DATASOURCE`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE;


/**
 * 修改文件切片存储路径长度
 * @author JonnyJiang
 * @date 2020/6/11 10:36
 */
ALTER TABLE `FILE_CHUNK_INFO`
CHANGE COLUMN `CHUNK_PATH` `CHUNK_PATH` VARCHAR(2000);

/**
 *
 * @param null
 * @return
 * @author zuogang
 * @date 2020/6/11 15:01
 */
ALTER TABLE `SYS_APP_INTERFACE` ADD COLUMN `AUTH_ID` VARCHAR(50) COMMENT '权限id';
ALTER TABLE `BLADE_VISUAL` ADD COLUMN `AUTH_ID` VARCHAR(50) COMMENT '权限id';

/** 
 * 接口标识
 * @param null	
 * @return 
 * @author zuogang
 * @date 2020/6/15 10:12
 */
ALTER TABLE `SYS_APP_INTERFACE` ADD COLUMN `CODE` VARCHAR(50) NOT NULL COMMENT '接口标识';

insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('7784ba3204344d37929abd4ad9a1dgth', '数据大屏', 'platform', null, 6, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060006', 0, 0, 'dataScreen.views.dataScreenList', 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('7784ba3204344d37929abd4ad9a1dgth', '数据大屏', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'dataScreen/views/dataScreenList', 1, 'iconfont icon-dashboard-fill', '7784ba3204344d37929abd4ad9a1dgth', null, null, 6, null, '00060006', null, null, null, null, null, null, null, null, 0, 0);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('7784ba3204344d37929abd4ad9a1dgth', '7784ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('7784ba3204344d37929abd4ad9a1dgth', '7784ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');


insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('4599ba3204344d37929abd4ad9a1dgth', '地图管理', 'platform', null, 7, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060007', 0, 0, 'dataScreen.views.dataMapList', 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('4599ba3204344d37929abd4ad9a1dgth', '地图管理', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'dataScreen/views/dataMapList', 1, 'iconfont icon-map-marked-alt', '4599ba3204344d37929abd4ad9a1dgth', null, null, 7, null, '00060007', null, null, null, null, null, null, null, null, 0, 0);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('4599ba3204344d37929abd4ad9a1dgth', '4599ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('4599ba3204344d37929abd4ad9a1dgth', '4599ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
