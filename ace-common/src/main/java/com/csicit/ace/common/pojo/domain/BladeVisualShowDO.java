package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * 可视化地图配置表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */
@Data
@TableName("BLADE_VISUAL_SHOW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BladeVisualShowDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 大屏ID
     */
    private String visualId;
    /**
     * 父节点
     */
    private String parentId;
    /**
     * json对象
     */
    private String content;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 类型(0为看版,1为项)
     */
    private Integer type;
    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<BladeVisualShowDO> childs;
    /**
     * 显示类型
     */
    private String exhibitionType;
    /**
     * 跟踪ID
     */
    private String traceId;
}
