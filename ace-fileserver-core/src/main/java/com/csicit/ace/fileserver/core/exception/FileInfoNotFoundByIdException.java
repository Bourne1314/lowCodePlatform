package com.csicit.ace.fileserver.core.exception;

import com.csicit.ace.fileserver.core.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/6/26 22:20
 */
public class FileInfoNotFoundByIdException extends FileSystemException {
    private String id;

    public FileInfoNotFoundByIdException(String id) {
        super(FileErrorCode.S00002, LocaleUtils.getFileNotFoundById(id));
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}