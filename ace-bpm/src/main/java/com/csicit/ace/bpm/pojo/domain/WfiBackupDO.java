package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程备份信息
 *
 * @author JonnyJiang
 * @date 2020/1/17 9:58
 */
@Data
@TableName("WFI_BACKUP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfiBackupDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 流程实例id
     */
    private String flowId;
    /**
     * 描述
     */
    private String description;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 备份时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime backupTime;
    /**
     * 备份数据
     */
    private String backupData;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 流程引擎版本
     */
    private String engineVersion;
}