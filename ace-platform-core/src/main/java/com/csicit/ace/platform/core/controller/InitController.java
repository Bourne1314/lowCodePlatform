package com.csicit.ace.platform.core.controller;

import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.InitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * 初始化 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/init")
@Api("系统初始化管理")
public class InitController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(InitController.class);

    /**
     * port
     */
    @Value("${server.port:8080}")
    private Integer port;

    /**
     * 是否是开发状态 0 否  1 是
     */
    @Value("${ace.config.isDevState:0}")
    private Integer isDevState;

    /**
     * socket端口
     */
    @Value("${ace.socket.port:5070}")
    private Integer socketPort;

    /**
     * 共享socket端口，多个单体版实例
     */
    @Value("${ace.socket.sharePort:0}")
    private Integer sharePort;

    /**
     * 单点登录接口 不包含应用名
     */
    @Value("${ace.config.sso.validateUrl:#{null}}")
    private String validateUrl;

    /**
     * 是否开启 单点登录
     */
    @Value("${ace.config.sso.openPlatformSso:false}")
    private boolean openPlatformSso;

    /**
     * 是否开启 单点登录
     */
    @Value("${ace.config.sso.open:false}")
    private boolean openSso;

    /**
     * 单点登录接口
     */
    @Value("${ace.config.sso.completedValidateUrl:#{null}}")
    private String completedValidateUrl;

    @Autowired
    InitService initService;

    /**
     * 是否指定单点登录接口对应的APP
     */
    @Value("${ace.config.sso.onlyOneApp:false}")
    private boolean onlyOneApp;

    /**
     * 单点登录接口对应的APP
     */
    @Value("${ace.config.sso.appName:#{null}}")
    private String theApp;

    /**
     * 单点登录接口要跳转的目标APP
     */
    @Value("${ace.config.sso.targetAppName:#{null}}")
    private String targetApp;

    @Autowired
    HttpClient client;

    /**
     * 获取系统默认配置参数
     *
     * @return
     * @author yansiyang
     * @date 2019/7/19 9:22
     */
    @ApiOperation(value = "获取系统默认配置参数", httpMethod = "GET", notes = "获取系统默认配置参数")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R getDefaultArgs() {

        R r = R.ok();
        String defaultSecretLevel = cacheUtil.get("defaultSecretLevel");
        if (StringUtils.isNotBlank(defaultSecretLevel)) {
            r.put("defaultSecretLevel", Integer.parseInt(defaultSecretLevel));
        }
        String defaultLanguage = cacheUtil.get("defaultLanguage");
        String defaultTokenExpireDay = cacheUtil.get("defaultTokenExpireDay");
        if (StringUtils.isNotBlank(defaultTokenExpireDay)) {
            r.put("defaultTokenExpireDay", Integer.parseInt(defaultTokenExpireDay));
        }
        String platformName = cacheUtil.get("platformName");
        if (StringUtils.isNotBlank(platformName)) {
            r.put("platformName", platformName);
        }
        if (Constants.isMonomerApp && !Constants.isZuulApp) {
            r.put("monoFlag", 1);
            r.put("monoAppName", appName);
            r.put("socketPort", sharePort > 0 ? sharePort : socketPort);
        }
        if (Constants.isZuulApp) {
            r.put("zuulFlag", 1);
            r.put("monoFlag", 1);
            r.put("socketPort", sharePort > 0 ? sharePort : socketPort);
        }
        if (openPlatformSso) {
            r.put("openSso", openSso ? 1 : 0);
            r.put("completedValidateUrl", completedValidateUrl);
        }
        return r
                .put("defaultLanguage", defaultLanguage).put("isDevState", isDevState);
    }

    /**
     * 获取系统默认配置参数
     *
     * @return
     * @author yansiyang
     * @date 2019/7/19 9:22
     */
    @ApiOperation(value = "获取系统默认配置参数", httpMethod = "GET", notes = "获取系统默认配置参数")
    @RequestMapping(value = "/ssoRequest", method = RequestMethod.GET)
    public R ssoRequest() {
        R r = R.ok();
        if (openSso && StringUtils.isNotBlank(validateUrl) && StringUtils.isNotBlank(theApp)) {
            String addr ;
            if (Constants.isZuulApp) {
                addr = "http://127.0.0.1:" + port + "/" + theApp;
            }else if (Constants.isMonomerApp) {
                addr = "http://127.0.0.1:" + port;
            }
            else {
                addr = client.getAppAddr(theApp);
            }
            if (StringUtils.isNotBlank(addr)) {
                String url = addr + validateUrl;
                logger.info("*************************[ SSO ]*******************************");
                logger.info("SSO-VALIDATEURL: " + url);
                String userKey = client.clientWithCookie(url, securityUtils.getCookies());
                logger.info("*************************[ SSO ]*******************************");
                logger.info("SSO-USERKEY: " + userKey);
                if (StringUtils.isNotBlank(userKey)) {
                    String token = null;
                    R rr = sysUserService.getTokenAfterLogin(userKey);
                    if (rr != null) {
                        token = (String) rr.get("token");
                    }
                    logger.info("*************************[ SSO ]*******************************");
                    logger.info("SSO-TOKEN: " + token);
                    if (StringUtils.isNotBlank(token)) {
                        boolean isAdmin = false;
                        try {
                            isAdmin = (boolean) rr.get("isAdmin");
                        } catch (Exception e) {
                            try {
                                isAdmin = Boolean.parseBoolean(JsonUtils.castObject(rr.get("isAdmin"), String.class));
                            } catch (Exception e1) {

                            }
                        }
                        r.put("isAdmin", isAdmin);
                        if (isAdmin) {
                            r.put("isAdminFlag", 1);
                        } else {
                            r.put("isAdminFlag", 0);
                            r.put("targetApp", StringUtils.isNotBlank(targetApp) ? targetApp : theApp);
                        }
                        r.put("token", token);
                        return r;
                    }
                }
            }
        }
        return R.error();
    }

    /**
     * 接收前台传来的初始化参数
     *
     * @param map 参数键值对
     * @return
     * @author yansiyang
     * @date 2019/4/12 11:21
     */
    @ApiOperation(value = "系统初始化", httpMethod = "POST", notes = "系统初始化")
    @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Map")
    @RequestMapping(value = "", method = RequestMethod.POST)
    //@AceAuth("系统初始化")
    public R init(@RequestBody Map<String, String> map) {
//        int count = sysConfigService.count(new QueryWrapper<SysConfigDO>().eq("name",
//                "platform_secret_key"));
//        if (count == 0) {
//            return R.error("请先上传密钥文件！");
//        }
        return initService.init(map);
    }

    @ApiOperation(value = "上传授权文件", httpMethod = "POST", notes = "上传授权文件")
    @ApiImplicitParam(name = "file", value = "file", required = true, dataType = "MultipartFile")
    @RequestMapping(value = "/action/upload", method = RequestMethod.POST)
    public R upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "授权文件"));
        }
        String str;
        try {
            InputStream input = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            str = bufferedReader.readLine();
            bufferedReader.close();
            input.close();
            inputStreamReader.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
        if (StringUtils.isNotBlank(str)) {
            if (!JsonUtils.isJson(str)) {
                return R.error(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
            }
            if (initService.updateSecretKey(str)) {
                return R.ok();
            }
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    @ApiOperation(value = "撤回授权文件", httpMethod = "DELETE", notes = "撤回授权文件")
    @RequestMapping(value = "/action/upload", method = RequestMethod.DELETE)
    public R delete() {
        if (initService.recallSecretKey()) {
            return R.ok();
        }
        return R.error();
    }
}
