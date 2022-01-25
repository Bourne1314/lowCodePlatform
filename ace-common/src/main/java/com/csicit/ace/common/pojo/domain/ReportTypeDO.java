package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 报表类别 实例对象类
 *
 * @author generator
 * @date 2019-08-07 08:52:49
 * @version V1.0
 */
@Data
@TableName("REPORT_TYPE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportTypeDO extends AbstractBaseDomain {

        /**
         * 类别名称
         */
        private String name;
        /**
         * 应用id
         */
        private String appId;
        /**
         * 排序
         */
        private Integer sort;
        /**
         * 类别类型 1报表2仪表盘
         */
        private Integer type;
        /**
         * 父节点id
         */
        private String parentId;

        /**
         * 子集团列表
         */
        @TableField(exist = false)
        private List<ReportTypeDO> children;
        /**
         * 跟踪ID
         */
        private String traceId;
}
