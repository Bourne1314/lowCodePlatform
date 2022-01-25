package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.SysConfigDetail;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:35
 */
@Transactional
public interface SysConfigServiceD extends IService<SysConfigDO> {

    /**
     * 应用启动扫描配置项
     *
     * @param appName 应用名
     * @return
     * @author FourLeaves
     * @date 2019/12/24 16:18
     */
    void scanConfig(String appName);


    /**
     * 加载配置项
     *
     * @return
     * @author FourLeaves
     * @date 2020/4/17 11:41
     */
    void initConfig();

    /**
     * 保存扫描出来的配置项
     *
     * @param configs
     * @return
     * @author FourLeaves
     * @date 2020/4/17 14:06
     */
    void saveScanConfig(List<SysConfigDO> configs);

    /**
     * 获取配置项类型的值
     * 先查找应用级的，然后是集团级，租户级
     *
     * @param appId 应用标识
     * @param name  配置名称
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2020/7/14 10:02
     */

    String getValue(String appId, String name);

    /**
     * 应用升级时，系统配置更新
     *
     * @param sysConfigDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 15:54
     */
    boolean sysConfigUpdate(List<SysConfigDetail> sysConfigDetails, String appId);
}
