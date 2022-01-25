package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * 组织-采购组织版本 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:17:45
 * @version V1.0
 */
@Data
@TableName("ORG_PURCHASE_ORG_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPurchaseOrgVDO extends AbstractBaseRecordDomain {

        /**
         * 上级采购组织主键
         */
        private String parentId;
        /**
         * 所属组织主键
         */
        private String organizationId;
        /**
         * 采购组织主键
         */
        private String purchaseOrgId;
        /**
         * 名称
         */
        private String name;
        /**
         * 简称
         */
        private String shortName;
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
         * 版本启用人主键
         */
        private String versionBeginUserId;
        /**
         * 版本停用人主键
         */
        private String versionEndUserId;

}
