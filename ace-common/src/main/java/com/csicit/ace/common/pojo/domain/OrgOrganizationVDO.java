package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

/**
 * 组织-组织版本 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:17:28
 */
@Data
@TableName("ORG_ORGANIZATION_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgOrganizationVDO extends AbstractBaseDomain {

    /**
     * 上级组织主键
     */
    private String parentId;

    /**
     * 所属集团主键
     */
    private String groupId;

    /**
     * 所属公司主键
     */
    private String corporationId;

    /**
     * 组织主键
     */
    private String organizationId;

    /**
     * 备注
     */
    private String remark;

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
     * 地址
     */
    private String address;

    /**
     * 国家地区
     */
    private String countryZone;

    /**
     * 组织机构码
     */
    private String organizationCode;

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
     * 是否业务单元
     */
    @TableField("IS_BUSINESS_UNIT")
    private Integer businessUnit;

    /**
     * 编码
     */
    private String code;

    /**
     * 是否最新版本
     */
    @TableField("IS_LAST_VERSION")
    private Integer lastVersion;

    /**
     * 版本生效人ID
     */
    private String versionBeginUserId;

    /**
     * 版本失效人ID
     */
    private String versionEndUserId;

}
