package com.csicit.ace.common.exception;

import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 自定义异常
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-10 10:37:46
 */
@RestControllerAdvice
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class RExceptionHandler {

    @Autowired
    AceLogger logger;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RException.class)
    public R handleRException(RException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMsg());
        r.put("exception", e.getMessage());
        r.put("innerCode", e.getInnerCode());
        logger.error(e.getMessage(), e);
        return r;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.errorWithException(404, InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return R.errorWithException(InternationUtils.getInternationalMsg("EXIST_SAME_DATA"), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error(e.getMessage(), e);
        return R.errorWithException(InternationUtils.getInternationalMsg("EXIST_SAME_DATA"), e.getMessage());
    }

//    @ExceptionHandler(AuthorizationException.class)
//    public R handleAuthorizationException(AuthorizationException e) {
//        logger.error(e.getMessage(), e);
//        return R.errorWithException("没有权限，请联系管理员授权", e.getMessage());
//    }

    @ExceptionHandler(NullPointerException.class)
    public R handleNullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return R.errorWithException(InternationUtils.getInternationalMsg("EMPTY_ARG"), e.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public void handleFileException(FileUploadException e) {
        logger.error(e.getMessage(), e);
        throw e;
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.errorWithException(e.getMessage());
    }
}
