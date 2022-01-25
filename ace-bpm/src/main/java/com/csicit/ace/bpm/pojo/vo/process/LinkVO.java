package com.csicit.ace.bpm.pojo.vo.process;

import lombok.Data;

import java.io.Serializable;

/**
 * 办理过程-连接
 *
 * @author JonnyJiang
 * @date 2019/11/21 15:08
 */
@Data
public class LinkVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 是否与当前任务相关
     */
    private Integer relevant;
}
