package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * 任务评论删除异常
 * 任务评论不能被删除
 *
 * @author JonnyJiang
 * @date 2020/6/2 17:23
 */
public class TaskCommentDeleteException extends BpmSystemException {
    private String id;

    public TaskCommentDeleteException(String id) {
        super(BpmErrorCode.S00023, LocaleUtils.getTaskCommentCannotBeDeleted());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}
