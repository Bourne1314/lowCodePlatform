package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 组织-公司组织 实例对象类
 *
 * @author generator
 * @date 2019-04-16 15:31:10
 * @version V1.0
 */
@Data
@TableName("ORG_CORPORATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgCorporationDO extends AbstractBaseRecordDomain {

        /**
         * 所属集团主键
         */
        private String groupId;
        /**
         * 所属组织主键
         */
        private String organizationId;
        /**
         * 上级公司主键
         */
        private String parentId;
        /**
         * 通讯地址
         */
        private String postAddress;
        /**
         * 联系电话1
         */
        private String phone1;
        /**
         * 联系电话2
         */
        private String phone2;
        /**
         * 联系电话3
         */
        private String phone3;
        /**
         * 传真1
         */
        private String fax1;
        /**
         * 传真2
         */
        private String fax2;
        /**
         * 传真3
         */
        private String fax3;
        /**
         * 联系人1
         */
        private String linkman1;
        /**
         * 联系人2
         */
        private String linkman2;
        /**
         * 联系人3
         */
        private String linkman3;
        /**
         * 电子邮件1
         */
        private String email1;
        /**
         * 电子邮件2
         */
        private String email2;
        /**
         * 电子邮件3
         */
        private String email3;
        /**
         * 名称
         */
        private String name;
        /**
         * 简称
         */
        private String shortName;
        /**
         * 排序号
         */
        private Integer sortIndex;

        /**
         * 排序路径
         */
        private String sortPath;
        /**
         * 版本主键
         */
        private String versionId;
        /**
         * 编码
         */
        private String code;

}
