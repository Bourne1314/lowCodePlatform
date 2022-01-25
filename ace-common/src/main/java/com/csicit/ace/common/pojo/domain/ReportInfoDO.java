package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 报表信息 实例对象类
 *
 * @author generator
 * @date 2019-08-07 08:54:46
 * @version V1.0
 */
@Data
@TableName("REPORT_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportInfoDO extends AbstractBaseDomain {
        /**
         * 应用id
         */
        private String appId;
        /**
         * 报表类型id
         */
        private String typeId;
        /**
         * 报表名称
         */
        private String name;
        /**
         * 查看权限集合
         */
        private String auth;
        /**
         * 公开报表（0不公开1公开）开放报表，无需登录查看
         */
        private Integer isPublic;
        /**
         * 自动刷新类型（0不自动刷新，1定时刷新，2推送事假定时刷新）
         */
        private Integer refreshType;
        /**
         * 查看选项
         */
        private String showItem;
        /**
         * 自动翻页（0启用 1不启用）
         */
        private Integer isAutoFlip;
        /**
         * 报表字符串
         */
        private String mrtStr;
        /**
         * 描述
         */
        private String remarks;
        /**
         * 权限名称
         */
        @TableField(exist = false)
        private String authName;
        /**
         * 数据源id，多数据源用','分开
          */
        private String datasourceId;
        /**
         * 数据源名称
         */
        @TableField(exist = false)
        private String datasourceName;
        /**
         * 跟踪ID
         */
        private String traceId;
}
