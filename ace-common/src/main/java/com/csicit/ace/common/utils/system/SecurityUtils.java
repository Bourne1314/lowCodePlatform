package com.csicit.ace.common.utils.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.vo.SysOnlineUserVO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IpUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 安全方面公共接口工具类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Component
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class SecurityUtils {
    public static final int TOKEN_ERROR_CODE = 40001;
    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    @Value("${spring.application.name}")
    private String appName;
    @Value("${ace.config.test-open-flg:false}")
    private boolean testOpenFLg;

    @Value("${ace.config.cache.type:redis}")
    private String cacheType;


    /**
     * 系统支持的最低密级
     *
     * @param null
     * @return
     * @author JonnyJiang
     * @date 2019/5/27 11:54
     */

    public static final int MIN_SECRET_LEVEL = 5;

    public static String TEST_TOKEN = "";

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;


    /**
     * 获取在线用户主键
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/11 17:56
     */
    public Set<SysOnlineUserVO> getOnlineUsers() {
        String groupId = getCurrentGroupId();
        try {
            Set<String> keys = new HashSet<>();
            if (Objects.equals(cacheType, "redis")) {
                keys = cacheUtil.getKeys("*aceuserid");
            } else {
                keys = cacheUtil.getKeys(".*aceuserid");
            }
            Set<String> userIds = new HashSet<>();
            Map<String, SysOnlineUserVO> sysOnlineUserMap = new HashMap<>();
            for (String key : keys) {
                try {
                    SysOnlineUserVO sysOnlineUserVO = JsonUtils.castObject(cacheUtil.get(key), SysOnlineUserVO.class);
                    String userId = sysOnlineUserVO.getUserId();
                    if (Objects.equals(groupId, sysOnlineUserVO.getGroupId())) {
                        if (!userIds.contains(userId)) {
                            userIds.add(userId);
                            sysOnlineUserMap.put(userId, sysOnlineUserVO);
                        } else {
                            SysOnlineUserVO oldUserVo = sysOnlineUserMap.get(userId);
                            // 取最新登录时间
                            if (oldUserVo.getLoginTime().isBefore(sysOnlineUserVO.getLoginTime())) {
                                sysOnlineUserMap.put(userId, sysOnlineUserVO);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("getOnlineUsers [ERROR]: " + key);
                }
            }
            Set<SysOnlineUserVO> sysOnlineUserVOS = new TreeSet<>();
            sysOnlineUserVOS.addAll(sysOnlineUserMap.values());
            return sysOnlineUserVOS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException("Redis异常");
        }
    }

    /**
     * 获取访问IP
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/4 15:35
     */
    public String getRealIp() {
        String ip = IpUtils.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            ip = IpUtils.getFirstIpAddress(request);
        }
        return ip;
    }

    /**
     * 获取当前会话
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * 获取 前台传来的值 Headers params cookie
     */
    public String getValue(String key) {
        try {
            String value = request.getHeader(key);
            if (StringUtils.isBlank(value)) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (Objects.equals(cookie.getName(), key)) {
                            value = cookie.getValue();
                            break;
                        }
                    }
                }
            }
            if (StringUtils.isBlank(value)) {
                value = request.getParameter(key);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 制作cookies
     *
     * @return
     * @author yansiyang
     * @date 2019/9/28 16:50
     */
    public List<String> getCookies() {
        List<String> cookieList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        logger.info("修改之前的Cookie: " + JSONObject.toJSONString(cookies));
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieList.add(cookie.getName() + "=" + cookie.getValue());
            }
        }
        Enumeration er = request.getHeaderNames();
        while (er.hasMoreElements()) {
            String name = (String) er.nextElement();
            String value = request.getHeader(name);
            cookieList.add(name + "=" + value);
        }
        logger.info("修改之后的Cookie: " + JSONObject.toJSONString(cookieList));
        return cookieList;
    }
    /***
     * @description: 从Cookie里获取token的值
     * @params: getTokenFrom Cookie
     * @return: java.util.List<java.lang.String>
     * @author: Zhangzhaojun
     * @time: 2021/12/3 8:55
     */
    public String getTokenFromCookie() {
        HashMap<String,String> cookieList = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        logger.info("修改之前的Cookie: " + JSONObject.toJSONString(cookies));
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieList.put(cookie.getName(),cookie.getValue());
            }
        }
        Enumeration er = request.getHeaderNames();
        while (er.hasMoreElements()) {
            String name = (String) er.nextElement();
            String value = request.getHeader(name);
            cookieList.put(name,value);
        }
        logger.info("修改之后的Cookie: " + JSONObject.toJSONString(cookieList));
        return cookieList.get("token");
    }

    /**
     * 获取token
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public String getToken() {
        if (testOpenFLg) {
            return TEST_TOKEN;
        }
        //从header中获取token
        logger.debug("----------------------request------------------------");
        logger.debug(JSONObject.toJSONString(request.getHeaderNames()));
        String token = request.getHeader("token");
        logger.debug("HEADER: " + token);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token) || Objects.equals("null", token)) {
            token = request.getParameter("token");
            logger.debug("QUERY: " + token);
        }
        //如果参数中不存在token，则从cookie中获取token
        if (StringUtils.isBlank(token) || Objects.equals("null", token)) {
            Cookie[] cookies = request.getCookies();
            logger.debug("----------------------cookie------------------------");
            logger.debug(JSONObject.toJSONString(cookies));
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Objects.equals(cookie.getName(), "token")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isBlank(token) || Objects.equals("null", token)) {
            throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
        }
        try {
            return URLDecoder.decode(token, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
    }

    /**
     * 获取是否已登录
     *
     * @return 是否已登录
     * @author JonnyJiang
     * @date 2020/7/13 11:13
     */

    public boolean hasLoggedIn() {
        try {
            getCurrentUser();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取当前用户ID
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/16 17:40
     */
    public String getCurrentUserId() {
        String token = getToken();
//        try {
//            JSONObject json = GMBaseUtil.decryptToken(token);
//            return json.getString("userId");
//        } catch (Exception e) {
//
//        }
        String userId = cacheUtil.get(token + "userid");
        if (StringUtils.isNotBlank(userId)) {
            return userId;
        }
        return getCurrentUser().getId();
    }

    /**
     * 获取当前用户名
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/16 17:40
     */
    public String getCurrentUserName() {
        String token = getToken();
        try {
            JSONObject json = GMBaseUtil.decryptToken(token);
            return json.getString("userName");
        } catch (Exception e) {

        }
        return getCurrentUser().getUserName();
    }

    /**
     * 获取当前用户
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public SysUserDO getCurrentUser() {
        String token = getToken();
        return getUserByToken(token);
    }

    /**
     * 根据token获取用户信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:23
     */
    public SysUserDO getUserByToken(String token) {
        if (token != null && StringUtils.isNotBlank(token)) {
            Object user = cacheUtil.hget(token, "user");
            SysUserDO sysUserDO = JsonUtils.castObject(user, SysUserDO.class);
            if (sysUserDO == null) {
                // 用户信息丢失 关系型数据库查询 重新放入缓存数据库
                // token 失效  重新登录
                throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
            }
            return sysUserDO;
        }
        // token为空 重新登录
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取当前用户  主职  部门信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public OrgDepartmentDO getDept() {
        String token = getToken();
        if (token != null && StringUtils.isNotBlank(token)) {
            OrgDepartmentDO dep = JsonUtils.castObject(cacheUtil.hget(token, "department"), OrgDepartmentDO.class);
            return dep;
        }
        // token为空 重新登录
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取当前用户所属集团ID
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public OrgGroupDO getCurrentGroup() {
        List<OrgGroupDO> list = getCurrentGroups();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return getCurrentGroups().get(0);
        }
        return null;
    }

    /**
     * 获取当前用户所属集团ID
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public String getCurrentGroupId() {
        OrgGroupDO group = getCurrentGroup();
        if (group != null) {
            return group.getId();
        }
        return null;
    }

    /**
     * 获取当前用户涉及的集团信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public List<OrgGroupDO> getCurrentGroups() {
        String token = getToken();
        if (token != null && StringUtils.isNotBlank(token)) {
            JSONArray groups = JsonUtils.castObject(cacheUtil.hget(token, "groups"), JSONArray.class);
            if (groups == null || groups.size() == 0) {
                return new ArrayList<>();
            }
            List<OrgGroupDO> list = new ArrayList<>();
            groups.stream().forEach(group -> {
                list.add(JsonUtils.castObject(group, OrgGroupDO.class));
            });
            return list;
        }
        // token为空 重新登录
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取当前用户  主职  部门主键
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public String getDeptID() {
        OrgDepartmentDO dep = getDept();
        if (dep == null) {
            return null;
        }
        return dep.getId();
    }

    /**
     * 获取当前用户  主职  部门名称
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public String getDeptName() {
        return getDept().getName();
    }

    /**
     * 获取当前用户角色信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:24
     */
    public List<String> getRoleIds() {
        String token = getToken();
        if (token != null && StringUtils.isNotBlank(token)) {
            List<String> roleIds = (List<String>) cacheUtil.hget(token, "roleIds");
            return roleIds;
        }
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取当前用户对于当前应用的所有Api信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:25
     */
    public List<String> getApis() {
        String token = getToken();
        if (token != null && StringUtils.isNotBlank(token)) {
            JSONArray jsonArray = JsonUtils.castObject(cacheUtil.hget(token, "apis"), JSONArray.class);
            if (jsonArray == null || jsonArray.size() == 0) {
                return new ArrayList<>();
            }
            List<String> apis = new ArrayList<>();
            jsonArray.forEach(api -> {
                apis.add((String) api);
            });
            return apis;
        }
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取文件密级列表
     *
     * @param
     * @return java.util.List<com.csicit.ace.common.utils.system.SecretLevel>
     * @author JonnyJiang
     * @date 2019/9/27 9:36
     */

    public List<SecretLevel> listAllFileSecretLevel() {
        List<SecretLevel> secretLevels = new ArrayList<>();
        secretLevels.add(new SecretLevel(5, "非密"));
        secretLevels.add(new SecretLevel(4, "内部"));
        secretLevels.add(new SecretLevel(3, "秘密"));
        secretLevels.add(new SecretLevel(2, "机密"));
        secretLevels.add(new SecretLevel(1, "绝密"));
        return secretLevels;
    }

    /**
     * 根据文件密级标识获取文件密级
     *
     * @param secretLevel 密级标识
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/6/4 14:21
     */

    public String getFileSecretLevelText(Integer secretLevel) {
        List<SecretLevel> secretLevels = listAllFileSecretLevel();
        Optional<SecretLevel> optionalSecretLevel = secretLevels.stream().filter(o -> o.equals(secretLevel))
                .findFirst();
        if (optionalSecretLevel.isPresent()) {
            return optionalSecretLevel.get().getName();
        }
        return "未知";
    }

    /**
     * 获取可用文件密级列表
     *
     * @return List<com.csicit.ace.common.utils.system.SecretLevel>
     * @author JonnyJiang
     * @date 2019/5/27 11:33
     */

    public List<SecretLevel> listAvailableFileSecretLevel() {
        SysUserDO user = getCurrentUser();
        Integer userSecretLevel = user.getSecretLevel() == null ? Integer.valueOf(MIN_SECRET_LEVEL) : user
                .getSecretLevel();
        return listAvailableFileSecretLevel(userSecretLevel);
    }

    /**
     * 获取可用文件密级列表
     *
     * @param secretLevel 密级
     * @return 可用文件密级列表
     * @author JonnyJiang
     * @date 2020/7/14 11:17
     */

    public List<SecretLevel> listAvailableFileSecretLevel(Integer secretLevel) {
        List<SecretLevel> secretLevels = listAllFileSecretLevel();
        Integer maxFileSecretLevel = getMaxFileSecretLevel();
        return secretLevels.stream().filter(o ->
                compareSecretLevel(o.getValue(), secretLevel) <= 0
                        && compareSecretLevel(o.getValue(), maxFileSecretLevel) <= 0)
                .collect(Collectors.toList());
    }

    /**
     * 获取应用系统支持的最高文件密级
     *
     * @return java.lang.Integer
     * @author JonnyJiang
     * @date 2019/5/27 11:43
     */

    public Integer getMaxFileSecretLevel() {
        // 获取应用系统的最高密级
        Integer appMaxSecretLevel = getAppMaxSecretLevel();
        Integer maxSecretLevel = getMaxSecretLevel();
        Integer result = compareSecretLevel(appMaxSecretLevel, maxSecretLevel);
        if (result < 0) {
            return appMaxSecretLevel;
        } else {
            return maxSecretLevel;
        }
    }

    /**
     * 获取应用最高密级
     *
     * @return java.lang.Integer
     * @author JonnyJiang
     * @date 2019/5/27 11:45
     */

    public Integer getAppMaxSecretLevel() {
        // 此处需要增加获取应用最高密级的代码
        return 1;
    }

    /**
     * 比较密级
     *
     * @param secretLevel1
     * @param secretLevel2
     * @return 大于0，则表示密级1比密级2大，等于0，则表示密级1与密级2相同，小于0，则表示密级1比密级2小
     */
    public static Integer compareSecretLevel(Integer secretLevel1, Integer secretLevel2) {
        if (secretLevel1 == null) {
            secretLevel1 = MIN_SECRET_LEVEL;
        }
        if (secretLevel2 == null) {
            secretLevel2 = MIN_SECRET_LEVEL;
        }
        return secretLevel2 - secretLevel1;
    }

    /**
     * 获取应用名称
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/7/10 8:19
     */

    public String getAppName() {
        return appName;
    }

    /**
     * 当前token是否是系统管理员
     *
     * @return
     */
    public boolean isAdmin() {
        String token = getToken();
        if (token != null && StringUtils.isNotBlank(token)) {
            Object isAdmin = cacheUtil.hget(token, "isAdmin");
            if (Objects.isNull(isAdmin)) {
                return false;
            }
            Integer admin = JsonUtils.castObject(isAdmin, Integer.class);
            return admin == 0 ? false : true;
        }
        // token为空 重新登录
        throw new RException(InternationUtils.getInternationalMsg("ERROR_TO_VALIDATE_TOKEN"), TOKEN_ERROR_CODE);
    }

    /**
     * 获取系统支持的最高密级
     *
     * @return 系统支持的最高密级
     * @author JonnyJiang
     * @date 2019/12/2 17:43
     */

    public Integer getMaxSecretLevel() {
        return Integer.parseInt(cacheUtil.get("defaultSecretLevel"));
    }
}