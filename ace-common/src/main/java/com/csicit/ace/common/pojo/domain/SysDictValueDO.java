package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 字典数据表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:11:27
 */
@Data
@TableName("SYS_DICT_VALUE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDictValueDO extends AbstractBaseDomain {

    /**
     * 类型编号
     */
    private String typeId;
    /**
     * 字典数据值
     */
    private String dictValue;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 显示顺序
     */
    private Integer sortIndex;
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
     * 类型
     */
    private String type;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 排序路径
     */
    private String sortPath;
    /**
     * 备注
     */
    private String remark;
    /**
     * 子字典数据列表
     */
    @TableField(exist = false)
    private List<SysDictValueDO> children;
    /**
     * 跟踪ID
     */
    private String traceId;
}
