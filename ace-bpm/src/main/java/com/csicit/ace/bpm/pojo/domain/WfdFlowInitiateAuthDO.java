package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 有权发起流程的用户 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:46:06
 */
@Data
public class WfdFlowInitiateAuthDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 所属流程
     */
    private String flowId;
}
