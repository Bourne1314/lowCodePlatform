package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/5/12 8:19
 */
public abstract class BpmSystemException extends BpmException {
    private ErrorCode errorCode;
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmSystemException.class);
    private static final SecurityUtils securityUtils = SpringContextUtils.getBean(SecurityUtils.class);
    private Boolean logging = true;

    public BpmSystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        setInnerCode(errorCode.getErrorCode());
    }

    protected void onLogging(Map<String, Object> args) {

    }

    @Override
    public String getMessage() {
        if (logging) {
            log();
            logging = false;
        }
        return super.getMessage();
    }

    private void log() {
        Map<String, Object> args = new HashMap<>(16);
        args.put("ErrorCode", errorCode);
        args.put("UserName", securityUtils.getCurrentUserName());
        onLogging(args);
        LOGGER.error(args.toString());
    }
}