package com.csicit.ace.cloudImpl.config;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.start.IAceAppStartToDo;
import com.csicit.ace.common.start.IAceOnlyOneAppStartToDo;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.ScanClassUtils;
import com.csicit.ace.common.utils.SerialNumberUtil;
import com.csicit.ace.common.utils.cache.SerializeUtils;
import com.csicit.ace.common.utils.cipher.SM2Util;
import com.csicit.ace.common.utils.redis.RedisUtils;
import com.csicit.ace.interfaces.service.IApi;
import com.csicit.ace.interfaces.service.IConfig;
import com.csicit.ace.interfaces.service.IStart;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author shanwj
 * @date 2019/7/1 20:42
 */
//@Component
//@Order(-1)
public class InitConfig implements ApplicationRunner {
    @Autowired
    AceLogger logger;
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;
    /**
     * nacos地址
     */
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosAddress;
    /**
     * redis访问工具类对象
     */
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    HttpClient client;

    /**
     * api访问接口对象
     */
    @Autowired
    private IApi api;

    @Autowired
    private IStart start;

    @Autowired
    private IConfig config;

    @NonNull
    @Autowired
    DiscoveryUtils discoveryUtils;

    /**
     * 启动时app进行的一些操作
     */
    @NonNull
    @Autowired
    List<IAceAppStartToDo> aceAppStartToDos;

    /**
     * 启动时只能在一个app上进行的某些操作
     */
    @NonNull
    @Autowired
    List<IAceOnlyOneAppStartToDo> aceOnlyOneAppStartToDos;


    private static String PublicStr = "rO0ABXNyADxvcmcuYm91bmN5Y2FzdGxlLmpjYWpjZS5wcm92aWRlci5hc3ltbWV0cm" +
            "ljLmVjLkJDRUNQdW" +
            "JsaWNLZXkhn3qKo-pIJAMAAloAD3dpdGhDb21wcmVzc2lvbkwACWFsZ29yaXRobXQAEkxqYXZhL2xhbmcvU3RyaW5nO3hw" +
            "AHQAAkVDdXIAAltCrPMX-AYIVOACAAB4cAAAATcwggEzMIHsBgcqhkjOPQIBMIHgAgEBMCwGByqGSM49AQECIQD____-" +
            "_____________________wAAAAD__________zBEBCD____-_____________________wAAAAD__________AQgKOn6np" +
            "2fXjRNWp5Lz2UJp_OXifUVq4-S3by9QU2UDpMEQQQyxK4sHxmBGV-ZBEZqOcmUj-MLv_JmC-FxWkWJM0x0x7w3NqL09nec" +
            "Wb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA_____v_______________3ID32shxgUrU7v0CTnVQSMCAQEDQgAEY7yAGa" +
            "P0izmHvg2AUuJHaU2gNxz0PRTJSPN4eluAaxu2N8tkA9f_eFt_w4oMNYjO8xBBjX6gPAvm3zJ0l2f7J3g=";

    @Override
    public void run(ApplicationArguments args) {
        try {
//          checkLicense();
//            GMBaseUtil.initSmKeyValue(redisUtils);  在common包里解决 AceAppStartToDoDemo
            //redisUtils.publish("newAceAppEvent", appName);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(aceAppStartToDos)) {
                for (IAceAppStartToDo aceAppStartToDo : aceAppStartToDos) {
                    aceAppStartToDo.run();
                }
            }
            // 应用启动过程中执行的逻辑必须满足两个条件
            // 1、nacos上注册的应用应该没有健康实例
            // 2、数据库中的应用启动锁应该是释放状态
            if (start.canAppStartToDo()) {
                config.scanConfig();
                initApiResource();
                // 拓展的接口
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(aceOnlyOneAppStartToDos)) {
                    for (IAceOnlyOneAppStartToDo aceOnlyOneAppStartToDo : aceOnlyOneAppStartToDos) {
                        aceOnlyOneAppStartToDo.run();
                    }
                }
                start.completeAppStartToDo();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 验证当前启动应用是否符合授权条件
     *
     * @author shanwj
     * @date 2019/7/2 16:06
     */
    public void checkLicense() throws UnsupportedEncodingException {
        //验证是否上传授权文件
        String license = redisUtils.get("platform_secret_key");
        if (StringUtils.isEmpty(license)) {
            logger.error("无授权信息!");
            System.exit(-1);
        }
        //授权文件信息进行验签
        JSONObject jsonObject = JSONObject.fromObject(license);
        String sign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        String licenseStr = jsonObject.toString();
        byte[] publicBytes = Base64Utils.decodeFromUrlSafeString(PublicStr);
        BCECPublicKey publicKeyPair = (BCECPublicKey) SerializeUtils.toObject(publicBytes);
        boolean verify = SM2Util.verify(publicKeyPair, null,
                licenseStr.getBytes("UTF-8"), Base64Utils.decodeFromUrlSafeString(sign));
        if (!verify) {
            logger.error("授权信息验证失败!");
            System.exit(-1);
        }
        String type = jsonObject.getString("type");
        if (Objects.equals(type, "cloud")) {
            checkCloudApp(jsonObject);
        } else {
            checkSingleApp(jsonObject);
        }
    }

    /**
     * 检验云版本授权
     *
     * @param jsonObject
     * @author shanwj
     * @date 2019/7/2 16:47
     */
    private void checkCloudApp(JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        String oldAppName = appName.substring(3, appName.length()).toLowerCase();
        JSONArray array = jsonObject.getJSONArray("apps");
        int count = 0;
        String endDate = null;
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String appId = object.getString("appId").toLowerCase();
            if (Objects.equals(oldAppName, appId)) {
                count = Integer.parseInt(object.getString("count"));
                endDate = object.getString("endDate");
                break;
            }
        }
        if (endDate == null) {
            return;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate licenseDt = LocalDate.parse(endDate, df);
        LocalDate now = LocalDate.now();
        if (Objects.equals(type, "prod") && now.isAfter(licenseDt)) {
            logger.error("当前应用已超授权时间!");
            System.exit(-1);
        }
//        String url = "http://" + nacosAddress + "/nacos/v1/console/namespaces?namespaceId=";
//        String resStr = this.client.client(url);
//        List<String> nameSpaces = new ArrayList<>(16);
//        JSONObject resNsObj = JSONObject.fromObject(resStr);
//        JSONArray nsData = resNsObj.getJSONArray("data");
//        for (int i = 0; i < nsData.size(); i++) {
//            JSONObject obj = nsData.getJSONObject(i);
//            nameSpaces.add(obj.getString("namespace"));
//        }
//        int runAppCount = 0;
//        for (String ns : nameSpaces) {
//            String address =
//                    "http://" + nacosAddress +
//                            "/nacos/v1/ns/catalog/services?withInstances=false&pageNo=1&pageSize=10000&namespaceId=" + ns;
//            String rsStr = this.client.client(address);
//            JSONObject resObj = JSONObject.fromObject(rsStr);
//            JSONArray runApps = resObj.getJSONArray("serviceList");
//            if (runApps.size() > 0) {
//                for (int i = 0; i < runApps.size(); i++) {
//                    JSONObject obj = runApps.getJSONObject(i);
//                    String name = obj.getString("name");
//                    name = name.substring(3, name.length()).toLowerCase();
//                    if (Objects.equals(name, oldAppName)) {
//                        int appCount = obj.getInt("ipCount");
//                        runAppCount = runAppCount + appCount;
//                        continue;
//                    }
//                }
//            }
//        }
        if (discoveryUtils.getHealthyInstances(appName).size() > count) {
            logger.error("授权应用已达最大授权数量!");
            System.exit(-1);
        }
    }

    private void checkSingleApp(JSONObject jsonObject) {
        JSONArray array = jsonObject.getJSONArray("apps");
        JSONObject jo = array.getJSONObject(0);
        String motherBoardNum = jo.getString("motherBoardNum");
        String macAddress = jo.getString("macAddress");
        if (!Objects.equals(motherBoardNum, SerialNumberUtil.getMotherboardSN())) {
            logger.error("授权主板编号不一致!");
            System.exit(-1);
        }
        if (!Objects.equals(macAddress, SerialNumberUtil.getMac())) {
            logger.error("授权MAC地址不一致!");
            System.exit(-1);
        }
    }

    private void initApiResource() {
        //包对应的绝对地址
        //String pkgPath = ResourceUtils.getURL("classpath:").getPath()+packagePath;
        //保存包路径下class的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // ScanClassUtils.setClassesSet(packageName,pkgPath,classes);
        ScanClassUtils.getInitScanClass(Constants.BasePackages, classes);
        List<SysApiResourceDO> list = new ArrayList<>(16);
        classes.stream().forEach(clazz -> {
            RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
            if (rm == null || rm.value().length == 0) {
                return;
            }
            Arrays.asList(clazz.getDeclaredMethods()).stream().forEach(method -> {
                AceAuth aceAuth = method.getAnnotation(AceAuth.class);
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (aceAuth != null && requestMapping != null) {
                    String authId = clazz.getName() + "." + method.getName();
                    String authName = aceAuth.value();
                    SysApiResourceDO sysApiResourceDO = new SysApiResourceDO();
                    sysApiResourceDO.setAppId(appName);
                    sysApiResourceDO.setId(authId);
                    sysApiResourceDO.setName(authName);
                    sysApiResourceDO.setApiUrl(
                            rm.value()[0] + (requestMapping.value().length > 0 ? requestMapping.value()[0] : ""));
                    sysApiResourceDO.setApiMethod(
                            requestMapping.method().length > 0 ? requestMapping.method()[0].toString() : "");
                    list.add(sysApiResourceDO);
                }
            });
        });
        api.save(list);
    }

}
