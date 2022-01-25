package com.csicit.ace.common.utils.cipher;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import java.math.BigInteger;
import java.security.*;

/**
 * SM2加密算法工具类
 *
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
public class SM2Util extends GMBaseUtil {
    private static final String ALGO_NAME_EC = "EC";
    static final String WITHID = "CNJARIACEJARIACE";
    private static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    private final static BigInteger SM2_ECC_N = CURVE.getOrder();
    private final static BigInteger SM2_ECC_H = CURVE.getCofactor();
    private final static BigInteger SM2_ECC_GX = new BigInteger(
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    private final static BigInteger SM2_ECC_GY = new BigInteger(
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    private static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    private static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G_POINT,
            SM2_ECC_N, SM2_ECC_H);

    /**
     * 获取密钥
     * @return java.security.KeyPair 密钥对
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        SecureRandom random = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO_NAME_EC, BouncyCastleProvider.PROVIDER_NAME);
        ECParameterSpec parameterSpec = new ECParameterSpec(DOMAIN_PARAMS.getCurve(), DOMAIN_PARAMS.getG(),
                DOMAIN_PARAMS.getN(), DOMAIN_PARAMS.getH());
        kpg.initialize(parameterSpec, random);
        return kpg.generateKeyPair();
    }

    /**
     *
     * @param pubKey 公钥加密
     * @param srcData 原文
     * @return 加密后数据
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static byte[] encrypt(BCECPublicKey pubKey, byte[] srcData) throws InvalidCipherTextException {
        ECPublicKeyParameters pubKeyParameters = convertPublicKeyToParameters(pubKey);
        return encrypt(pubKeyParameters, srcData);
    }

    /**
     * ECC公钥加密
     *
     * @param pubKeyParameters ECC公钥
     * @param srcData          源数据
     * @return SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static byte[] encrypt(ECPublicKeyParameters pubKeyParameters, byte[] srcData)
            throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        ParametersWithRandom pwr = new ParametersWithRandom(pubKeyParameters, new SecureRandom());
        engine.init(true, pwr);
        return engine.processBlock(srcData, 0, srcData.length);
    }

    public static byte[] decrypt(BCECPrivateKey priKey, byte[] sm2Cipher) throws InvalidCipherTextException {
        ECPrivateKeyParameters priKeyParameters = convertPrivateKeyToParameters(priKey);
        return decrypt(priKeyParameters, sm2Cipher);
    }

    /**
     * ECC私钥解密
     *
     * @param priKeyParameters ECC私钥
     * @param sm2Cipher        SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @return 原文
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static byte[] decrypt(ECPrivateKeyParameters priKeyParameters, byte[] sm2Cipher)
            throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        engine.init(false, priKeyParameters);
        return engine.processBlock(sm2Cipher, 0, sm2Cipher.length);
    }


    public static byte[] sign(BCECPrivateKey priKey, byte[] srcData) throws CryptoException {
        ECPrivateKeyParameters priKeyParameters = convertPrivateKeyToParameters(priKey);
        return sign(priKeyParameters, null, srcData);
    }

    /**
     * ECC私钥签名
     *
     * @param priKeyParameters ECC私钥
     * @param withId           可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param srcData          源数据
     * @return 签名
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static byte[] sign(ECPrivateKeyParameters priKeyParameters, byte[] withId, byte[] srcData)
            throws CryptoException {
        SM2Signer signer = new SM2Signer();
        CipherParameters param;
        ParametersWithRandom pwr = new ParametersWithRandom(priKeyParameters, new SecureRandom());
        if (withId == null) {
            param = new ParametersWithID(pwr, WITHID.getBytes());
        } else {
            param = new ParametersWithID(pwr, withId);
        }
        signer.init(true, param);
        signer.update(srcData, 0, srcData.length);
        return signer.generateSignature();
    }

    public static boolean verify(ECPublicKeyParameters pubKeyParameters, byte[] srcData, byte[] sign) {
        return verify(pubKeyParameters, null, srcData, sign);
    }

    public static boolean verify(BCECPublicKey pubKey, byte[] withId, byte[] srcData, byte[] sign) {
        ECPublicKeyParameters pubKeyParameters = convertPublicKeyToParameters(pubKey);
        return verify(pubKeyParameters, withId, srcData, sign);
    }

    /**
     * ECC公钥验签
     *
     * @param pubKeyParameters ECC公钥
     * @param withId           可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param srcData          原文
     * @param sign             签名
     * @return 验签成功返回true，失败返回false
     * @author shanwj
     * @date 2019/4/12 17:33
     */
    public static boolean verify(ECPublicKeyParameters pubKeyParameters, byte[] withId, byte[] srcData, byte[] sign) {
        SM2Signer signer = new SM2Signer();
        CipherParameters param;
        if (withId == null) {
            param = new ParametersWithID(pubKeyParameters, WITHID.getBytes());
        } else {
            param = new ParametersWithID(pubKeyParameters, withId);
        }
        signer.init(false, param);
        signer.update(srcData, 0, srcData.length);
        return signer.verifySignature(sign);
    }

    /** 
     *  获取ECC模式公钥
     * @param ecPubKey 公钥
     * @return ECC模式公钥
     * @author shanwj
     * @date 2019/4/11 20:20
     */
    private static ECPublicKeyParameters convertPublicKeyToParameters(BCECPublicKey ecPubKey) {
        ECParameterSpec parameterSpec = ecPubKey.getParameters();
        ECDomainParameters domainParameters = new ECDomainParameters(parameterSpec.getCurve(), parameterSpec.getG(),
                parameterSpec.getN(), parameterSpec.getH());
        return new ECPublicKeyParameters(ecPubKey.getQ(), domainParameters);
    }

    /**
     * 获取ECC模式私钥
     * @param ecPriKey	私钥
     * @return ECC模式私钥
     * @author shanwj
     * @date 2019/4/11 20:21
     */
    public static ECPrivateKeyParameters convertPrivateKeyToParameters(BCECPrivateKey ecPriKey) {
        ECParameterSpec parameterSpec = ecPriKey.getParameters();
        ECDomainParameters domainParameters = new ECDomainParameters(parameterSpec.getCurve(), parameterSpec.getG(),
                parameterSpec.getN(), parameterSpec.getH());
        return new ECPrivateKeyParameters(ecPriKey.getD(), domainParameters);
    }

}
