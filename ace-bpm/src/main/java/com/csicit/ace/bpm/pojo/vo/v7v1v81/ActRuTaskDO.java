package com.csicit.ace.bpm.pojo.vo.v7v1v81;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author JonnyJiang
 * @date 2020/3/13 8:42
 */
@Data
public class ActRuTaskDO {
    private String id_;
    private String rev_;
    private String execution_id_;
    private String proc_inst_id_;
    private String proc_def_id_;
    private String name_;
    private String parent_task_id_;
    private String description_;
    private String task_def_key_;
    private String owner_;
    private String assignee_;
    private String delegation_;
    private String priority_;
    private String create_time_;
    private String due_date_;
    private String category_;
    private String suspension_state_;
    private String tenant_id_;
    private String form_key_;
    private String claim_time_;
}
