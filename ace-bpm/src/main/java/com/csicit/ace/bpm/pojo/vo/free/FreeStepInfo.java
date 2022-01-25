package com.csicit.ace.bpm.pojo.vo.free;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/12/1 8:19
 */
@Data
public class FreeStepInfo implements Serializable {
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 流程版本
     */
    private Integer flowVersion;
    /**
     * 自由流步骤
     */
    private List<FreeStep> freeSteps;

    public List<FreeStep> getFreeSteps() {
        if (freeSteps == null) {
            freeSteps = new ArrayList<>();
        }
        return freeSteps;
    }

    public void addFreeStep(FreeStep freeStep) {
        getFreeSteps().add(freeStep);
    }
}
