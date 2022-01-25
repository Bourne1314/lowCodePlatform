package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractSecretRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


/**
 * 组织-集团 实例对象类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/19 16:39
 */
@Data
@TableName("ORG_GROUP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgGroupDO extends AbstractSecretRecordDomain {

    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETE")
    private Integer beDeleted;
    /**
     * 上级集团主键
     */
    private String parentId;

    /**
     * 上级部门名称
     */
    @TableField(exist = false)
    private String parentName;


    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer sortIndex;

    /**
     * 排序
     */
    private String sortPath;

    /**
     * 是否启用三员0不启用1启用
     */
    @TableField(value = "IS3_ADMIN")
    private Integer threeAdmin;

    /**
     * 子集团列表
     */
    @TableField(exist = false)
    private List<OrgGroupDO> children;


}
