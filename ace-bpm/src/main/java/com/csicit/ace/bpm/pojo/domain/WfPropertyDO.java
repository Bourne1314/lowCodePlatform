package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程属性
 *
 * @author JonnyJiang
 * @date 2019/10/23 19:38
 */
@Data
@TableName("WF_PROPERTY")
public class WfPropertyDO implements Serializable {
    /**
     * 属性名
     */
    @TableId
    private String name;
    /**
     * 属性值
     */
    private String value;
    /**
     * 描述
     */
    private String description;
}
