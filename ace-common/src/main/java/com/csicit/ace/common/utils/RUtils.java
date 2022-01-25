package com.csicit.ace.common.utils;

import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.utils.server.R;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author JonnyJiang
 * @date 2019/6/4 16:27
 */
public class RUtils {
    /**
     * 返回结果是否成功
     * @param r 服务器返回结果
     * @return java.lang.Boolean
     * @author JonnyJiang
     * @date 2019/6/4 16:28
     */

    public static Boolean isSucceed(R r) {
        return ObjectUtils.compare(HttpCode.SUCCESS, (Integer) r.get("code")) == 0;
    }
}
