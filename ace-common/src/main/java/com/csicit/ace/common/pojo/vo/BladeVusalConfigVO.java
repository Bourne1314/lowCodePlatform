package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 可视化配置表
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/17 17:31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BladeVusalConfigVO {
    /**
     * 可视化表主键
     */
    private String visualId;
    /**
     * 配置json
     */
    private String detail;
    /**
     * 组件json
     */
    private String component;
}
