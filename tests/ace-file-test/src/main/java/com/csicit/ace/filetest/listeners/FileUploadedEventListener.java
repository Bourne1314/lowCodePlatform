package com.csicit.ace.filetest.listeners;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import com.csicit.ace.interfaces.service.IFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author JonnyJiang
 * @date 2019/10/16 19:52
 */
@Component
public class FileUploadedEventListener implements com.csicit.ace.file.delegate.FileUploadedEventListener {
    @Autowired
    private IFile iFile;

    @Override
    public void notify(FileConfiguration fileConfiguration, FileInfo fileInfo) {
        System.out.println("==============FileUploadedEventListener==============");
        try {
            InputStream inputStream = iFile.download(fileConfiguration.getConfigurationKey(), fileInfo.getId());
            int cnt = inputStream.available();
            System.out.println("->fileSize: " + cnt);
            iFile.deleteFileByFileId(fileConfiguration.getConfigurationKey(), fileInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
