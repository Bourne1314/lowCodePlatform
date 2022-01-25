package com.csicit.ace.testapp.delegate;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2021/5/18 11:00
 */
@Component
public class FileDeletingEventListener implements com.csicit.ace.file.delegate.FileDeletingEventListener {
    @Override
    public void notify(FileConfiguration fileConfiguration, FileInfo fileInfo) {
        System.out.println("-------------deleting-------------");
        System.out.println("fileId: " + fileInfo.getId());
        System.out.println("fileName: " + fileInfo.getFileName());
    }
}
