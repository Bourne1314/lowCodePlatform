package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.utils.server.R;
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
public interface SysConfigService extends IService<SysConfigDO> {


    /**
     * 应用启动初始化配置项
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/24 16:18
     */
    void initConfig();

    /**
     * 根据集团ID获取String类型的配置项
     *
     * @param key
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:38
     */
    String getValueByApp(String appId, String key);

    /**
     * 根据应用ID获取String类型的配置项
     *
     * @param key
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:38
     */
    String getValueByGroup(String groupId, String key);

    /**
     * 获取String类型的配置项
     *
     * @param key
     * @return
     * @author yansiyang
     * @date 2019/5/16 17:38
     */
    String getValue(String key);

    /**
     * 保存配置项
     *
     * @param config
     * @return
     * @author yansiyang
     * @date 2019/5/20 17:02
     */
    R saveConfig(SysConfigDO config);

    /**
     * 修改配置项
     *
     * @param config
     * @return
     * @author yansiyang
     * @date 2019/5/20 17:03
     */
    R updateConfig(SysConfigDO config);

    /**
     * 删除配置项
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/20 17:03
     */
    R delete(List<String> ids);


    /**
     * 保存或更新租户级的配置项
     *
     * @param key
     * @param value
     * @return
     * @author yansiyang
     * @date 2019/5/31 14:39
     */
    boolean initSaveOrUpdateConfigByKey(String key, String value, String remark);

}
