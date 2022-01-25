package com.csicit.ace.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 17:23
 */
public class ExceptionUtils {



    /**
     * 获取异常的堆栈信息
     * @param throwable
     * @return
     * @author yansiyang
     * @date 2019/11/28 17:22
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
