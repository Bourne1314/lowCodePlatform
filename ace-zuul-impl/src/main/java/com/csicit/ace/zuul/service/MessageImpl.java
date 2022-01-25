package com.csicit.ace.zuul.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.DateUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2019/7/5 14:31
 */
@Service("message")
public class MessageImpl extends BaseImpl implements IMessage {
    @Override
    public SysMsgTemplateDO get(String code) {
        if ( StringUtils.isNotBlank(code)) {
            return clientService.getMsgTem(appName, code);
        }
        return new SysMsgTemplateDO();
    }

    @Override
    public R sendMessage(List<String> receivers,String channelName, String templateId, Map<String, Object> data ) {
        SysMessageDO sysMessageDo = new SysMessageDO(appName,channelName,templateId,data,receivers);
        return clientService.sendMsg(sysMessageDo);
}

    @Override
    public R fireSocketEvent(List<String> receivers, String eventName, Map<String, Object> data) {
        if(Objects.isNull(data)){
            data = new HashMap<>(16);
        }
        data.put("datetime", DateTimeFormatter.ofPattern(DateUtils.DATE_TIME_PATTERN).format(LocalDateTime.now()));
        data.put("eventName",eventName);
        return clientService.fireSocketEvent(new SocketEventVO(receivers,eventName,data,appName));
    }

    @Override
    public List<SysMessageDO> listUserAllMsg(String userId) {
        return clientService.getAllMsgList(userId, appName);
    }

    @Override
    public Page<SysMessageDO> listUserAllMsgInPage(String userId, int size, int current) {
        return clientService.getPageAllMsgList(userId, appName, size, current);
    }

    @Override
    public List<SysMessageDO> listUserReadMsg(String userId) {
        return clientService.getReadMsgList(userId, appName);
    }

    @Override
    public Page<SysMessageDO> listUserReadMsgInPage(String userId, int size, int current) {
        return clientService.getPageReadMsgList(userId, appName, size, current);
    }

    @Override
    public List<SysMessageDO> listUserNoReadMsg(String userId) {
        return clientService.getNoReadMsgList(userId, appName);
    }

    @Override
    public Page<SysMessageDO> listUserNoReadMsgInPage(String userId, int size, int current) {
        return clientService.getPageNoReadMsgList(userId, appName, size, current);
    }

    @Override
    public boolean updateUserNoReadMsg(String userId, String msgId) {
        return clientService.updateUserMsgRead(userId, msgId);
    }

    @Override
    public boolean updateUserAllNoReadMsg(String userId) {
        return clientService.updateUserAllNoReadMsg(userId, appName);
    }

    @Override
    public boolean deleteMsg(String msgId) {
        return clientService.deleteMsg(msgId, securityUtils.getCurrentUserId());
    }

    @Override
    public boolean deleteMsgs(List<String> msgIds) {
        return clientService.deleteMsgsForInterface(msgIds, securityUtils.getCurrentUserId());
    }

    @Override
    public boolean deleteAllMsgs(String userId) {
        return clientService.deleteAllMsg(userId, appName);
    }

    @Override
    public boolean deleteAllReadMsgs(String userId) {
        return clientService.deleteAllReadMsg(userId, appName);
    }


}
