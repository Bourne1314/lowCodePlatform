package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 字典类型 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:58
 */
@Data
@TableName("SYS_DICT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDictDO extends AbstractBaseDomain {

    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 说明
     */
    private String dictExplain;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 集团id
     */
    private String groupId;
    /**
     * 配置范围 1租户 2集团 3应用
     */
    private Integer scope;
    /**
     * 跟踪ID
     */
    private String traceId;
}
