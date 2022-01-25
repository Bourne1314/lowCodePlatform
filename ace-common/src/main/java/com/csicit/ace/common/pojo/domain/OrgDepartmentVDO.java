package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

/**
 * 组织-部门版本 实例对象类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:43
 */
@Data
@TableName("ORG_DEPARTMENT_V")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgDepartmentVDO extends AbstractBaseRecordDomain {

    /**
     * 上级部门主键
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
     * 部门名称
     */
    private String name;

    /**
     * 部门主键
     */
    private String departmentId;

    /**
     * 排序号
     */
    private Integer sortIndex;

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
     * 编码
     */
    private String code;

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
