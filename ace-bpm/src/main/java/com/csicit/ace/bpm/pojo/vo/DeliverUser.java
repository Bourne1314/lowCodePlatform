package com.csicit.ace.bpm.pojo.vo;

import com.csicit.ace.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 分支经办人
 *
 * @author JonnyJiang
 * @date 2019/9/24 16:39
 */
@Data
public class DeliverUser implements Serializable {
    /**
     * 序号
     */
    private Integer sortIndex;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 身份类型（0:主办人,1:经办人）
     */
    private Integer userType;
    /**
     * 接收时间(当主办模式为谁先接收谁主办时，在接收后赋值)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claimTime;
    /**
     * 委托图
     */
    private Map<String, String> delegateMap;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 组织id
     */
    private String organizationId;
    /**
     * 集团id
     */
    private String groupId;
    /**
     * 是否加签
     */
    private Integer invite;

    public Map<String, String> getDelegateMap() {
        if (delegateMap == null) {
            delegateMap = new HashMap<>(16);
        }
        return delegateMap;
    }

    public String getDepartmentId() {
        return StringUtils.isEmpty(departmentId) ? null : departmentId;
    }

    public String getOrganizationId() {
        return StringUtils.isEmpty(organizationId) ? null : organizationId;
    }

    public String getGroupId() {
        return StringUtils.isEmpty(groupId) ? null : groupId;
    }
}
