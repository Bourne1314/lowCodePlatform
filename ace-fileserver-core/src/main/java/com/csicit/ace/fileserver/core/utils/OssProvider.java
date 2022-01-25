package com.csicit.ace.fileserver.core.utils;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public abstract class OssProvider {
    private String providerName;
    private String ossEndpoint;
    private String ossSecret;
    private String ossKey;
    private String buket;

    public OssProvider(String providerName, String ossEndpoint, String ossSecret, String ossKey, String buket){
        this.providerName = providerName;
        this.ossEndpoint = ossEndpoint;
        this.ossSecret= ossSecret;
        this.ossKey = ossKey;
        this.buket = buket;
    }
}
