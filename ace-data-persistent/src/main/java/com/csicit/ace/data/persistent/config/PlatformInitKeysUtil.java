package com.csicit.ace.data.persistent.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.cache.SerializeUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.cipher.SM2Util;
import com.csicit.ace.common.utils.cipher.SM4Util;
import com.csicit.ace.data.persistent.service.SysConfigServiceD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/17 10:30
 */
@Component
public class PlatformInitKeysUtil {

    private static Logger logger = LoggerFactory.getLogger(PlatformInitKeysUtil.class);

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SysConfigServiceD sysConfigServiceD;


    /**
     * 单机版启动的工作如下：
     * 1、第一次初始化需要生成密钥
     * 2、扫描自身配置项
     * 3、加载数据库配置项
     *
     * @return
     * @author FourLeaves
     * @date 2020/4/17 10:43
     */
    public void initSmKeyValue() {
        try {
            //单机版
//            if(Constants.isMonomerApp){
//                createKey();
//            }else{//platform
            int count = sysConfigServiceD.count(new QueryWrapper<SysConfigDO>().eq("NAME", "platform_init"));
            if (count == 0) {
                createKey();
            } else {
                setKeyValue();
                GMBaseUtil.initSmKeyValue(cacheUtil);
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    private void createKey() {
        try {
            initAppIdentifying();
            initKey();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            SysConfigDO sysConfigDO = new SysConfigDO();
            sysConfigDO.setName("platform_init");
            sysConfigDO.setValue("平台已初始化");
            sysConfigDO.setRemark("初始化配置");
            sysConfigDO.setAppId(appName);
            sysConfigDO.setScope(1);
            sysConfigServiceD.save(sysConfigDO);
            cacheUtil.set("platform_init", "平台已初始化", CacheUtil.NOT_EXPIRE);
        }
    }

    /**
     * 防止缓存重启后数据丢失，platform重启时判断缓存有没有数据，没有则从数据库查询再插入缓存
     *
     * @return
     * @author yansiyang
     * @date 2019/8/16 16:00
     */
    private void setKeyValue() {
        String[] keys = {"platform_secret_key", "platform_sm4_key", "platform_sm2_key_pair", "platform_sm2_private_key",
                "platform_sm2_public_key",
                "platform_init","defaultPassword", "defaultLanguage", "platformName", "defaultSecretLevel", "defaultTokenExpireDay"};
        List<SysConfigDO> list = sysConfigServiceD.list(new QueryWrapper<SysConfigDO>().in("name", Arrays.asList(keys)));
        list.forEach(config -> {
            String key = config.getName();
            String value = config.getValue();
            cacheUtil.set(key, value, CacheUtil.NOT_EXPIRE);
        });
    }

    /**
     * 初始化应用标识
     *
     * @author shanwj
     * @date 2019/5/16 16:42
     */
    private void initAppIdentifying() {
        SysConfigDO sysConfigDO = new SysConfigDO();
        sysConfigDO.setName("app_identifying");
        sysConfigDO.setValue("aa");
        sysConfigDO.setRemark("初始化配置");
        sysConfigDO.setAppId(appName);
        sysConfigDO.setScope(1);
        sysConfigServiceD.save(sysConfigDO);
    }

    /**
     * 初始化密钥
     *
     * @author shanwj
     * @date 2019/4/12 11:12
     */
    private void initKey() throws Exception {
        initSm4Key();
        initSm2KeyPair();
    }

    /**
     * 生成sm4随机全局密钥
     *
     * @author shanwj
     * @date 2019/4/12 11:13
     */
    private void initSm4Key() throws NoSuchProviderException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        byte[] sm4Key = SM4Util.generateKey();
        GMBaseUtil.SM4KEY = Base64Utils.encodeToUrlSafeString(sm4Key);
        byte[] sm4Keys = SM4Util.encryptEcbPadding(GMBaseUtil.INIT_SM4_KEY, sm4Key);
        cacheUtil.set("platform_sm4_key", Base64Utils.encodeToUrlSafeString(sm4Keys), CacheUtil.NOT_EXPIRE);
        SysConfigDO sm4SysConfigDO = new SysConfigDO();
        sm4SysConfigDO.setName("platform_sm4_key");
        sm4SysConfigDO.setValue(Base64Utils.encodeToUrlSafeString(sm4Keys));
        sm4SysConfigDO.setRemark("初始化配置");
        sm4SysConfigDO.setAppId(appName);
        sm4SysConfigDO.setScope(1);
        sysConfigServiceD.save(sm4SysConfigDO);
    }

    /**
     * 生成全局秘钥对
     *
     * @author shanwj
     * @date 2019/5/10 9:52
     */
    public void initSm2KeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,
            InvalidKeyException {
        KeyPair keyPair = SM2Util.generateKeyPair();
        byte[] keyPairBytes = SerializeUtils.toByteArray(keyPair);
        GMBaseUtil.SERIALIZE_KEY_PAIR = Base64Utils.encodeToUrlSafeString(keyPairBytes);
        byte[] privateKeySm4Keys = SM4Util.encryptEcbPadding(GMBaseUtil.INIT_SM4_KEY, keyPairBytes);
        cacheUtil.set("platform_sm2_key_pair", Base64Utils.encodeToUrlSafeString(privateKeySm4Keys), CacheUtil.NOT_EXPIRE);
        SysConfigDO sm2KeyPairConfigDO = new SysConfigDO();
        sm2KeyPairConfigDO.setName("platform_sm2_key_pair");
        sm2KeyPairConfigDO.setValue(Base64Utils.encodeToUrlSafeString(privateKeySm4Keys));
        sm2KeyPairConfigDO.setRemark("初始化配置");
        sm2KeyPairConfigDO.setAppId(appName);
        sm2KeyPairConfigDO.setScope(1);
        sysConfigServiceD.save(sm2KeyPairConfigDO);
        initSm2PrivateKey(keyPair);
        initSm2PublicKey(keyPair);
    }

    /**
     * 生成sm2随机全局私钥
     *
     * @param keyPair 初始化随机生成密钥对
     * @author shanwj
     * @date 2019/4/12 11:13
     */
    private void initSm2PrivateKey(KeyPair keyPair) throws NoSuchAlgorithmException, NoSuchProviderException,
            IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        GMBaseUtil.SM2KEY_PRIVATE_KEY = Base64Utils.encodeToUrlSafeString(privateKeyBytes);
        byte[] privateKeySm4Keys = SM4Util.encryptEcbPadding(GMBaseUtil.INIT_SM4_KEY, privateKeyBytes);
        cacheUtil.set("platform_sm2_private_key",
                Base64Utils.encodeToUrlSafeString(privateKeySm4Keys), CacheUtil.NOT_EXPIRE);
        SysConfigDO sm2PrivateConfigDO = new SysConfigDO();
        sm2PrivateConfigDO.setName("platform_sm2_private_key");
        sm2PrivateConfigDO.setValue(Base64Utils.encodeToUrlSafeString(privateKeySm4Keys));
        sm2PrivateConfigDO.setRemark("初始化配置");
        sm2PrivateConfigDO.setAppId(appName);
        sm2PrivateConfigDO.setScope(1);
        sysConfigServiceD.save(sm2PrivateConfigDO);
    }

    /**
     * 生成sm2随机全局公钥
     *
     * @param keyPair 初始化随机生成密钥对
     * @author shanwj
     * @date 2019/4/12 11:13
     */
    private void initSm2PublicKey(KeyPair keyPair) throws NoSuchAlgorithmException, NoSuchProviderException,
            IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        GMBaseUtil.SM2KEY_PUBLIC_KEY = Base64Utils.encodeToUrlSafeString(publicKeyBytes);
        byte[] publicKeySm4Keys = SM4Util.encryptEcbPadding(GMBaseUtil.INIT_SM4_KEY, publicKeyBytes);
        GMBaseUtil.SM2KEY_PUBLIC_KEY = Base64Utils.encodeToUrlSafeString(publicKeySm4Keys);
        cacheUtil.set("platform_sm2_public_key",
                Base64Utils.encodeToUrlSafeString(publicKeySm4Keys), CacheUtil.NOT_EXPIRE);
        SysConfigDO sm2PublicConfigDO = new SysConfigDO();
        sm2PublicConfigDO.setName("platform_sm2_public_key");
        sm2PublicConfigDO.setValue(Base64Utils.encodeToUrlSafeString(publicKeySm4Keys));
        sm2PublicConfigDO.setRemark("初始化配置");
        sm2PublicConfigDO.setAppId(appName);
        sm2PublicConfigDO.setScope(1);
        sysConfigServiceD.save(sm2PublicConfigDO);
    }

}
