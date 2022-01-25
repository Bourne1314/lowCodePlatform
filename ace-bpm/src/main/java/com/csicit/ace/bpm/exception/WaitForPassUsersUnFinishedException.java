package com.csicit.ace.bpm.exception;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/29 16:58
 */
public class WaitForPassUsersUnFinishedException extends BpmSystemException {
    private String taskId;
    private List<SysUserDO> unfinishedUsers;

    public WaitForPassUsersUnFinishedException(String taskId, List<SysUserDO> unfinishedUsers) {
        super(BpmErrorCode.S00020, LocaleUtils.getWaitForPassUsersUnFinished(unfinishedUsers));
        this.taskId = taskId;
        this.unfinishedUsers = unfinishedUsers;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
        args.put("unfinishedUsers", JSONObject.toJSON(unfinishedUsers));
    }
}
