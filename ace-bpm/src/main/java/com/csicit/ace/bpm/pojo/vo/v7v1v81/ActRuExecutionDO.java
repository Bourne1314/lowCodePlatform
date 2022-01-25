package com.csicit.ace.bpm.pojo.vo.v7v1v81;

import lombok.Data;

/**
 * @author JonnyJiang
 * @date 2020/2/11 10:26
 */
@Data
public class ActRuExecutionDO {
    private String id_;
    private String rev_;
    private String proc_inst_id_;
    private String business_key_;
    private String parent_id_;
    private String proc_def_id_;
    private String super_exec_;
    private String root_proc_inst_id_;
    private String act_id_;
    private String is_active_;
    private String is_concurrent_;
    private String is_scope_;
    private String is_event_scope_;
    private String is_mi_root_;
    private String suspension_state_;
    private String cached_ent_state_;
    private String tenant_id_;
    private String name_;
    private String start_time_;
    private String start_user_id_;
    private String lock_time_;
    private String is_count_enabled_;
    private String evt_subscr_count_;
    private String task_count_;
    private String job_count_;
    private String timer_job_count_;
    private String susp_job_count_;
    private String deadletter_job_count_;
    private String var_count_;
    private String id_link_count_;
}