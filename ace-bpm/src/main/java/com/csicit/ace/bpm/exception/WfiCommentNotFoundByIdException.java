package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/25 19:52
 */
public class WfiCommentNotFoundByIdException extends BpmSystemException {
    private String commentId;

    public WfiCommentNotFoundByIdException(String comentId) {
        super(BpmErrorCode.S00015, LocaleUtils.getWfiCommentNotFoundById());
        this.commentId = comentId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("commentId", commentId);
    }
}