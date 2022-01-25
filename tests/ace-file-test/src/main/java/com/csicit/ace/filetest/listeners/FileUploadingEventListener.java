package com.csicit.ace.filetest.listeners;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2019/10/16 19:52
 */
@Component
public class FileUploadingEventListener implements com.csicit.ace.file.delegate.FileUploadingEventListener {
    @Override
    public void notify(FileConfiguration fileConfiguration, String formId) {
        System.out.println("==============FileUploadingEventListener==============");
    }
}
