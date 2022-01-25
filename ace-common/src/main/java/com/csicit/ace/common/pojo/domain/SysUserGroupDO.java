package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 系统管理-用户组 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:14:36
 * @version V1.0
 */
@Data
@TableName("SYS_USER_GROUP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserGroupDO extends AbstractBaseRecordDomain {

        /**
         * 父级主键
         */
        private String parentId;
        /**
         * 所属集团主键
         */
        private String groupId;
        /**
         * 所属组织主键
         */
        private String organizationId;
        /**
         * 名称
         */
        private String name;
        /**
         * 编码
         */
        private String code;
        /**
         * 内部码
         */
        private String innerCode;
        /**
         * 集团应用主键
         */
        private String appId;
        /**
         * 排序号
         */
        private Integer sortIndex;
        /**
         * 排序路径
         */
        private String sortPath;
        /**
         * 子用户组列表
         */
        @TableField(exist = false)
        private List<SysUserGroupDO> children;

}
