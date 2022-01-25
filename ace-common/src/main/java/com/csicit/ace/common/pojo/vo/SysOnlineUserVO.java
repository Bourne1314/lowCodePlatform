package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 在线用户信息
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/12 11:05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysOnlineUserVO implements Comparable<SysOnlineUserVO> {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户主键
     */
    private String userId;


    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 集团主键
     */
    private String groupId;


    /**
     * 用户登录IP
     */
    private String loginIP;


    /**
     * 用户登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;


    @Override
    public int compareTo(SysOnlineUserVO o) {
        if (o != null && o.getLoginTime() != null && this.loginTime != null) {
            return this.loginTime.isBefore(o.getLoginTime()) ? 1 : -1;
        }
        return 0;
    }
}
