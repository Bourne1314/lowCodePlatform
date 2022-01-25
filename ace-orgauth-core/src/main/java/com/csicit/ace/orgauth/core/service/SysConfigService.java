package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置项 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Transactional
public interface SysConfigService extends IBaseService<SysConfigDO> {
    /**
     * 应用启动扫描配置项
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/24 16:18
     */
    void saveScanConfig(List<SysConfigDO> configDOSet);

    /**
     * 获取配置项类型的值 集团级
     * @param name 配置项类型
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    String getValue(String name);


    /**
     * 获取配置项类型的值
     * 先查找应用级的，然后是集团级，租户级
     * @param name 配置项类型
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
     String getValueByApp(String appId, String name);


}
