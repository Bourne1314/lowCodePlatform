package com.csicit.ace.bpm.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 获取用户新建工作的左侧流程列表
 *
 * @author zuogang
 * @return
 * @date 2020/4/26 17:12
 */
@Data
public class NewJobTreeVO {
    /**
     * 属性：所有流程->流程类别->流程名称
     */
    private String label;

    /**
     * id
     */
    private String id;

    /**
     * 属性：parentNode->categoryNode->childNode
     */
    private String type;

    /**
     * 标识
     */
    private String code;

    /**
     * 子列表
     */
    private List<NewJobTreeVO> children;
}
