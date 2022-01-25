package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 10:16
 */
public class FormSaveOperationNotFoundException extends BpmSystemException {
    private final List<String> formOperations;
    private final String formSaveOperate;

    public FormSaveOperationNotFoundException(List<String> formOperations, String formSaveOperate) {
        super(BpmErrorCode.S00052, LocaleUtils.getFormSaveOperationNotFound(formSaveOperate));
        this.formOperations = formOperations;
        this.formSaveOperate = formSaveOperate;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("formOperations", formOperations);
        args.put("formSaveOperate", formSaveOperate);
    }
}
