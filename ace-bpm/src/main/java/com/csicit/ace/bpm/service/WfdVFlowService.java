package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfdVFlowDO;
import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 流程定义 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:43:23
 */
@Transactional
public interface WfdVFlowService extends IBaseService<WfdVFlowDO> {

    /**
     * 保存工作流模板
     *
     * @param json
     * @return
     * @author yansiyang
     * @date 2019/8/28 19:37
     */
    boolean saveWorkFlow(JSONObject json);

    /**
     * 更新是否最新
     *
     * @param flowId 流程id
     * @param latest 是否最新
     * @return void
     * @author JonnyJiang
     * @date 2019/9/3 9:53
     */
    void updateLatestByFlowId(String flowId, Integer latest);

    /**
     * 获取最新发布的流程定义
     *
     * @param flowId 流程id
     * @return WfdVFlowDO
     * @author JonnyJiang
     * @date 2019/9/5 11:52
     */

    WfdVFlowDO getLatestByFlowId(String flowId);

    /**
     * 获取最新发布的流程版本
     *
     * @param code 流程标识
     * @return WfdVFlowDO
     * @author JonnyJiang
     * @date 2019/9/5 11:52
     */

    WfdVFlowDO getLatestByCode(String code);

    /**
     * 获取指定日期生效的流程版本
     *
     * @param code 流程标识
     * @param date 指定日期
     * @return com.csicit.ace.bpm.pojo.domain.WfdVFlowDO
     * @author JonnyJiang
     * @date 2019/9/23 9:14
     */

    WfdVFlowDO getEffectiveByCode(String code, LocalDateTime date);

    /**
     * 获取指定日期生效的流程版本
     *
     * @param flowId 流程id
     * @param date   指定日期
     * @return com.csicit.ace.bpm.pojo.domain.WfdVFlowDO
     * @author JonnyJiang
     * @date 2019/9/23 9:14
     */

    WfdVFlowDO getEffectiveByFlowId(String flowId, LocalDateTime date);

    /**
     * 获取指定流程的最大版本的流程定义
     *
     * @param flowId 流程id
     * @return java.lang.Integer
     * @author JonnyJiang
     * @date 2019/9/17 17:31
     */

    WfdVFlowDO getMaxVersion(String flowId);

    /**
     * 更新版本失效日期
     *
     * @param flowId         流程定义id
     * @param flowVersion    流程版本
     * @param versionEndDate 版本失效日期
     * @return java.lang.Integer 影响行数
     * @author JonnyJiang
     * @date 2019/9/23 9:46
     */

    Integer updateVersionEndDate(String flowId, int flowVersion, LocalDateTime versionEndDate);

    /**
     * 更新是否被使用
     *
     * @param id   流程版本id
     * @param used 是否被使用
     * @param processDefinitionId 流程引擎中的流程定义id
     * @return java.lang.Integer 影响行数
     * @author JonnyJiang
     * @date 2019/9/23 21:17
     */

    Integer updateUsedById(String id, int used, String processDefinitionId);

    /**
     * 获取指定版本流程
     *
     * @param flowId  流程id
     * @param version 流程版本
     * @return 指定版本流程
     * @author JonnyJiang
     * @date 2020/4/27 0:28
     */

    WfdVFlowDO getByVersion(String flowId, Integer version);
}
