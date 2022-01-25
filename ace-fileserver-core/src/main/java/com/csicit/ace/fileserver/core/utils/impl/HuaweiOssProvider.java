package com.csicit.ace.fileserver.core.utils.impl;

import com.csicit.ace.fileserver.core.utils.OssProvider;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public class HuaweiOssProvider extends OssProvider {
    public HuaweiOssProvider(String providerName, String ossEndpoint, String ossSecret, String ossKey, String buket) {
        super(providerName, ossEndpoint, ossSecret, ossKey, buket);
    }
}
