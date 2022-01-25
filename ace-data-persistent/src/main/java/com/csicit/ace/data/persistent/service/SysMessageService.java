package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/7/5 11:09
 */
@Transactional
public interface SysMessageService extends IBaseService<SysMessageDO> {

    /**
     * 发送消息
     *
     * @param sysMessageDO
     * @return
     */
    R sendMsg(SysMessageDO sysMessageDO);

    /**
     * 启用socket事件
     *
     * @param socketEvent
     * @return
     */
    R fireSocketEvent(SocketEventVO socketEvent);

    /**
     * 查询所有消息
     *
     * @param userId
     * @param appId
     * @return
     */
    List<SysMessageDO> listAllMsg(String userId, String appId);

    /**
     * 查询所有消息
     *
     * @param userId
     * @param appId
     * @param size
     * @param current
     * @return
     */
    Page<SysMessageDO> pageAllMsg(String userId, String appId, int size, int current);

    /**
     * 查询已读消息
     *
     * @param userId
     * @param appId
     * @return
     */
    List<SysMessageDO> listRead(String userId, String appId);

    /**
     * 查询已读消息
     *
     * @param userId
     * @param size
     * @param current
     * @return
     */
    Page<SysMessageDO> pageRead(String userId, String appId, int size, int current);

    /**
     * 查询未读消息
     *
     * @param userId
     * @return
     */
    List<SysMessageDO> listNoRead(String userId, String appId);


    List<SysMessageDO> listNoRead(String userId);
    /**
     * 查询未读消息
     *
     * @param userId
     * @param size
     * @param current
     * @return
     */
    Page<SysMessageDO> pageNoRead(String userId, String appId, int size, int current);

    /**
     * 更新消息阅读状态
     *
     * @param userId
     * @param msgId
     * @return
     */
    boolean updateMsgRead(String userId, String msgId);

    /**
     * 更新用户所有消息阅读状态
     *
     * @param userId
     * @return
     */
    boolean updateUserAllNoReadMsg(String userId, String appId);

    /**
     * 删除用户某一条消息
     *
     * @param msgId 消息id
     * @param userId 用户ID
     * @return 更新结果
     */
    boolean deleteMsg(String msgId, String userId);

    /**
     * 删除用户多条消息
     *
     * @param msgIds 消息ids
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteMsgs(List<String> msgIds, String userId);

    /**
     * 删除用户所有消息
     *
     * @param userId 用户id
     * @return 删除结果
     */
    boolean deleteAllMsgs(String userId, String appId);

    /**
     * 删除用户所有已读消息
     *
     * @param userId 用户id
     * @return 删除结果
     */
    boolean deleteAllReadMsgs(String userId, String appId);
}
