package com.csicit.ace.interfaces.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/** 
 * 消息管理
 * 消息处理都在当前应用下处理
 * @author shanwj
 * @date 2019/7/5 14:45
 */
public interface IMessage {

    /**
     * 获取流程模板
     * @param code 消息模板标识
     * @return
     * @author FourLeaves
     * @date 2020/4/27 16:05
     */
    SysMsgTemplateDO get(String code);

    /**
     * 发送消息
     * @param receivers	 消息接收人 不能为空
     * @param channelName	消息频道
     * @param templateId	平台消息模板标识
     * @param data 消息模板内容替换参数
     * @return 返回结果 R code=40000 表示发送成功,code=40002 发送失败,msg 表示失败原因 exg.{code:40002,msg:"失败原因"}
     * @author shanwj
     * @date 2020/4/21 17:37
     */
    R sendMessage(@NonNull List<String> receivers,@NonNull String channelName, @NonNull String templateId,@Nullable Map<String,Object> data);


    /**
     * 开启事件处理
     * @param receivers	 事件接收人
     * @param eventName	 事件名称
     * @param data 处理数据
     * @return 返回结果 R code=40000 表示发送成功,code=40002 发送失败,msg 表示失败原因 exg.{code:40002,msg:"失败原因"}
     * @author shanwj
     * @date 2020/4/21 17:37
     */
    R fireSocketEvent(@NonNull List<String> receivers,@NonNull String eventName,@Nullable Map<String,Object> data);

    /**
     * 查询用户所有消息
     * @param userId 用户id
     * @return 用户所有消息
     */
    List<SysMessageDO> listUserAllMsg(String userId);

    /**
     *  分页查询用户所有消息
     *
     * @param userId 用户id
     * @param size	每页数量
     * @param current 当前页
     * @return 用户所有消息
     * @author shanwj
     * @date 2020/4/16 15:15
     */
    Page<SysMessageDO> listUserAllMsgInPage(String userId, int size, int current);

    /**
     * 查询用户已读消息
     * @param userId 用户id
     * @return 用户已读消息
     */
    List<SysMessageDO> listUserReadMsg(String userId);
    /**
     *  分页查询用户已读消息
     *
     * @param userId 用户id
     * @param size	每页数量
     * @param current 当前页
     * @return 用户已读消息
     * @author shanwj
     * @date 2020/4/16 15:15
     */
    Page<SysMessageDO> listUserReadMsgInPage(String userId,int size,int current);
    /**
     * 查询用户未读消息列表
     * @param userId 用户id
     * @return 用户未读消息列表
     */
    List<SysMessageDO> listUserNoReadMsg(String userId);
    /**
     *  分页查询用户未读消息
     *
     * @param userId 用户id
     * @param size	每页数量
     * @param current 当前页
     * @return 用户未读消息
     * @author shanwj
     * @date 2020/4/16 15:15
     */
    Page<SysMessageDO> listUserNoReadMsgInPage(String userId,int size,int current);

    /**
     * 更新用户未读消息
     * @param userId 用户id
     * @param msgId 消息id
     * @return 更新结果
     */
    boolean updateUserNoReadMsg(String userId,String msgId);
    /**
     * 更新用户全部未读消息
     * @param userId 用户id
     * @return 更新结果
     */
    boolean updateUserAllNoReadMsg(String userId);
    /**
     * 删除用户某一条消息
     * @param msgId 消息id
     * @return 更新结果
     */
    boolean deleteMsg(String msgId);
    /**
     * 删除用户多条消息
     * @param msgIds 消息ids
     * @return 删除结果
     */
    boolean deleteMsgs(List<String> msgIds);
    /**
     * 删除用户所有消息
     * @param userId 用户id
     * @return 删除结果
     */
    boolean deleteAllMsgs(String userId);
    /**
     * 删除用户所有已读消息
     * @param userId 用户id
     * @return 删除结果
     */
    boolean deleteAllReadMsgs(String userId);
}
