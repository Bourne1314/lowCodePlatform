package com.csicit.ace.file.controller;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.file.delegate.*;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/10/15 16:44
 */
@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    /**
     * 触发事件
     *
     * @param params 参数
     * @return 执行结果
     */
    @CrossOrigin
    @RequestMapping("/notify")
    public R notify(@RequestBody Map<String, Object> params) {
        String eventName = (String) params.get(EventListener.VARNAME_EVENT_NAME);
        LOGGER.debug("EventName: " + eventName);
        try {
            switch (eventName) {
                case EventListener.EVENTNAME_FILE_DELETING:
                    onFileDeleting(params);
                    break;
                case EventListener.EVENTNAME_FILE_DELETING_BY_FORM:
                    onFileDeletingByForm(params);
                    break;
                case EventListener.EVENTNAME_FILE_DOWNLOADING:
                    onFileDownloading(params);
                    break;
                case EventListener.EVENTNAME_FILE_DOWNLOADED:
                    onFileDownloaded(params);
                    break;
                case EventListener.EVENTNAME_FILE_UPLOADING:
                    onFileUploading(params);
                    break;
                case EventListener.EVENTNAME_FILE_UPLOADED:
                    onFileUploaded(params);
                    break;
            }
        } catch (Exception e) {
            return R.errorWithCode(HttpCode.FILE_EVENT_LISTENER_ERROR, e.getMessage());
        }
        return R.ok();
    }

    private void onFileDeleting(Map<String, Object> params) {
        Map<String, FileDeletingEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileDeletingEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), getFileInfo(params));
        });
    }

    private void onFileDeletingByForm(Map<String, Object> params) {
        Map<String, FileDeletingByFormEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileDeletingByFormEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), getFormId(params), getFileInfoList(params));
        });
    }

    private void onFileDownloading(Map<String, Object> params) {
        Map<String, FileDownloadingEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileDownloadingEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), getFileInfo(params));
        });
    }

    private void onFileDownloaded(Map<String, Object> params) {
        Map<String, FileDownloadedEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileDownloadedEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), getFileInfo(params));
        });
    }

    private void onFileUploading(Map<String, Object> params) {
        Map<String, FileUploadingEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileUploadingEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), (String) params.get(EventListener.VARNAME_FORM_ID));
        });
    }

    private void onFileUploaded(Map<String, Object> params) {
        Map<String, FileUploadedEventListener> eventListeners = SpringContextUtils.getBeansOfType(FileUploadedEventListener.class);
        eventListeners.forEach((className, eventListener) -> {
            eventListener.notify(getFileConfiguration(params), getFileInfo(params));
        });
    }

    private FileInfo getFileInfo(Map<String, Object> params) {
        return JSONObject.parseObject((String) params.get(EventListener.VARNAME_FILE_INFO), FileInfo.class);
    }

    private List<FileInfo> getFileInfoList(Map<String, Object> params) {
        return JSONObject.parseArray((String) params.get(EventListener.VARNAME_FILE_INFO_LIST), FileInfo.class);
    }

    private String getFormId(Map<String, Object> params) {
        return JSONObject.parseObject((String) params.get(EventListener.VARNAME_FORM_ID), String.class);
    }

    private FileConfiguration getFileConfiguration(Map<String, Object> params) {
        return JSONObject.parseObject((String) params.get(EventListener.VARNAME_FILE_CONFIGURATION), FileConfiguration.class);
    }
}