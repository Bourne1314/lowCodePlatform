package com.csicit.ace.bpm.pojo.vo.wfi;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2020/11/12 9:36
 */
@Data
public class WfiFlowInfo implements Serializable {
    private WfiFlowDO flow;
    /**
     * 流程实例版本
     */
    private Integer flowVersion;
}