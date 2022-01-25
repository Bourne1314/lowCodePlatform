package com.csicit.ace.bpm.pojo.vo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author JR-zhangzhaojun
 * @DATE 2021/9/18
 * @Param
 * @return
 * @Version 1.0
 */
@Data
@TableName("URGE_TASK_MESSAGE")
public class UrgeTaskMessageVO implements Serializable {
    /**
     * 催办消息id
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 催办节点id
     */
    private String nodeId;
    /**
     * 最大催办次数
     */
    private Integer  overTimeRemindTime;
    /**
     * 当前催办次数
     */
    private Integer currentTime;
    /**
     * 催办开始时间
     */
    private Date startTime;
    /**
     * 催办间隔时间（小时数）
     */
    private Integer intervalTime;
    /**
     * 催办结束时间
     */
    private Date endTime;
    /**
     * 催办用户Id
     */
    private String urgeUserIds;
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 流程编号
     */
    private String flowId;
}
