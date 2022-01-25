package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;

/**
 * 指定办理人员未办理完成
 *
 * @author JonnyJiang
 * @date 2020/5/25 17:18
 */
public class SpecifiedUserUnfinishedException extends BpmSystemException {
    private String taskId;
    private List<SysUserDO> waitForPassUsers;

    public SpecifiedUserUnfinishedException(String taskId, List<SysUserDO> waitForPassUsers) {
        super(BpmErrorCode.S00013, LocaleUtils.getSpecifiedUserUnfinished(waitForPassUsers));
        this.taskId = taskId;
        this.waitForPassUsers = waitForPassUsers;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("taskId", taskId);
        args.put("specifiedUsers", waitForPassUsers);
    }
}