package com.csicit.ace.common.pojo.vo;

import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 初始化扫描注解数据存储对象
 *
 * @author shanwj
 * @date 2020/4/13 19:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitStorageDataVO {

    /**
     * 当前应用名称
     */
    private String appName;

    /**
     * api资源列表
     */
    private List<SysApiResourceDO> apis;
    /**
     * 自定义拓展消息信使列表
     */
    private List<SysMsgTypeExtendDO> msgExtends;
    /**
     * 计划任务列表
     */
    private List<ScheduledVO> scheduleds;
    /**
     * 配置项
     */
    private List<SysConfigDO> sysConfigs;

    public InitStorageDataVO(){

    }

    public InitStorageDataVO(List<SysApiResourceDO> apis, List<SysMsgTypeExtendDO> msgExtends,
                             List<ScheduledVO> scheduleds, List<SysConfigDO> sysConfigs){
        this.apis = apis;
        this.msgExtends = msgExtends;
        this.scheduleds = scheduleds;
        this.sysConfigs = sysConfigs;
    }
}
