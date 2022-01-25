package com.csicit.ace.bpm.pojo.vo.preset;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 预设节点
 *
 * @author JonnyJiang
 * @date 2020/8/25 15:53
 */
@Data
public class PresetRoute implements Serializable {
    /**
     * 排序号
     */
    private Integer sortIndex;
    /**
     * 流入分支id
     */
    private String flowInLinkId;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 强制办理顺序（主要供前端显示使用）
     */
    private Integer forceSequence;
    /**
     * 预设经办人列表
     */
    private List<PresetUser> presetUsers;

    public List<PresetUser> getPresetUsers() {
        if (presetUsers == null) {
            presetUsers = new ArrayList<>();
        }
        return presetUsers;
    }
}
