package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

/**
 * 组织-项目组织版本 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:17:34
 * @version V1.0
 */
@Data
@TableName("ORG_PROJECT_ORG_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgProjectOrgVDO extends AbstractBaseRecordDomain {

        /**
         * 上级项目组织主键
         */
        private String parentId;
        /**
         * 所属组织主键
         */
        private String organizationId;
        /**
         * 项目组织主键
         */
        private String projectOrgId;
        /**
         * 名称
         */
        private String name;
        /**
         * 简称
         */
        private String shortName;
        /**
         * 版本编号
         */
        private String versionNo;
        /**
         * 版本名称
         */
        private String versionName;
        /**
         * 版本生效日期
         */
        private LocalDate versionBeginDate;
        /**
         * 版本失效日期
         */
        private LocalDate versionEndDate;
        /**
         * 所属集团主键
         */
        private String groupId;
        /**
         * 是否最新版本
         */
        @TableField("IS_LAST_VERSION")
        private Integer lastVersion;
        /**
         * 编码
         */
        private String code;
        /**
         * 版本启用人主键
         */
        private String versionBeginUserId;
        /**
         * 版本停用人主键
         */
        private String versionEndUserId;

}
