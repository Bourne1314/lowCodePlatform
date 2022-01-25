package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @Description 附件审查类
 * @Author JR-zhangzhaojun
 * @DATE 2021/11/29
 * @Param
 * @return
 * @Version 1.0
 */
@Data
@TableName("SYS_REVIEW_FILE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysReviewFile {
    @Id
    @TableId(type = IdType.UUID)
    private String Id;

    /**
     * 申请人userId
     */
    private String applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 备注
     */
    private String remark;

    private String manageUserId;

    private String applyDepId;

    private String applyDepName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    private String flowCode;

    private String nodeName;

    private String authId;

    private String authName;

    private String roleId;

    private String roleName;

    private Integer secretLevel;
}
