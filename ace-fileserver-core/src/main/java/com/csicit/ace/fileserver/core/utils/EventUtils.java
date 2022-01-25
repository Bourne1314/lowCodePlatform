package com.csicit.ace.fileserver.core.utils;

import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.RestUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.file.delegate.EventListener;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 事件工具类
 *
 * @author JonnyJiang
 * @date 2019/10/15 17:43
 */
public class EventUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventUtils.class);

    /**
     * 通知
     *
     * @param fileConfiguration 附件配置项
     * @param eventName         事件名称
     * @param params            参数
     * @author JonnyJiang
     * @date 2019/10/15 19:20
     */

    public static void notify(FileConfigurationDO fileConfiguration, String eventName, Map<String, Object> params) throws Exception {
        switch (eventName) {
            case EventListener.EVENTNAME_FILE_DELETING:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileDeleting())) {
                    return;
                }
                break;
            case EventListener.EVENTNAME_FILE_DELETING_BY_FORM:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileDeleting())) {
                    return;
                }
                break;
            case EventListener.EVENTNAME_FILE_DOWNLOADING:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileDownloading())) {
                    return;
                }
                break;
            case EventListener.EVENTNAME_FILE_DOWNLOADED:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileDownloaded())) {
                    return;
                }
                break;
            case EventListener.EVENTNAME_FILE_UPLOADING:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileUploading())) {
                    return;
                }
                break;
            case EventListener.EVENTNAME_FILE_UPLOADED:
                if (!IntegerUtils.isTrue(fileConfiguration.getEnableEvtFileUploaded())) {
                    return;
                }
                break;
        }
        LOGGER.debug("notify event begin: " + eventName);
        if (params == null) {
            params = new HashMap<>(16);
        }
        params.put(EventListener.VARNAME_FILE_CONFIGURATION, fileConfiguration.getFileConfigurationJson());
        params.put(EventListener.VARNAME_EVENT_NAME, eventName);
        HttpClient httpClient = SpringContextUtils.getBean(HttpClient.class);
        String url = httpClient.getAppAddr(fileConfiguration.getAppId()) + "/file/notify";
//        String url = "http://192.168.16.114:3333/file/notify";
        R r = RestUtils.notify(url, params);
        if (!Objects.equals(r.get(HttpCode.NAME), HttpCode.SUCCESS)) {
            throw new Exception((String) r.get(HttpCode.MESSAGE));
        }
        LOGGER.debug("notify event end: " + eventName);
    }
}