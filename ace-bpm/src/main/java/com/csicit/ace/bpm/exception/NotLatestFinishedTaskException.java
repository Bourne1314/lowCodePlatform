package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

/**
 * @author JonnyJiang
 * @date 2020/6/12 8:43
 */
public class NotLatestFinishedTaskException extends BpmSystemException {

    private final String currentTaskId;
    private final String compareToTaskId;

    public NotLatestFinishedTaskException(String currentTaskId, String compareToTaskId) {
        super(BpmErrorCode.S00029, LocaleUtils.getNotLatestFinishedTask());
        this.currentTaskId = currentTaskId;
        this.compareToTaskId = compareToTaskId;
    }
}