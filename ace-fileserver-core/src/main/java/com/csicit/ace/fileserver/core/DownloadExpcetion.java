package com.csicit.ace.fileserver.core;

/**
 * 下载异常
 *
 * @author JonnyJiang
 * @date 2019/10/14 8:42
 */
public class DownloadExpcetion extends ServerException {
    public DownloadExpcetion(String message) {
        super(message);
    }
}
