package com.csicit.ace.license.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.license.core.domain.LicenseAppDO;
import com.csicit.ace.license.core.domain.LicenseDO;
import com.csicit.ace.license.core.mapper.LicenseDBHelperMapper;
import com.csicit.ace.license.core.service.LicenseService;
import com.csicit.ace.license.core.util.SM2Util;
import com.csicit.ace.license.core.util.SerialNumberUtil;
import com.csicit.ace.license.core.util.SerializeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/18 9:12
 */
@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    private static List<LicenseDO> REGISTER_APPS = new ArrayList<>();

    private static List<LicenseAppDO> LICENSE_APPS = new ArrayList<>();

    private static JSONObject LIC = new JSONObject();

    private final static String PublicStr = "rO0ABXNyADxvcmcuYm91bmN5Y2FzdGxlLmpjYWpjZS5wcm92aWRlci5hc3ltbWV0cm" +
            "ljLmVjLkJDRUNQdW" +
            "JsaWNLZXkhn3qKo-pIJAMAAloAD3dpdGhDb21wcmVzc2lvbkwACWFsZ29yaXRobXQAEkxqYXZhL2xhbmcvU3RyaW5nO3hw" +
            "AHQAAkVDdXIAAltCrPMX-AYIVOACAAB4cAAAATcwggEzMIHsBgcqhkjOPQIBMIHgAgEBMCwGByqGSM49AQECIQD____-" +
            "_____________________wAAAAD__________zBEBCD____-_____________________wAAAAD__________AQgKOn6np" +
            "2fXjRNWp5Lz2UJp_OXifUVq4-S3by9QU2UDpMEQQQyxK4sHxmBGV-ZBEZqOcmUj-MLv_JmC-FxWkWJM0x0x7w3NqL09nec" +
            "Wb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA_____v_______________3ID32shxgUrU7v0CTnVQSMCAQEDQgAEY7yAGa" +
            "P0izmHvg2AUuJHaU2gNxz0PRTJSPN4eluAaxu2N8tkA9f_eFt_w4oMNYjO8xBBjX6gPAvm3zJ0l2f7J3g=";


    private static Boolean IS_OK = true;

    private static String INFO = null;

    private final static int FixRegisterAppMinute = 2;

    @Autowired
    private LicenseDBHelperMapper dbHelperMapper;

    @Override
    public String register(Map<String, String> params) {
        JSONObject result = new JSONObject();
        String state = "yes";
        String info = null;
        String appId = params.get("appId");
        if (!LICENSE_APPS.stream().anyMatch(app -> Objects.equals(app.getAppId(), appId))) {
            state = "44444";
            info = "?????????????????????";
        }
        LicenseAppDO appDO = LICENSE_APPS.stream().filter(app -> Objects.equals(app.getAppId(), appId))
                .findFirst().get();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("?????????????????????" + appDO.getEndDate().format(df));
        if (appDO.getEndDate().isBefore(LocalDate.now())) {
            if (LICENSE_APPS.stream().anyMatch(app -> Objects.equals(app.getAppId(), appId))) {
                state = "44444";
                info = "???????????????????????????";
            }
        }

        String uuId = params.get("uuId");

        fixRegisterApps();

        long count = REGISTER_APPS.stream().filter(app -> Objects.equals(app.getAppId(), appId)
                && !Objects.equals(app.getUuId(), uuId)).count();
        if (count >= appDO.getCount()) {
            state = "44444";
            info = "?????????????????????????????????";
        }

        LicenseDO licenseDO = REGISTER_APPS.stream().filter(app -> Objects.equals(app.getAppId(), appId)
                && Objects.equals(app.getUuId(), uuId))
                .findFirst().orElse(new LicenseDO());
        licenseDO.setRegisterTime(LocalDateTime.now());
        if (StringUtils.isBlank(licenseDO.getAppId())) {
            licenseDO.setAppId(appId);
            licenseDO.setUuId(uuId);
            REGISTER_APPS.add(licenseDO);
        }
        result.put("state", state);
        result.put("info", info);
        return result.toJSONString();
    }


    public static String serverUuid = UUID.randomUUID().toString();

    public static int uuidCount = 0;

    @Override
    public void checkManyServer() {
        String uuidInEnv = System.getProperty("ace.uuid");
        if (!Objects.equals(uuidInEnv, serverUuid)) {
            log.error("????????????????????????????????????!");
            System.exit(-1);
        }
        uuidCount++;
        if (uuidCount > 2) {
            serverUuid = UUID.randomUUID().toString();
            uuidCount = 0;
            System.setProperty("ace.uuid", serverUuid);
        }
    }

    /**
     * ?????????????????????????????????5??????????????????????????????5???????????????app???
     *
     * @param
     * @return void
     * @date 2021/11/29 11:01
     */
    @Override
    public void fixRegisterApps() {
        List<LicenseDO> REGISTER_APPS_T = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().minusMinutes(FixRegisterAppMinute);
        for (LicenseDO license : REGISTER_APPS) {
            if (license.getRegisterTime().isAfter(now)) {
                REGISTER_APPS_T.add(license);
            }
        }
        REGISTER_APPS = REGISTER_APPS_T;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @author FourLeaves
     * @date 2021/8/31 9:19
     */
    @Override
    public void checkLicense() {
        System.out.println("????????????MAC??????:" + SerialNumberUtil.getMac());
        System.out.println("????????????????????????IP:" + SerialNumberUtil.getLocalIps());
        System.out.println("????????????CPU??????:" + SerialNumberUtil.getCPUSerial().replace(" ", ""));
        System.out.println("???????????????????????????:" + SerialNumberUtil.getHardDiskSN().replace(" ", ""));
        System.out.println("??????????????????:" + SerialNumberUtil.getMotherboardSN().replace(" ", ""));
        try {
            //??????????????????????????????
            String license = dbHelperMapper.getString("select value from sys_config where name = " +
                    "'platform_secret_key'");
            if (StringUtils.isEmpty(license)) {
                log.error("???????????????!");
                System.exit(-1);
            }
            //??????????????????????????????
            JSONObject jsonObject = JSONObject.parseObject(license);
            String sign = jsonObject.getString("sign");
            jsonObject.remove("sign");
            String licenseStr = jsonObject.toString();
            byte[] publicBytes = Base64Utils.decodeFromUrlSafeString(PublicStr);
            BCECPublicKey publicKeyPair = (BCECPublicKey) SerializeUtils.toObject(publicBytes);
            boolean verify = SM2Util.verify(publicKeyPair, null,
                    licenseStr.getBytes("UTF-8"), Base64Utils.decodeFromUrlSafeString(sign));
            if (!verify) {
                log.error("????????????????????????!");
                System.exit(-1);
            }
            LIC = jsonObject;
            LICENSE_APPS = jsonObject.getJSONArray("apps").toJavaList(LicenseAppDO.class);
            checkSingleApp(jsonObject);
        } catch (Exception e) {
            log.error("????????????????????????!", e);
            System.exit(-1);
        }
        System.out.println("???????????????????????????????????????");
    }

    @Override
    public void macInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cpuNum", SerialNumberUtil.getCPUSerial());
        jsonObject.put("mBNum:", SerialNumberUtil.getMotherboardSN());
        jsonObject.put("mac:", SerialNumberUtil.getMac());
        jsonObject.put("diskNum:", SerialNumberUtil.getHardDiskSN());
        jsonObject.put("localIp:", SerialNumberUtil.getLocalIps());
        log.info(jsonObject.toJSONString());
    }

    /**
     * @param set ??????????????????
     * @param str ??????????????????
     * @return boolean
     * @date 2021/11/29 11:16
     */
    private boolean setHasStrings(Set<String> set, String str) {
        if (set.size() == 0 || StringUtils.isBlank(str)) {
            return true;
        }
        String[] strs = str.replace(" ", "").split(",");
        if (strs.length == 1) {
            return set.contains(str);
        }
        return set.containsAll(Arrays.asList(strs));
    }

    /**
     * ??????????????? ??????
     *
     * @author FourLeaves
     * @date 2021/8/31 9:22
     */
    private void checkSingleApp(JSONObject jsonObject) {
        String mbNum = jsonObject.getString("mbNum");
        String cpuNum = jsonObject.getString("cpuNum");
        String mac = jsonObject.getString("mac");
        String diskNum = jsonObject.getString("diskNum");
        String localIp = jsonObject.getString("localIp");

        /**
         * ?????????????????????
         */
        if (StringUtils.isNotBlank(SerialNumberUtil.getHardDiskSN())) {
            if (!SerialNumberUtil.getHardDiskSN().replace(" ", "").contains(diskNum.replace(" ", ""))) {
                INFO = "??????????????????????????????????????????!";
                IS_OK = false;
            }
        }
        /**
         * ??????????????????ip
         */
        if (!CollectionUtils.isEmpty(SerialNumberUtil.getLocalIps())) {
            if (!setHasStrings(SerialNumberUtil.getLocalIps(), localIp)) {
                INFO = "??????????????????ip?????????????????????!";
                IS_OK = false;
            }
        }

        /**
         * ??????CPU??????
         */
        if (StringUtils.isNotBlank(SerialNumberUtil.getCPUSerial())) {
            if (!SerialNumberUtil.getCPUSerial().replace(" ", "").contains(cpuNum.replace(" ", ""))) {
                INFO = "??????CPU???????????????????????????!";
                IS_OK = false;
            }
        }
        /**
         * ??????????????????
         */
        if (StringUtils.isNotBlank(SerialNumberUtil.getMotherboardSN())) {
            if (!SerialNumberUtil.getMotherboardSN().replace(" ", "").contains(mbNum.replace(" ", ""))) {
                INFO = "???????????????????????????????????????!";
                IS_OK = false;
            }
        }
        /**
         * ??????MAC??????
         */
        if (!CollectionUtils.isEmpty(SerialNumberUtil.getMac())) {
            if (!setHasStrings(SerialNumberUtil.getMac(), mac)) {
                INFO = "??????MAC???????????????????????????!";
                IS_OK = false;
            }
        }

        if (!IS_OK) {
            log.error(INFO);
            System.exit(-1);
        }
    }
}
