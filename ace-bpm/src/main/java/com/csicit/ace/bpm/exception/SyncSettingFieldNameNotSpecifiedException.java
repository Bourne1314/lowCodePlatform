package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.wfd.SyncSetting;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/17 16:45
 */
public class SyncSettingFieldNameNotSpecifiedException extends BpmSystemException {
    private SyncSetting syncSetting;

    public SyncSettingFieldNameNotSpecifiedException(SyncSetting syncSetting) {
        super(BpmErrorCode.S00051, LocaleUtils.getSyncSettingFieldNameNotSpecified());
        this.syncSetting = syncSetting;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("variantName", syncSetting.getVariantName());
        args.put("fieldName", syncSetting.getFieldName());
    }
}
