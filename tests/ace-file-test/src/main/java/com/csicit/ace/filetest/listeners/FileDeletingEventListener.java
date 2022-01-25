package com.csicit.ace.filetest.listeners;

import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import org.springframework.stereotype.Component;

/**
 * @author JonnyJiang
 * @date 2019/10/16 19:51
 */
@Component
public class FileDeletingEventListener implements com.csicit.ace.file.delegate.FileDeletingEventListener {
    @Override
    public void notify(FileConfiguration fileConfiguration, FileInfo fileInfo) {
        System.out.println("==============FileDeletingEventListener==============");
    }
}
