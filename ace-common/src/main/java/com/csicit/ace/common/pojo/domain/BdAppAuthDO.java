package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 基础应用权限 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:24:44
 * @version V1.0
 */
@Data
@TableName("BD_APP_AUTH")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdAppAuthDO extends AbstractBaseRecordDomain {

        /**
         * 应用库标识
         */
        private String appLibId;
        /**
         * 权限名称
         */
        private String name;
        /**
         * 当前路径下排序
         */
        private Integer sortIndex;
        /**
         * 叶子节点0不是1是
         */
        @TableField("IS_LEAF")
        private Integer leaf;
        /**
         * 父节点id
         */
        private String parentId;
        /**
         * 全局排序
         */
        private String sortPath;

}
