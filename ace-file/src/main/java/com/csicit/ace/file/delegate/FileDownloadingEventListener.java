package com.csicit.ace.file.delegate;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;

/**
 * 文件上传前事件监听
 *
 * @author JonnyJiang
 * @date 2019/10/16 9:33
 */
public interface FileDownloadingEventListener extends EventListener {
    /**
     * 通知
     *
     * @param fileConfiguration 附件配置项
     * @param fileInfo          文件信息
     * @author JonnyJiang
     * @date 2019/10/16 9:45
     */

    void notify(FileConfiguration fileConfiguration, FileInfo fileInfo);
}
