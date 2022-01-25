package com.csicit.ace.fileserver.core.exception;

/**
 * 文件错误代码
 * @author JonnyJiang
 * @date 2021/6/26 22:08
 */
public enum FileErrorCode implements ErrorCode {
    S00001("FILE-S00001"), S00002("FILE-S00002"), S00003("FILE-S00003"), S00004("FILE-S00004");

    private String errorCode;

    FileErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}