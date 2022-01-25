package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 初始化服务
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/31 14:54
 */
@Transactional
public interface InitService {
    /**
     * 初始化 应用  创建租户管理员
     * @param map
     * @return 
     * @author yansiyang
     * @date 2019/5/31 14:55
     */
    R init(Map<String, String> map);

    /**
     * 更新授权文件
     * @param str
     * @return
     * @author yansiyang
     * @date 2019/11/12 8:26
     */
    boolean updateSecretKey(String str);

    /**
     * 撤回授权文件
     * @return
     * @author yansiyang
     * @date 2019/11/12 8:26
     */
    boolean recallSecretKey();
}
