package com.csicit.ace.common.exception;

/**
 * 文件服务异常
 *
 * @author JonnyJiang
 * @date 2019/6/6 8:05
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException(Exception e) {
        super(e);
    }
}