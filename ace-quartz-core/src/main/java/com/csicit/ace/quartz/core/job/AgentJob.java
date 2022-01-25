package com.csicit.ace.quartz.core.job;

import com.csicit.ace.common.config.ZuulRouteConfig;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import net.sf.json.JSONObject;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.*;

/**
 * 定时任务
 *
 * @author zuogang
 * @date Created in 14:26 2019/7/16
 */
public class AgentJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${server.port}")
    String port;

    @Autowired
    HttpClient client;

    @Autowired
    CacheUtil cacheUtil;

    @Nullable
    @Autowired
    DiscoveryUtils discoveryUtils;

    @Autowired
    ZuulRouteConfig zuulRouteConfig;

    static List<String> apps = Arrays.asList(new String[]{"platform", "fileserver", "quartz", "report", "orgauth", "push"});

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> vo : jobDataMap.entrySet()) {
            if (!Objects.equals("paramAppId", vo.getKey()) && !Objects.equals("paramUrl", vo.getKey())
//                    && !Objects.equals("paramUserId", vo.getKey())
                    ) {
                map.put(vo.getKey(), vo.getValue());
            }
        }

        String appId = (String) jobDataMap.get("paramAppId");
//        String userId = (String) jobDataMap.get("paramUserId");
        String[] sourceStrArray = ((String) jobDataMap.get("paramUrl")).split("_");
        String beanName = sourceStrArray[0];
        String methodName = sourceStrArray[1];

        String jobAddress;
        if (discoveryUtils != null) {
            ServiceInstance serviceInstance = discoveryUtils.getOneHealthyInstance(appId);
            if (serviceInstance == null) {
                logger.error("没有健康的应用");
                throw new RException("没有健康的应用");
            }

            if (Objects.equals("/", beanName.substring(0, 1))) {
                jobAddress =
                        "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + beanName;
            } else {
                jobAddress =
                        "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/" + beanName;
            }
        } else {
            if (Constants.isZuulApp) {
                Set<String> appIds = zuulRouteConfig.getRoutes().keySet();
                if (!appIds.contains(appId) && !apps.contains(appId)) {
                    logger.warn("任务执行失败，原因为：应用" + appId + "不在线");
                    return;
                }
                if (beanName.startsWith("/")) {
                    jobAddress =
                            "http://127.0.0.1:" + port + "/" + appId + beanName;
                } else {
                    jobAddress =
                            "http://127.0.0.1:" + port + "/" + appId + "/" + beanName;
                }

            } else {
                jobAddress =
                        "http://127.0.0.1:" + port + "/" + beanName;
            }
        }

        //制作token
        SysUserDO user = new SysUserDO();
        user.setUserName(Constants.ACE_SERVICE_USER);
        user.setId(Constants.ACE_SERVICE_USER);
        String token = null;
        try {
            int seconds = 60 * 6;
            token = GMBaseUtil.getToken(user.getId(), user.getUserName());
            cacheUtil.hset(token, "user", com.alibaba.fastjson.JSONObject.toJSON(user), seconds);
            cacheUtil.set(token + "userid", user.getId(), seconds);
        } catch (Exception e) {
            logger.error("任务执行失败，原因为：生成token异常");
        }
        if (token == null) {
            return;
        }

        try {
            String res = this.client.client(token, jobAddress, HttpMethod.valueOf(methodName.toUpperCase()), map);
            if (res != null) {
                JSONObject resultObj = JSONObject.fromObject(res);
                String code = resultObj.getString("code");
                String msg = resultObj.getString("msg");
                if (Objects.equals(code, "40000")) {
                    logger.error("任务执行成功");
                } else {
                    logger.error("jobAddress: " + jobAddress);
                    logger.error("任务执行失败，原因为：" + msg);
                }
            }
        } catch (Exception e) {
            logger.error("jobAddress: " + jobAddress);
            throw e;
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
//        for (String ns : nameSpaces) {
//            String address =
//                    "http://" + nacosAddress +
//                            "/nacos/v1/ns/catalog/services?withInstances=false&pageNo=1&pageSize=10000&namespaceId="
//                            + ns;
//            String rsStr = this.client.client(address);
//            JSONObject resObj = JSONObject.fromObject(rsStr);
//            JSONArray runApps = resObj.getJSONArray("serviceList");
//            // 启动的应用列表
//            List<String> apps = new ArrayList<>(16);
//            if (runApps.size() > 0) {
//                for (int i = 0; i < runApps.size(); i++) {
//                    JSONObject obj = runApps.getJSONObject(i);
//                    String name = obj.getString("name");
//                    int ipCount = obj.getInt("ipCount");
//                    if (ipCount == 1) {
//                        apps.add(name);
//                    }
//                }
//            }
//            if (apps.contains(appId)) {
//                SysApiMixDO sysApiMixDO = sysApiMixService.getOne(new QueryWrapper<SysApiMixDO>()
//                        .eq("user_id", userId)
//                        .eq("api_url", beanName)
//                        .eq("api_method", methodName));
//                if (sysApiMixDO == null) {
//                    logger.error("任务执行失败，原因为：该用户ID没有该URL使用权限");
//                } else {
//                    String infoAddress =
//                            "http://" + nacosAddress +
//                                    "/nacos/v1/ns/catalog/instances?serviceName=" + appId +
//                                    "&clusterName=DEFAULT&pageSize=10&pageNo=1&namespaceId=" + ns;
//                    String result = this.client.client(infoAddress);
//                    JSONObject infoObj = JSONObject.fromObject(result);
//                    JSONArray list = infoObj.getJSONArray("list");
//
//
//                    String ip = list.getJSONObject(0).getString("ip");
//                    String port = list.getJSONObject(0).getString("port");
//
//                    //制作token
//                    SysUserDO user = sysUserService.getById(userId);
//                    String userName = user.getUserName();
//                    String token = null;
//                    try {
//                        int seconds = 60 * 6;
//                        token = GMBaseUtil.getToken(userId, userName);
//                        redisUtils.hset(token, "user", com.alibaba.fastjson.JSONObject.toJSON(user), seconds);
//                        redisUtils.set(token + "userid", user.getId(), seconds);
//                    } catch (Exception e) {
//                        logger.error("任务执行失败，原因为：生成token异常");
//                    }
//                    if (token == null) {
//                        return;
//                    }
//                    String jobAddress = "";
//                    if (Objects.equals("/", beanName.substring(0, 1))) {
//                        jobAddress =
//                                "http://" + ip + ":" + port + beanName;
//                    } else {
//                        jobAddress =
//                                "http://" + ip + ":" + port + "/" + beanName;
//                    }
//
//                    String res = this.client.client(token, jobAddress, HttpMethod.valueOf(methodName), map);
//                    if (res != null) {
//                        JSONObject resultObj = JSONObject.fromObject(res);
//                        String code = resultObj.getString("code");
//                        String msg = resultObj.getString("msg");
//                        if (Objects.equals(code, "40000")) {
//                            logger.error("任务执行成功");
//                        } else {
//                            logger.error("任务执行失败，原因为：" + msg);
//                        }
//                    }
//                }
//            } else {
//                logger.error("任务执行失败，该应用尚未启动");
//            }
//        }

    }
}
