package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.vo.NewJobTreeVO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 流程定义 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Transactional
public interface WfdFlowService extends IBaseService<WfdFlowDO> {

    /**
     * 存在判断
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 13:55
     */
    String existCheck(WfdFlowDO instance);

    /**
     * 新增流程定义
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 14:09
     */
    boolean saveWfdFlow(WfdFlowDO instance);

    /**
     * 前台Json信息传到后台,保存数据库
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/9/2 14:54
     */
    R commitJsonInfoToDB(Map<String, Object> map);

    /**
     * 签出,进入编辑状态
     *
     * @param flowId
     * @return
     * @author zuogang
     * @date 2019/9/18 15:59
     */
    boolean checkOutEditing(String flowId);

    /**
     * 签入,退出编辑状态
     *
     * @param flowId
     * @return
     * @author zuogang
     * @date 2019/9/18 15:59
     */
    boolean checkInEditing(String flowId);

    /**
     * 锁定流水
     *
     * @param id
     * @return void
     * @author JonnyJiang
     * @date 2019/10/11 16:04
     */

    void lockSeq(String id);

    /**
     * 更新
     *
     * @param id
     * @param seqNo            流水号
     * @param latestCreateTime 最新创建时间
     */
    void updateSeq(String id, Integer seqNo, LocalDateTime latestCreateTime);

    /**
     * 获取用户新建工作的左侧流程列表
     *
     * @author zuogang
     * @date 2020/1/2 17:04
     */

    List<NewJobTreeVO> listFlowTreesForInitAuth(String appId);

    /**
     * 获取用户新建工作的最近使用流程列表
     *
     * @author zuogang
     * @date 2020/1/2 17:04
     */

    R initFlowList(String appId);

    /**
     * 撤回到历史版本状态
     *
     * @param flowId         流程id
     * @param historyVersion 历史流程版本
     * @author JonnyJiang
     * @date 2020/4/27 0:12
     */

    void recall(String flowId, Integer historyVersion);
}
