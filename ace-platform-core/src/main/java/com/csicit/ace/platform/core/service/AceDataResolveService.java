package com.csicit.ace.platform.core.service;

import com.csicit.ace.platform.core.pojo.vo.AceDataVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/8/27 8:49
 */
@Transactional
public interface AceDataResolveService {
    /**
     * 平台数据导出
     * @param params
     * @return 
     * @author FourLeaves
     * @date 2020/8/27 8:50
     */
    AceDataVO exportData(Map<String, String> params);

    /**
     * 平台数据导出
     * @param aceData
     * @return 
     * @author FourLeaves
     * @date 2020/8/27 8:50
     */
    boolean importData(AceDataVO aceData);


    /**
     * 克隆APP相关数据
     * @param appId 要克隆的app
     * @param newAppId 新的app标识
     * @return 
     * @author FourLeaves
     * @date 2020/9/25 8:37
     */
    boolean cloneApp(String appId, String newAppId);
}
