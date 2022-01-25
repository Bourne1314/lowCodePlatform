package com.csicit.ace.bpm.pojo.vo.preset;

import lombok.Data;

import java.io.Serializable;

/**
 * 预设经办人
 *
 * @author JonnyJiang
 * @date 2020/8/25 15:56
 */
@Data
public class PresetUser implements Serializable {
    /**
     * 排序号
     */
    private Integer sortIndex;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户身份
     */
    private Integer userType;

    /**
     * 用于回显
     */
    private String realName;
}
