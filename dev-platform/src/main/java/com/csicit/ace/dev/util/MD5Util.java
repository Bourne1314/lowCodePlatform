package com.csicit.ace.dev.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 根据传过来的密码进行加密 - 注意：加密算法必须一致(加密算法类型，加密次数，盐值  ex：MD5 10 zhengqing)
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/8/24 0024 21:06
 */
public class MD5Util {

    public static final int HASHITERATIONS = 10;

    /**
     * 加密算法
     **/
    public final static String HASH_ALGORITHM_NAME = "MD5";

    public static String createMD5Str(String password, String salt) {
        SimpleHash hash = new SimpleHash(HASH_ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), HASHITERATIONS);
        return hash.toString();
    }

}
