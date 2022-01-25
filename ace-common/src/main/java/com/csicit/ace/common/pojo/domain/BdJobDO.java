package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 职务称谓实例表
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 8:19
 */

@Data
@TableName("BD_JOB")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdJobDO extends AbstractBaseDomain {

    /**
     * 职务称谓名称
     */
    private String name;

    /**
     * 职务称谓排序号
     */
    private Integer sortIndex;

    /**
     * 所属集团主键
     */
    private String groupId;
}
