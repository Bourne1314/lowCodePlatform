package com.csicit.ace.fileserver.core.service;

import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Transactional
public interface FileConfigurationService extends IBaseService<FileConfigurationDO> {
    /**
     * 获取文件配置
     *
     * @param configurationKey 附件配置标识
     * @param appId            应用标识
     * @return 获取文件配置
     * @author JonnyJiang
     * @date 2019/5/22 18:55
     */

    FileConfigurationDO loadByKey(String configurationKey, String appId);

    /**
     * 获取文件配置
     *
     * @param id 附件配置id
     * @return 获取文件配置
     * @author JonnyJiang
     * @date 2019/12/23 14:55
     */

    FileConfigurationDO loadById(String id);

    /**
     * 根据文件获取附件配置
     *
     * @param fileInfo
     * @return FileConfigurationDO
     * @author JonnyJiang
     * @date 2019/5/22 18:55
     */

    FileConfigurationDO getByFile(FileInfoDO fileInfo);

    /**
     * 保存附件配置项
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(FileConfigurationDO instance);

    /**
     * 更新附件配置项
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(FileConfigurationDO instance);

    /**
     * 删除附件配置项
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(String[] ids);
}
