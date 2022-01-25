package com.csicit.ace.bpm.activiti.impl;

import com.csicit.ace.bpm.TaskUser;
import org.activiti.engine.task.IdentityLink;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/11/5 8:41
 */
public class TaskUserImpl implements TaskUser {
    private IdentityLink identityLink;

    public TaskUserImpl(IdentityLink identityLink) {
        this.identityLink = identityLink;
    }

    public static List<TaskUser> castToTaskUsers(List<IdentityLink> identityLinks) {
        List<TaskUser> taskUsers = new ArrayList<>();
        for (IdentityLink identityLink : identityLinks) {
            taskUsers.add(new TaskUserImpl(identityLink));
        }
        return taskUsers;
    }

    @Override
    public String getUserType() {
        return identityLink.getType();
    }

    @Override
    public String getUserId() {
        return identityLink.getUserId();
    }

    @Override
    public String getTaskInstanceId() {
        return identityLink.getTaskId();
    }

    @Override
    public String getFlowInstanceId() {
        return identityLink.getProcessInstanceId();
    }
}
