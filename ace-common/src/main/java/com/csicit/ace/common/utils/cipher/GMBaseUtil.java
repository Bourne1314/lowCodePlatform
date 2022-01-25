package com.csicit.ace.common.utils.cipher;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.cache.SerializeUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.time.LocalDateTime;

/**
 * 国密算法工具类基类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-03-29 10:37:46
 */
public class GMBaseUtil {

    private static final Logger log = LoggerFactory.getLogger(GMBaseUtil.class);

    /**
     * 编码格式
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * SM4密钥
     */
    public static String SM4KEY = "";

    /**
     * SM2私钥
     */
    public static String SM2KEY_PRIVATE_KEY;

    /**
     * SM2共钥
     */
    public static String SM2KEY_PUBLIC_KEY;

    /**
     * 序列化密钥对象
     */
    public static String SERIALIZE_KEY_PAIR;

    /**
     * SM4分组长度（字节数）
     */
    public static final int SM4_GROUP_BYTES = 16;

    /**
     * SM4根密钥字符串
     */
    private static final String INIT_SM4_KEY_STR = "AGr-sV0grqrHhBDYFwvOLw==";
    /**
     * 初始化SM4密钥字符串
     */
    public static byte[] INIT_SM4_KEY = Base64Utils.decodeFromUrlSafeString(INIT_SM4_KEY_STR);


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 通过 userid userName生成Token
     *
     * @param userId   用户id
     * @param userName 用户名
     * @return token
     * @author yansiyang
     * @date 2019/4/11 20:30
     */
    public static String getToken(String userId, String userName)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("loginTime", LocalDateTime.now());
        String str = json.toJSONString();
        byte[] data = str.getBytes(GMBaseUtil.DEFAULT_ENCODING);
        byte[] encryptData = SM4Util.encryptEcbPadding(key, data);
        return Base64Utils.encodeToUrlSafeString(encryptData);
    }

    /**
     * 解析Token
     *
     * @param token 用户token
     * @return com.alibaba.fastjson.JSONObject
     * @author yansiyang
     * @date 2019/4/11 20:31
     */
    public static JSONObject decryptToken(String token)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
        byte[] data = Base64Utils.decodeFromUrlSafeString(token);
        byte[] decryptData = SM4Util.decryptEcbPadding(key, data);
        String jsonStr = new String(decryptData, GMBaseUtil.DEFAULT_ENCODING);
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * 获取用户存入数据库密码
     * 用户密码先进行混淆再加密
     *
     * @param username 用户名
     * @param password 密码
     * @return 存入数据库密码
     * @author shanwj
     * @date 2019/4/11 20:25
     */
    public static String pwToCipherPassword(String username, String password) throws UnsupportedEncodingException {
        String mixPassWord = username + password;
        byte[] srcData = mixPassWord.getBytes(GMBaseUtil.DEFAULT_ENCODING);
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM2KEY_PRIVATE_KEY);
        byte[] hmac = SM3Util.hmac(key, srcData);
        return Base64Utils.encodeToUrlSafeString(hmac);
    }

    /**
     * 对原文进行私钥签名
     *
     * @param originalData 原文
     * @return 原文签名
     * @throws UnsupportedEncodingException
     */
    public static String getSign(String originalData) throws Exception {
        //原文进行sm3 hmac 加密
        byte[] originalDataBytes = originalData.getBytes(GMBaseUtil.DEFAULT_ENCODING);
        byte[] sm2PrivateKeyBytes = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM2KEY_PRIVATE_KEY);
        byte[] originalHmacData = SM3Util.hmac(sm2PrivateKeyBytes, originalDataBytes);

        //获取初始化密钥对
        byte[] keyPairBytes = Base64Utils.decodeFromUrlSafeString(SERIALIZE_KEY_PAIR);

        KeyPair keyPair = (KeyPair) SerializeUtils.toObject(keyPairBytes);

        //生成签名
        byte[] signBytes = SM2Util.sign((BCECPrivateKey) keyPair.getPrivate(), originalHmacData);
        return Base64Utils.encodeToUrlSafeString(signBytes);
    }

    /**
     * 验证数据一致性
     *
     * @param verifyData
     * @param signData
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verifyBySign(String verifyData, String signData) throws UnsupportedEncodingException {
        //获取初始化密钥对
        byte[] keyPairBytes = Base64Utils.decodeFromUrlSafeString(SERIALIZE_KEY_PAIR);
        KeyPair keyPair = (KeyPair) SerializeUtils.toObject(keyPairBytes);

        //获取签名数组
        byte[] signBytes = Base64Utils.decodeFromUrlSafeString(signData);

        //sm3 hamc加密验证数据
        byte[] verifyDataBytes = verifyData.getBytes(DEFAULT_ENCODING);
        byte[] sm2PrivateKeyBytes = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM2KEY_PRIVATE_KEY);
        byte[] verifyHmacData = SM3Util.hmac(sm2PrivateKeyBytes, verifyDataBytes);

        return SM2Util.verify((BCECPublicKey) keyPair.getPublic(), SM2Util.WITHID.getBytes(GMBaseUtil.DEFAULT_ENCODING),
                verifyHmacData, signBytes);
    }

    /**
     * 对字符串进行sm4加密
     *
     * @param data 加密字符串
     * @return java.lang.String
     * @author shanwj
     * @date 2019/5/31 15:11
     */
    public static String encryptString(String data) throws Exception {
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
        byte[] srcData = data.getBytes(GMBaseUtil.DEFAULT_ENCODING);
        byte[] bytes = SM4Util.encryptEcbPadding(key, srcData);
        return Base64Utils.encodeToUrlSafeString(bytes);
    }

    /**
     * 对字符串进行sm4解密
     *
     * @param cipherText 密文
     * @return java.lang.String
     * @author shanwj
     * @date 2019/5/31 15:11
     */
    public static String decryptString(String cipherText) throws Exception {
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
        byte[] bytes = Base64Utils.decodeFromUrlSafeString(cipherText);
        byte[] srcData = SM4Util.decryptEcbPadding(key, bytes);
        return new String(srcData, "UTF-8");
    }

    /**
     * 初始化国密算法静态变量值
     *
     * @author shanwj
     * @date 2019/4/22 10:28
     */
    public static void initSmKeyValue(CacheUtil cacheUtil) throws Exception {
        if (StringUtils.isBlank(GMBaseUtil.SM4KEY)) {
            String sm4 = cacheUtil.get("platform_sm4_key");
            int i = 1;
            while (StringUtils.isBlank(sm4)) {
                log.info("缓存中当前密钥为空,正在尝试第[ " + i + " ]次....");
                // 20200213 19:38
                // 2秒修改为10秒
                Thread.sleep(10000);
                sm4 = cacheUtil.get("platform_sm4_key");
                i++;
                if (i > 20) {
                    log.error("缓存中当前密钥为空,程序无法启动....");
                    System.exit(-1);
                }
            }
            String keyPair = cacheUtil.get("platform_sm2_key_pair");
            String privateKey = cacheUtil.get("platform_sm2_private_key");
            String publicKey = cacheUtil.get("platform_sm2_public_key");
            GMBaseUtil.SM4KEY =
                    Base64Utils.encodeToUrlSafeString(
                            SM4Util.decryptEcbPadding(INIT_SM4_KEY, Base64Utils.decodeFromUrlSafeString(sm4)));
            GMBaseUtil.SERIALIZE_KEY_PAIR =
                    Base64Utils.encodeToUrlSafeString(
                            SM4Util.decryptEcbPadding(INIT_SM4_KEY, Base64Utils.decodeFromUrlSafeString(keyPair)));
            GMBaseUtil.SM2KEY_PRIVATE_KEY =
                    Base64Utils.encodeToUrlSafeString(
                            SM4Util.decryptEcbPadding(INIT_SM4_KEY, Base64Utils.decodeFromUrlSafeString(privateKey)));
            GMBaseUtil.SM2KEY_PUBLIC_KEY =
                    Base64Utils.encodeToUrlSafeString(
                            SM4Util.decryptEcbPadding(INIT_SM4_KEY, Base64Utils.decodeFromUrlSafeString(publicKey)));
        }
    }

}
