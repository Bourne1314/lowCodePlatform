package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.free.FreeStepInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/9/5 17:59
 */
@Transactional
public interface WfiFlowService extends IBaseService<WfiFlowDO> {
    /**
     * 根据流程标识获取流程实例
     *
     * @param code
     * @param businessKey
     * @return void
     * @author JonnyJiang
     * @date 2019/9/27 14:02
     */

    WfiFlowDO getByCode(String code, String businessKey);

    /**
     * 删除业务表单
     *
     * @param tableName   业务表单表名
     * @param columnName  业务标识对应的列名
     * @param businessKey 业务标识
     * @return 删除行数
     * @author JonnyJiang
     * @date 2019/11/6 19:39
     */

    Integer deleteFormByBusinessKey(String tableName, String columnName, String businessKey);

    /**
     * 同步数据
     *
     * @param tableName   表名
     * @param idColName   id列名
     * @param businessKey 业务标识
     * @param columnName  列名
     * @param val         值
     * @return java.lang.Integer
     * @author JonnyJiang
     * @date 2019/11/12 19:31
     */

    Integer syncData(String tableName, String idColName, String businessKey, String columnName, Object val);

    /**
     * 获取可用工作文号
     *
     * @param flowCode  流程标识
     * @param wfiFlowId 流程实例id
     * @param flowNo    工作文号
     * @return 工作文号
     * @author JonnyJiang
     * @date 2019/12/31 10:17
     */

    String getAvailableFlowNo(String flowCode, String wfiFlowId, String flowNo);

    /**
     * 更新工作文号
     *
     * @param flowCode  流程标识
     * @param wfiFlowId 流程实例id
     * @param flowNo    工作文号
     * @author JonnyJiang
     * @date 2019/12/31 10:26
     */

    void updateFlowNo(String flowCode, String wfiFlowId, String flowNo);

    /**
     * 获取无效流程实例列表
     *
     * @return 无效流程实例列表
     * @author JonnyJiang
     * @date 2020/1/2 17:04
     */

    List<WfiFlowDO> listInvalidFromActiviti();

    /**
     * 业务是否存在
     *
     * @param tableName   数据表
     * @param businessKey 业务标识
     * @return 业务是否存在
     * @author JonnyJiang
     * @date 2020/1/2 17:08
     */

    Boolean businessExists(String tableName, String idName, String businessKey);

    /**
     * 获取表单字段值
     *
     * @param tableName   表名
     * @param columnName  列名
     * @param formIdName  业务标识列名
     * @param businessKey 业务标识
     * @return 表单字段值
     * @author JonnyJiang
     * @date 2020/1/13 16:59
     */

    Object getFormValue(String tableName, String columnName, String formIdName, String businessKey);

    /**
     * 预设后续办理路径
     *
     * @param presetInfo 预设信息
     * @param wfiFlow
     * @param node
     * @author JonnyJiang
     * @date 2020/8/25 16:02
     */

    void presetRoute(PresetInfo presetInfo, WfiFlowDO wfiFlow, Node node);

    /**
     * 更新流程实例
     *
     * @param wfiFlowNew  新流程实例
     * @param flowVersion 流程版本
     * @author JonnyJiang
     * @date 2020/11/12 10:09
     */

    void updateByNew(WfiFlowDO wfiFlowNew, Integer flowVersion);

    /**
     * 更新自由流节点步骤列表
     *
     * @param freeStepInfo 自由流设置信息
     * @author JonnyJiang
     * @date 2020/12/1 9:51
     */

    void updateNodeFreeSteps(TaskInstance taskInstance, FreeStepInfo freeStepInfo);
}