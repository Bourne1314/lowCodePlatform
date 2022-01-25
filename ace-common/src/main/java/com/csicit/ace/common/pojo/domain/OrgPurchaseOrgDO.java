package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 组织-采购组织 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:17:40
 * @version V1.0
 */
@Data
@TableName("ORG_PURCHASE_ORG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPurchaseOrgDO extends AbstractBaseRecordDomain {

        /**
         * 上级采购组织主键
         */
        private String parentId;
        /**
         * 所属组织主键
         */
        private String organizationId;
        /**
         * 版本主键
         */
        private String versionId;
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
         * 编码
         */
        private String code;

}
