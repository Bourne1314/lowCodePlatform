package com.csicit.ace.zuul.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/18 9:36
 */
@Component
@Slf4j
public class AppClientConfig implements ApplicationRunner {

    private final static String uuid = UUID.randomUUID().toString();

    @Value("${spring.application.name}")
    private String appName;

    @Value("${ace.config.license.server.port:2222}")
    private Integer port;

    @Value("${ace.config.license.server.ip:127.0.0.1}")
    private String ip;

    private final static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static int FreeCompareMinute = 180;


    /**
     * 5分钟 执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "*/10 * * * * ?")
    private void registerApp() {
        try {
            LocalDateTime ldt = LocalDateTime.parse(System.getProperty("ace.startTime"), df);
            //往前推一小时
            LocalDateTime now = LocalDateTime.now().minusMinutes(FreeCompareMinute);
//            System.out.println(ldt.isBefore(now));
            if (ldt.isBefore(now)) {
                String url = "http://" + ip + ":" + port + "/register";
                Map<String, String> params = new HashMap<>();
                params.put("appId", appName);
                params.put("uuId", uuid);
                url += buildUrl(params);
                String result = client(url);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    String state = jsonObject.getString("state");
                    if (state.equals("44444")) {
                        log.error("授权异常");
                        log.error(jsonObject.getString("info"));
                        System.exit(-1);
                    }
                    System.out.println("当前应用已授权");
                    return;
                }
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("授权异常，请检查License服务器或授权信息！");
        System.exit(-1);
    }

    private String client(String url) {
        RestTemplate client = new RestTemplate();
        //  执行HTTP请求
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        return response.getBody();
    }


    private String buildUrl(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String str = "?";
        StringJoiner joiner1 = new StringJoiner("&");
        for (String key : keys) {
            joiner1.add(key + "=" + map.get(key));
        }
        str = str + joiner1.toString();
        return str;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.setProperty("ace.startTime", df.format(LocalDateTime.now()));
//        registerApp();
    }

}