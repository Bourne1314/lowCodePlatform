package com.csicit.ace.file.delegate;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/10/24 18:15
 */
public interface FileDeletingByFormEventListener extends EventListener {
    /**
     * 通知
     *
     * @param fileConfiguration 附件配置项
     * @param formId            表单id
     * @param fileInfos         文件列表
     * @author JonnyJiang
     * @date 2019/10/16 9:44
     */
    void notify(FileConfiguration fileConfiguration, String formId, List<FileInfo> fileInfos);
}
