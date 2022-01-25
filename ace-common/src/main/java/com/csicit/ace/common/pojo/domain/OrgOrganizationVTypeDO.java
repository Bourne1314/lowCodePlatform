package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 组织-组织-职能版本 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:17:28
 */
@Data
@TableName("ORG_ORGANIZATION_V_TYPE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgOrganizationVTypeDO extends AbstractBaseDomain {

    /**
     * 是否法人公司组织
     */
    @TableField("IS_CORPORATION")
    private Integer corporation;

    /**
     * 是否项目组织
     */
    @TableField("IS_PROJECT")
    private Integer project;

    /**
     * 是否固定资产组织
     */
    @TableField("is_asset")
    private Integer asset;

    /**
     * 是否销售组织
     */
    @TableField("IS_SALES")
    private Integer sales;

    /**
     * 是否库存组织
     */
    @TableField("IS_STOCK")
    private Integer stock;

    /**
     * 是否采购组织
     */
    @TableField("IS_PURCHASE")
    private Integer purchase;

    /**
     * 是否质检组织
     */
    @TableField("IS_QC")
    private Integer qc;

    /**
     * 是否物流组织
     */
    @TableField("IS_TRAFFIC")
    private Integer traffic;

    /**
     * 是否维修组织
     */
    @TableField("IS_MAINTAIN")
    private Integer maintain;

    /**
     * 是否人力资源组织
     */
    @TableField("IS_HR")
    private Integer hr;

    /**
     * 是否行政组织
     */
    @TableField("IS_ADMINISTRATION")
    private Integer administration;

    /**
     * 是否工厂组织
     */
    @TableField("IS_FACTORY")
    private Integer factory;

    /**
     * 是否集团组织
     */
    @TableField("IS_GROUP")
    private Integer isGroup;

    /**
     * 是否财务组织
     */
    @TableField("IS_FINANCE")
    private Integer finance;
    /**
     * 是否部门
     */
    @TableField("IS_DEPARTMENT")
    private Integer department;
}
