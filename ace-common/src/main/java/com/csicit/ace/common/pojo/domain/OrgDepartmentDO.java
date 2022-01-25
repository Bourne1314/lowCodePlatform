package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 组织-部门 实例对象类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */
@Data
@TableName("ORG_DEPARTMENT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgDepartmentDO extends AbstractBaseRecordDomain {
    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETE")
    private Integer beDeleted;
    /**
     * 部门主键
     */
    private String id;
    /**
     * 上级部门主键
     */
    private String parentId;
    /**
     * 上级部门名称
     */
    @TableField(exist = false)
    private String parentName;


    /**
     * 所属集团主键
     */
    private String groupId;

    /**
     * 版本主键
     */
    private String versionId;

    /**
     * 所属组织主键
     */
    private String organizationId;

    /**
     * 部门名称
     */
    private String name;
    
    /**
     * 编码
     */
    private String code;
    /**
     * 排序号
     */
    private Integer sortIndex;

    /**
     * 排序路径
     */
    private String sortPath;
    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<OrgDepartmentDO> children;

    /**
     * 是否实体部门 0 否 1 是
     */
    @TableField(value = "IS_ENTITY")
    private Integer entity;

}
