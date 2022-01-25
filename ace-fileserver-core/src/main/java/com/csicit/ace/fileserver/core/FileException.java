package com.csicit.ace.fileserver.core;

import com.csicit.ace.common.exception.RException;

/**
 * 文件异常
 * @author JonnyJiang
 * @date 2021/6/26 21:59
 */
public class FileException extends RException {
    public FileException(String message) {
        super(message);
    }
}
