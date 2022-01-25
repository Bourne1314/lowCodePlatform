package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


/**
 * 组织-组织 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:17:28
 */
@Data
@TableName("ORG_ORGANIZATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgOrganizationDO extends AbstractBaseRecordDomain {
    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETE")
    private Integer beDeleted;
    /**
     * 上级组织主键
     */
    private String parentId;

    /**
     * 上级组织名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 所属集团主键
     */
    private String groupId;

    /**
     * 所属公司主键
     */
    private String corporationId;

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
     * 地址
     */
    private String address;

    /**
     * 国家地区
     */
    private String countryZone;

    /**
     * 排序号
     */
    private Integer sortIndex;

    /**
     * 排序路径
     */
    private String sortPath;

    /**
     * 组织机构码
     */
    private String organizationCode;

    /**
     * 是否业务单元 0集团 1业务单元 2部门
     */
    @TableField("IS_BUSINESS_UNIT")
    private Integer businessUnit;

    /**
     * 编码
     */
    private String code;

    /**
     * 业务单元类型 String
     */
    @TableField(exist = false)
    private List<String> orgType;

    /**
     * 业务单元类型  Object
     */
    @TableField(exist = false)
    private JSONArray orgTypeObj;
    /**
     * 子业务单元列表
     */
    @TableField(exist = false)
    private List<OrgOrganizationDO> children;

}
