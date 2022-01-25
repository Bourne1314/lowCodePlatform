package com.csicit.ace.common.start;

import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/25 10:35
 */
@Service
public class AceAppStartToDoDemo implements IAceAppStartToDo {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * redis访问工具类对象
     */
    @Autowired
    CacheUtil cacheUtil;

    @Override
    public void run() {
        if (!Objects.equals(appName, "platform")) {
            try {
                GMBaseUtil.initSmKeyValue(cacheUtil);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
