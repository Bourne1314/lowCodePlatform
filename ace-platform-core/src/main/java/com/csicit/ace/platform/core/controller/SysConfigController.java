package com.csicit.ace.platform.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.enums.PlatformImgsEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 系统配置项 接口访问层
 *
 * @author generator
 * @Descruption 系统配置项
 * @date 2019-04-11 14:36:21
 * @Modified By
 */

@RestController
@RequestMapping("/sysConfigs")
@Api("系统配置项管理")
public class SysConfigController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysConfigController.class);

    /**
     * 上传背景图、logo等
     *
     * @param type
     * @param file
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiImplicitParam(name = "type", value = "图片类型", dataType = "String", required = true)
    @ApiOperation(value = "上传背景图、logo等", httpMethod = "POST", notes = "上传背景图、logo等")
    @AceAuth("上传背景图、logo等")
    @RequestMapping(value = "/action/uploadImg/{type}", method = RequestMethod.POST)
    public R uploadImg(@PathVariable("type") String type, @RequestParam("file") MultipartFile file) throws Exception {
        if (!Objects.equals(securityUtils.getCurrentUser().getUserName(), "admin")) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        if (StringUtils.isNotBlank(type)) {

            String imageName = PlatformImgsEnum.createNewImageName(type, file.getOriginalFilename());

            // 写入文件夹
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(PlatformImgsEnum.IMAGE_PATH +
                    File.separator + imageName));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();
            fileInputStream.close();

            // 写入数据库
            SysConfigDO config = new SysConfigDO();
            config.setId(PlatformImgsEnum.IMG_PREFIX + type);
            config.setScope(1);
            config.setName(PlatformImgsEnum.IMG_PREFIX + type);
            config.setValue(imageName);
            sysConfigService.saveOrUpdate(config);

            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));

            /**
             *  20200807 14:47
             *  之前的储存进redis的代码
             *
             byte[] data;
             // 转换成字符串存储
             try {
             InputStream inputStream = file.getInputStream();
             data = new byte[inputStream.available()];
             inputStream.read(data);
             inputStream.close();
             } catch (Exception e) {
             e.printStackTrace();
             logger.error(e.getMessage());
             return R.error(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
             }
             String str = PlatformImgsEnum.getStrByBytes(data);
             String id = PlatformImgsEnum.IMG_PREFIX + type;

             if (cacheUtil.hasKey(id)) {
             cacheUtil.rename(id, id + "-old", true);
             cacheUtil.rename(id + "-name", id + "-name" + "-old", true);
             }
             cacheUtil.set(id, str, CacheUtil.NOT_EXPIRE);
             cacheUtil.set(id + "-name", file.getOriginalFilename(), CacheUtil.NOT_EXPIRE);
             return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
             */

        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 撤回背景图、logo等
     *
     * @param name
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiImplicitParam(name = "name", value = "图片名称", dataType = "String", required = true)
    @ApiOperation(value = "删除背景图、logo等", httpMethod = "DELETE", notes = "删除背景图、logo等")
    @AceAuth("删除背景图、logo等")
    @RequestMapping(value = "/action/deleteImage/{name}", method = RequestMethod.DELETE)
    public R deleteImage(@PathVariable("name") String name) {
        if (!Objects.equals(securityUtils.getCurrentUser().getUserName(), "admin")) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        if (StringUtils.isNotBlank(name)) {
            /**
             *  20200807 14:47
             *  之前的储存进redis的代码
             *
             String id = PlatformImgsEnum.IMG_PREFIX + type + "-old";
             cacheUtil.delete(PlatformImgsEnum.IMG_PREFIX + type);
             cacheUtil.delete(PlatformImgsEnum.IMG_PREFIX + type + "-name");
             cacheUtil.rename(id, PlatformImgsEnum.IMG_PREFIX + type, true);
             cacheUtil.rename(id + "-name", PlatformImgsEnum.IMG_PREFIX + type + "-name", true);
             */
            File file = new File(PlatformImgsEnum.IMAGE_PATH + File.separator + name);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 撤回背景图、logo等
     *
     * @param name
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiImplicitParam(name = "name", value = "图片名称", dataType = "String", required = true)
    @ApiOperation(value = "设置背景图、logo等", httpMethod = "PUT", notes = "设置背景图、logo等")
    @AceAuth("设置背景图、logo等")
    @RequestMapping(value = "/action/setImage/{type}/{name}", method = RequestMethod.PUT)
    public R setImage(@PathVariable("name") String name, @PathVariable("type") String type) {
        if (!Objects.equals(securityUtils.getCurrentUser().getUserName(), "admin")) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        }
        if (StringUtils.isNotBlank(name)) {
            SysConfigDO config = new SysConfigDO();
            config.setId(PlatformImgsEnum.IMG_PREFIX + type);
            config.setScope(1);
            config.setName(PlatformImgsEnum.IMG_PREFIX + type);
            config.setValue(name);
            if (sysConfigService.saveOrUpdate(config)) {
                return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
            }
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 获取背景图、logo等图片名称列表
     *
     * @param type
     * @return
     * @author yansiyang
     * @date 2020/8/8 8:04
     */
    @ApiImplicitParam(name = "type", value = "图片类型", dataType = "String", required = true)
    @ApiOperation(value = "获取背景图、logo等图片名称列表", httpMethod = "GET", notes = "获取背景图、logo等图片名称列表")
    @AceAuth("获取背景图、logo等图片名称列表")
    @RequestMapping(value = "/action/listImages/{type}", method = RequestMethod.GET)
    public R listImages(@PathVariable("type") String type) {
        SysConfigDO configDO = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                .eq("id", PlatformImgsEnum.IMG_PREFIX + type).select("value", "id"));
        String name = configDO == null ? null : configDO.getValue();
        JSONArray imgList = new JSONArray();
        List<String> allList = PlatformImgsEnum.getImageList(type);
        List<String> defaultList = PlatformImgsEnum.getDefaultImgList(type);
        for (String fileName : allList) {
            JSONObject file = new JSONObject();
            file.put("fileName", fileName);
            file.put("isDefault", defaultList.contains(fileName) ? 1 : 0);
            file.put("isCurrent", Objects.equals(fileName, name) ? 1 : 0);
            imgList.add(file);
        }
        return R.ok().put("list", imgList);
    }

    /**
     * 获取背景图、logo等图片
     *
     * @param name
     * @return
     * @author yansiyang
     * @date 2020/8/8 8:04
     */
    @ApiImplicitParam(name = "type", value = "图片类型", dataType = "String", required = true)
    @ApiOperation(value = "获取背景图、logo等图片", httpMethod = "GET", notes = "获取背景图、logo等图片")
    @AceAuth("获取背景图、logo等图片")
    @RequestMapping(value = "/action/getImage/{name}", method = RequestMethod.GET)
    public void getImage(@PathVariable("name") String name, HttpServletResponse response) {
        if (StringUtils.isNotBlank(name) && new File(PlatformImgsEnum.IMAGE_PATH + File.separator + name).exists()) {
            try {
                InputStream inputStream = new FileInputStream(new File(PlatformImgsEnum.IMAGE_PATH + File.separator +
                        name));
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                response.setHeader("Context-Type", "application/xmsdownload");
                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + name);
                OutputStream os = response.getOutputStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 100];
                int ch;
                while ((ch = inputStream.read(buffer)) != -1) {
                    swapStream.write(buffer, 0, ch);
                }
                os.write(swapStream.toByteArray());
                os.close();
                swapStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 获取背景图、logo等
     *
     * @param type
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiImplicitParam(name = "type", value = "图片类型", dataType = "String", required = true)
    @ApiOperation(value = "获取背景图、logo等", httpMethod = "GET", notes = "获取背景图、logo等")
    @RequestMapping(value = "/action/downloadImg/{type}", method = RequestMethod.GET)
    public void downloadImg(@PathVariable("type") String type, HttpServletResponse response) {
        try {

            /**
             *  20200807 14:47
             *  之前的储存进redis的代码
             *
             if (!cacheUtil.hasKey(PlatformImgsEnum.IMG_PREFIX + type + "-name")) {
             cacheUtil.set(PlatformImgsEnum.IMG_PREFIX + type + "-name", PlatformImgsEnum.getImgName(type), CacheUtil
             .NOT_EXPIRE);
             }
             if (cacheUtil.hasKey(PlatformImgsEnum.IMG_PREFIX + type)) {
             str = cacheUtil.get(PlatformImgsEnum.IMG_PREFIX + type);
             name = cacheUtil.get(PlatformImgsEnum.IMG_PREFIX + type + "-name");
             } else {
             str = PlatformImgsEnum.getImgStr(type);
             name = PlatformImgsEnum.getImgName(type);
             cacheUtil.set(PlatformImgsEnum.IMG_PREFIX + type, str, CacheUtil.NOT_EXPIRE);
             cacheUtil.set(PlatformImgsEnum.IMG_PREFIX + type + "-name", name, CacheUtil.NOT_EXPIRE);
             }
             */
            if (StringUtils.isNotBlank(type)) {
                String name;
                InputStream inputStream;
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                response.setHeader("Context-Type", "application/xmsdownload");
                SysConfigDO configDO = sysConfigService.getOne(new QueryWrapper<SysConfigDO>()
                        .eq("id", PlatformImgsEnum.IMG_PREFIX + type).select("value", "id"));
                if (configDO != null && new File(PlatformImgsEnum.IMAGE_PATH + File.separator + configDO.getValue())
                        .exists()) {
                    name = configDO.getValue();
                } else {
                    name = PlatformImgsEnum.getDefaultImgName(type);
                    // 写入数据库
                    SysConfigDO config = new SysConfigDO();
                    config.setId(PlatformImgsEnum.IMG_PREFIX + type);
                    config.setScope(1);
                    config.setName(PlatformImgsEnum.IMG_PREFIX + type);
                    config.setValue(name);
                    sysConfigService.saveOrUpdate(config);
                }
                inputStream = new FileInputStream(new File(PlatformImgsEnum.IMAGE_PATH + File.separator + name));
                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + name);
                OutputStream os = response.getOutputStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 100];
                int ch;
                while ((ch = inputStream.read(buffer)) != -1) {
                    swapStream.write(buffer, 0, ch);
                }
                os.write(swapStream.toByteArray());
                os.close();
                swapStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
    }


    /**
     * 获取单个配置项
     *
     * @param id 配置项id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个配置项", httpMethod = "GET", notes = "获取单个配置项")
    @AceAuth("获取单个配置项")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            SysConfigDO instance = sysConfigService.getById(id);
            return R.ok().put("instance", instance);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
    }

    /**
     * 获取配置项列表
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 配置项列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取配置项列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取配置项列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<SysConfigDO> page = new Page<>(current, size);
        Integer scope = Integer.parseInt((String) params.get("scope"));
        String appId = (String) params.get("appId");
        String groupId = (String) params.get("groupId");
        IPage list = null;
//        List<SysUserRoleDO> roles =
//                sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", securityUtils.getCurrentUserId()));
        if (Objects.equals(scope, 1)) {
//            if (hasAdminRoles(roles, new String[]{"admin", "sec", "auditor"})) {
            list = sysConfigService.page(page, new QueryWrapper<SysConfigDO>().orderByAsc("sort_index").eq("scope",
                    1).notLike("name", "key")
                    .notLike("name", "adminIpAddressCheck")
                    .notLike("name", "retainGroupAndAppAdmin")
                    .notLike("name", "Password").notLike("name", "SecretLevel").notLike("id", "platformImg"));
//           }
        } else if (Objects.equals(scope, 2) && StringUtils.isNotBlank(groupId)) {
//            if (hasAdminRoles(roles, new String[]{"groupadmin", "groupsec", "groupauditor"})) {
            list = sysConfigService.page(page, new QueryWrapper<SysConfigDO>().orderByAsc("sort_index").eq
                    ("group_id", groupId).eq
                    ("scope", 2));
//            }
        } else if (Objects.equals(scope, 3) && StringUtils.isNotBlank(appId)) {
//            if (hasAdminRoles(roles, new String[]{"appadmin", "appsec", "appauditor"})) {
            list = sysConfigService.page(page, new QueryWrapper<SysConfigDO>().orderByAsc("sort_index").eq("app_id",
                    appId).eq("scope", 3));
//            }
        }
        return R.ok().put("page", list);
    }

    private boolean hasAdminRoles(List<SysUserRoleDO> roles, String[] roleIds) {
        return roles.stream().anyMatch(userRole -> Arrays.asList(roleIds).contains(userRole.getRoleId()));
    }

    /**
     * 保存配置项
     *
     * @param config 配置项对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存配置项", httpMethod = "POST")
    @ApiImplicitParam(name = "config", value = "配置项实体", required = true, dataType = "SysConfigDO")
    @AceAuth("保存配置项")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody SysConfigDO config) {
//        Integer scope = config.getScope();
//        List<SysUserRoleDO> roles =
//                sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
//                        .eq("user_id", securityUtils.getCurrentUserId()));
//        if (Objects.equals(scope, 1)) {
//            if (!hasAdminRoles(roles, new String[]{"admin", "sec", "auditor"})) {
//                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
//            }
//        } else if (Objects.equals(scope, 2) && StringUtils.isNotBlank(config.getGroupId())) {
//            if (!hasAdminRoles(roles, new String[]{"groupadmin", "groupsec", "groupauditor"})) {
//                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
//            }
//        } else if (Objects.equals(scope, 3) && StringUtils.isNotBlank(config.getAppId())) {
//            if (!hasAdminRoles(roles, new String[]{"appadmin", "appsec", "appauditor"})) {
//                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
//            }
//        }
//        return sysConfigService.saveConfig(config);
        return R.ok();
    }

    /**
     * 修改配置项
     *
     * @param config 配置项对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改配置项", httpMethod = "PUT")
    @ApiImplicitParam(name = "config", value = "配置项实体", required = true, dataType = "SysConfigDO")
    @AceAuth("修改配置项")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysConfigDO config) {
        Integer scope = config.getScope();
        List<SysUserRoleDO> roles =
                sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .eq("user_id", securityUtils.getCurrentUserId()));
        if (Objects.equals(scope, 1)) {
            if (!hasAdminRoles(roles, new String[]{"admin", "sec", "auditor"})) {
                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        } else if (Objects.equals(scope, 2) && StringUtils.isNotBlank(config.getGroupId())) {
            if (!hasAdminRoles(roles, new String[]{"groupadmin", "groupsec", "groupauditor", "groupsuperadmin", "businessadmin"})) {
                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        } else if (Objects.equals(scope, 3) && StringUtils.isNotBlank(config.getAppId())) {
            if (!hasAdminRoles(roles, new String[]{"appadmin", "appsec", "appauditor","appsuperadmin", "businessadmin"})) {
                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        }
        return sysConfigService.updateConfig(config);
    }

    /**
     * 删除配置项
     *
     * @param configIds
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除配置项", httpMethod = "DELETE")
    @ApiImplicitParam(name = "configIds", value = "配置项ID数组", required = true, allowMultiple = true, dataType
            = "String")
    @AceAuth("删除配置项")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody List<String> configIds) {
        //return sysConfigService.delete(configIds);
        return R.ok();
    }

    /**
     * 从redis获取管理员是否开启IP校验
     *
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "从redis获取管理员是否开启IP校验", httpMethod = "GET", notes = "从redis获取管理员是否开启IP校验")
    @AceAuth("从redis获取管理员是否开启IP校验")
    @RequestMapping(value = "/action/getAdminIpAddressCheckValue", method = RequestMethod.GET)
    public R getAdminIpAddressCheckValue() {
        SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name", "adminIpAddressCheck"));
        if (config == null) {
            sysConfigService.initSaveOrUpdateConfigByKey("adminIpAddressCheck", "stop", "管理员IP地址校验");
            return R.ok().put("adminIpAddressCheck", "stop");
        } else {
            return R.ok().put("adminIpAddressCheck", config.getValue());
        }
//        return R.ok().put("adminIpAddressCheck", cacheUtil.get("adminIpAddressCheck") == null ? "stop" : cacheUtil
//                .get("adminIpAddressCheck"));
    }

    /**
     * 修改管理员IP地址校验
     *
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "修改管理员IP地址校验", httpMethod = "POST", notes = "修改管理员IP地址校验")
    @AceAuth("修改管理员IP地址校验")
    @RequestMapping(value = "/action/updAdminIpAddressCheckValue", method = RequestMethod.POST)
    public R updAdminIpAddressCheckValue(@RequestBody Map<String, String> map) {
        // 值：stop/open
        String adminIpAddressCheck = map.get("adminIpAddressCheck");
        sysConfigService.initSaveOrUpdateConfigByKey("adminIpAddressCheck", adminIpAddressCheck, "管理员IP地址校验");
        cacheUtil.set("adminIpAddressCheck", adminIpAddressCheck, CacheUtil.NOT_EXPIRE);
        return R.ok();
    }

    /**
     * 获取是否保留集团应用三员
     *
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "获取是否保留集团应用三员", httpMethod = "GET", notes = "获取是否保留集团应用三员")
    @AceAuth("获取是否保留集团应用三员")
    @RequestMapping(value = "/action/getRetainGroupAndAppAdminValue", method = RequestMethod.GET)
    public R getRetainGroupAndAppAdminValue() {
        SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                "retainGroupAndAppAdmin"));
        if (config == null) {
            sysConfigService.initSaveOrUpdateConfigByKey("retainGroupAndAppAdmin", "1", "平台管理员组成  \"1\"：集团应用三员；\"2" +
                    "\"：集团单一管理员、应用单一管理员；\"3\"：单一业务管理员");
            return R.ok().put("retainGroupAndAppAdmin", "1");
        } else {
            return R.ok().put("retainGroupAndAppAdmin", config.getValue());
        }
    }

    /**
     * 设置是否保留集团应用三员
     *
     * @return
     * @author yansiyang
     * @date 2019/11/5 8:04
     */
    @ApiOperation(value = "设置是否保留集团应用三员", httpMethod = "POST", notes = "设置是否保留集团应用三员")
    @AceAuth("设置是否保留集团应用三员")
    @RequestMapping(value = "/action/updRetainGroupAndAppAdminValue", method = RequestMethod.GET)
    public R updRetainGroupAndAppAdminValue(@RequestParam Map<String, String> map) {
        // 获取当前用户名
        String adminName = securityUtils.getCurrentUserName();
        if (!Objects.equals("admin", adminName)) {
            return R.error("token无效，请确保使用租户管理员登录的token！！！");
        }
        // 值：true/false
        String retainGroupAndAppAdmin = map.get("retainGroupAndAppAdmin");
        sysConfigService.initSaveOrUpdateConfigByKey("retainGroupAndAppAdmin", retainGroupAndAppAdmin, "平台管理员组成  " +
                "\"1\"：集团应用三员；\"2\"：集团单一管理员、应用单一管理员；\"3\"：单一业务管理员");
        cacheUtil.set("retainGroupAndAppAdmin", retainGroupAndAppAdmin, CacheUtil.NOT_EXPIRE);
        return R.ok();
    }
}
