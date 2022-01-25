package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 集团应用库 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:12:24
 * @version V1.0
 */
@Data
@TableName("SYS_GROUP_APP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysGroupAppDO extends AbstractBaseRecordDomain{

        /**
         * 应用名称
         */
        private String name;
        /**
         * 前后端分离 0 否 1是
         */
        private Integer hasUi;

        /**
         * 是否在线 1 是 0 否
         */
        @TableField(exist = false)
        private Integer isOnline;

        /**
         * 前端服务名称
         */
        private String uiName;
        /**
         * 版本
         */
        private String version;
        /**
         * 应用图标
         */
        private String icon;
        /**
         * 排序
         */
        private Integer sortIndex;
        /**
         * 密级5非密4内部3秘密2机密1绝密
         */
        private Integer secretLevel;
        /**
         * 集团id(为空时为平台应用)
         */
        private String groupId;
        /**
         * 数据源id
         */
        private String datasourceId;
        /**
         * 数据源名称
         */
        @TableField(exist = false)
        private String datasourceName;
        /**
         * 是否启用三员0不启用1启用
         */
        @TableField("IS_3_ADMIN")
        private Integer thirdAdmin;

        /**
         * 是否集团主要APP 0不是 1是
         */
        @TableField("IS_MAIN_APP")
        private Integer mainApp;
        /**
         * 是否被锁住 0不是 1是
         */
        @TableField("IS_LOCK")
        private Integer islock;

        /**
         * 应用锁 操作时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lockTime;
        /**
         * 是否用到工作流  0否  1是
         */
        @TableField(exist = false)
        private Integer hasBpm;
}
