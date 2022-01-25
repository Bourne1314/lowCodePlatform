package com.csicit.ace.fileserver.core.exception;


import com.csicit.ace.fileserver.core.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2021/6/26 22:01
 */
public class FileConfigurationNotFoundByIdException extends FileSystemException {
    private String id;

    public FileConfigurationNotFoundByIdException(String id) {
        super(FileErrorCode.S00001, LocaleUtils.getFileConfigurationNotFoundById());
        this.id = id;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("id", id);
    }
}