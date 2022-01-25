package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/17 11:08
 */
public class FormFieldNotFoundByNameException extends BpmSystemException {
    private String name;

    public FormFieldNotFoundByNameException(String name) {
        super(BpmErrorCode.S00049, LocaleUtils.getFormFieldNotFoundByName(name));
        this.name = name;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("name", name);
    }
}