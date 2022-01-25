package com.csicit.ace.fileserver.core.exception;

import com.csicit.ace.fileserver.core.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/6/26 22:24
 */
public class FileRepositoryNotFoundByIdException extends FileSystemException {
    private String id;

    public FileRepositoryNotFoundByIdException(String id) {
        super(FileErrorCode.S00003, LocaleUtils.getFileRepositoryNotFoundById(id));
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}