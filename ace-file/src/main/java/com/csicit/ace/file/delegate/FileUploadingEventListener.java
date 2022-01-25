package com.csicit.ace.file.delegate;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;

/**
 * 文件删除前事件监听
 *
 * @author JonnyJiang
 * @date 2019/10/16 9:32
 */
public interface FileUploadingEventListener extends EventListener {
    /**
     * 通知
     *
     * @param fileConfiguration 附件配置项
     * @param formId            表单id
     * @author JonnyJiang
     * @date 2019/10/16 9:46
     */

    void notify(FileConfiguration fileConfiguration, String formId);
}
