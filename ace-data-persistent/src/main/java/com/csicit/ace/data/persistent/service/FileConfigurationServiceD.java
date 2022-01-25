package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.FileConfigDetail;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Transactional
public interface FileConfigurationServiceD extends IBaseService<FileConfigurationDO> {

    /**
     * 应用升级时，附件配置更新
     *
     * @param fileConfigDetailList
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 9:38
     */
    boolean fileConfigurationUpdate(List<FileConfigDetail> fileConfigDetailList, String appId);
}
