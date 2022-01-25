package com.csicit.ace.bpm.pojo.vo.v7v1v81;

import lombok.Data;

/**
 * @author JonnyJiang
 * @date 2020/3/13 10:09
 */
@Data
public class ActHiProcinst {
    private String id_;
    private String proc_inst_id_;
    private String business_key_;
    private String proc_def_id_;
    private String start_time_;
    private String end_time_;
    private String duration_;
    private String start_user_id_;
    private String start_act_id_;
    private String end_act_id_;
    private String super_process_instance_id_;
    private String delete_reason_;
    private String tenant_id_;
    private String name_;
}
