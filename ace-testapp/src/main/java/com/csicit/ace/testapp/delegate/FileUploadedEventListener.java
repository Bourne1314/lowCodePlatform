package com.csicit.ace.testapp.delegate;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2021/5/18 10:59
 */
@Component
public class FileUploadedEventListener implements com.csicit.ace.file.delegate.FileUploadedEventListener {
    @Override
    public void notify(FileConfiguration fileConfiguration, FileInfo fileInfo) {
        System.out.println("-------------uploaded-------------");
        System.out.println("fileId: " + fileInfo.getId());
        System.out.println("fileName: " + fileInfo.getFileName());
    }
}
