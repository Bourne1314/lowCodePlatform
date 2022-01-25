package com.csicit.ace.bpm.pojo.vo.wfi;

import lombok.Data;

import java.io.Serializable;

@Data
public class WfiNodeUserVO implements Serializable {
    /**
     * 用户标识
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 排序号
     */
    private Integer sortIndex;
}