package com.csicit.ace.bpm.pojo.vo.v7v1v81;

import lombok.Data;

/**
 * @author JonnyJiang
 * @date 2020/3/13 10:10
 */
@Data
public class ActHiTaskinst {
    private String id_;
    private String proc_def_id_;
    private String task_def_key_;
    private String proc_inst_id_;
    private String execution_id_;
    private String parent_task_id_;
    private String name_;
    private String description_;
    private String owner_;
    private String assignee_;
    private String start_time_;
    private String claim_time_;
    private String end_time_;
    private String duration_;
    private String delete_reason_;
    private String priority_;
    private String due_date_;
    private String form_key_;
    private String category_;
    private String tenant_id_;
}
