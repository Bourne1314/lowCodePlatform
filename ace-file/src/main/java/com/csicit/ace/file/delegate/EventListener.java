package com.csicit.ace.file.delegate;

import java.io.Serializable;

/**
 * 文件事件监听
 *
 * @author JonnyJiang
 * @date 2019/10/15 16:53
 */
public interface EventListener extends Serializable {
    String EVENTNAME_FILE_UPLOADED = "FILE_UPLOADED";
    String EVENTNAME_FILE_DOWNLOADING = "FILE_DOWNLOADING";
    String EVENTNAME_FILE_DOWNLOADED = "FILE_DOWNLOADED";
    String EVENTNAME_FILE_DELETING = "FILE_DELETING";
    String EVENTNAME_FILE_DELETING_BY_FORM = "FILE_DELETING_BY_FORM";
    String EVENTNAME_FILE_UPLOADING = "FILE_UPLOADING";

    String VARNAME_FILE_CONFIGURATION = "fileConfiguration";
    String VARNAME_FILE_INFO = "fileInfo";
    String VARNAME_FILE_INFO_LIST = "fileInfos";
    String VARNAME_FORM_ID = "formId";
    String VARNAME_EVENT_NAME = "eventName";
}