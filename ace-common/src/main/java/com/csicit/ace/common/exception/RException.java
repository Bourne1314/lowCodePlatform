package com.csicit.ace.common.exception;

import com.csicit.ace.common.constant.HttpCode;

/**
 * 返回值常量
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-10 10:37:46
 */
public class RException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 提示消息
     */
    private String msg;
    /**
     * 状态吗
     */
    private int code = HttpCode.INTERNAL_SERVER_ERROR;

    private String innerCode;

    public RException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public RException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }
}