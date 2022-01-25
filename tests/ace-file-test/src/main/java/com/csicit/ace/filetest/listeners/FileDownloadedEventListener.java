package com.csicit.ace.filetest.listeners;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2019/10/16 19:50
 */
@Component
public class FileDownloadedEventListener implements com.csicit.ace.file.delegate.FileDownloadedEventListener {
    @Override
    public void notify(FileConfiguration fileConfiguration, FileInfo fileInfo) {
        System.out.println("==============FileDownloadedEventListener==============");
    }
}
