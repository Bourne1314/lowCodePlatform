package com.csicit.ace.quartz.core.service;

import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.quartz.core.vo.JobDetailVO;
import com.csicit.ace.quartz.core.utils.JobDetailType;
import org.quartz.SchedulerMetaData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 批处理任务配置 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@Transactional
public interface QrtzConfigService extends IBaseService<QrtzConfigDO> {
    /**
     * 获取JobDetail信息
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/6 8:45
     */
    List<JobDetailType> getJobDetailType(QrtzConfigDO instance);

    /**
     * 获取任务组group下的所有任务列表
     *
     * @param group
     * @param appId
     * @return
     * @author zuogang
     * @date 2019/8/6 8:45
     */
    List<JobDetailType> allJobByGroup(String group, String appId);

    /**
     * 编辑运行监控的任务列表
     *
     * @return
     * @author zuogang
     * @date 2019/8/6 8:45
     */
    List<JobDetailVO> getOperateDataList();

    /**
     * 获取Scheduler容器状态
     *
     * @return
     * @author zuogang
     * @date 2019/8/6 8:45
     */
    SchedulerMetaData getSchedulerState();

    /**
     * 编辑运行监控的任务列表
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/8/6 8:45
     */
    List<JobDetailVO> getTaskConfigDataList(String id);

    /**
     * 暂停任务
     *
     * @param group
     * @param name
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean pausedJob(String group, String name);

    /**
     * 运行任务
     *
     * @param group
     * @param name
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean resumeJob(String group, String name);

    /**
     * 新增任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean addJob(Map<String, Object> params);

    /**
     * 更新任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean updJob(Map<String, Object> params);

    /**
     * 删除任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean delJob(Map<String, Object> params);

    /**
     * 根据group和那么获取当前jonDetail信息
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    JobDetailType getJobDetail(Map<String, Object> params);

    /**
     * 判断该任务下是否存在同名触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean existTrigger(Map<String, Object> params);

    /**
     * 判断任务组下是否已经存在同名任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean existJob(Map<String, Object> params);

    /**
     * 新增触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean addTrigger(Map<String, Object> params);

    /**
     * 更新触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean updTrigger(Map<String, Object> params);

    /**
     * 删除触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean delTrigger(Map<String, Object> params);

    /**
     * 判断该任务下是否存在同名参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean existParam(Map<String, Object> params);

    /**
     * 新增参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean addParam(Map<String, Object> params);

    /**
     * 更新参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean updParam(Map<String, Object> params);

    /**
     * 删除参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean delParam(Map<String, Object> params);

    /**
     * 运行所有服务任务
     *
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean clickAllRun();

    /**
     * 暂停所有服务任务
     *
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    boolean clickAllPause();
}
