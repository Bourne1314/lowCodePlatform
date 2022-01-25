package com.csicit.ace.fileserver.core;

/**
 * 服务器错误
 *
 * @author JonnyJiang
 * @date 2019/10/14 8:44
 */
public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
