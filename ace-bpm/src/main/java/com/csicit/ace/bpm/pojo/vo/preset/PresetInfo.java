package com.csicit.ace.bpm.pojo.vo.preset;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预设信息
 *
 * @author JonnyJiang
 * @date 2020/8/25 15:52
 */
@Data
public class PresetInfo implements Serializable {
    /**
     * 流程实例ID
     */
    private String flowId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 预设的流转路径列表
     */
    private List<PresetRoute> presetRoutes;

    /**
     * 流程实例版本
     */
    private Integer flowVersion;

    /**
     * 是否是已预设的（用于告知前端）
     */
    private Integer preseted;

    public List<PresetRoute> getPresetRoutes() {
        if (presetRoutes == null) {
            presetRoutes = new ArrayList<>();
        }
        return presetRoutes;
    }
}
