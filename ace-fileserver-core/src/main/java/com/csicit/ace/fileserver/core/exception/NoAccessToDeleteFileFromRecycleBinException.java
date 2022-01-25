package com.csicit.ace.fileserver.core.exception;

import com.csicit.ace.fileserver.core.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/6/29 21:07
 */
public class NoAccessToDeleteFileFromRecycleBinException extends FileSystemException {
    private final String fileInfoId;
    private final String recyclerId;
    private final String currentUserId;

    public NoAccessToDeleteFileFromRecycleBinException(String fileInfoId, String recyclerId, String currentUserId)
    {
        super(FileErrorCode.S00004, LocaleUtils.getNoAccessToDeleteFileFromRecycleBin());
        this.fileInfoId = fileInfoId;
        this.recyclerId = recyclerId;
        this.currentUserId = currentUserId;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("fileInfoId", fileInfoId);
        args.put("recyclerId", recyclerId);
        args.put("currentUserId", currentUserId);
    }
}