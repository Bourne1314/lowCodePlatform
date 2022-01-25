package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 部门岗位表 实例对象类
 *
 * @author generator
 * @date 2019-09-23 17:24:50
 * @version V1.0
 */

@Data
@TableName("BD_POST")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdPostDO extends AbstractBaseDomain {

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 岗位类别主键
     */
    private String typeId;

    /**
     * 岗位类别名称
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 所属部门主键
     */
    private String departmentId;

    /**
     * 所属集团主键
     */
    private String groupId;

    /**
     * 所属业务单元主键
     */
    private String organizationId;

    /**
     * 所属业务单元排序号
     */
    private Integer sortIndex;

    /**
     * 保存到其他部门
     */
    @TableField(exist = false)
    private List<String> otherDepIds;
}
