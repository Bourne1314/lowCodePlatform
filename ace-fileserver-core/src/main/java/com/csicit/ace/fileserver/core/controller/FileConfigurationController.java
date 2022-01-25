package com.csicit.ace.fileserver.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.domain.FileReviewDO;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecretLevel;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.service.FileReviewService;
import com.csicit.ace.fileserver.core.utils.AcceptUtil;
import com.csicit.ace.fileserver.core.utils.ConfigurationType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 附件管理-附件配置
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@RestController
@Api("附件管理-附件配置")
@RequestMapping("/fileConfiguration")
public class FileConfigurationController {
    public static final String APP_ID = "appName";
    @Autowired
    FileConfigurationService fileConfigurationService;
    @Autowired
    FileRepositoryService fileRepositoryService;
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    CacheUtil cacheUtil;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    FileReviewService fileReviewService;
    /**
     * 附件打包下载信息在Redis中的过期时间
     */
    public static final int DOWNLOAD_TOKEN_CONFIG_EXPIRE = 600;

    /**
     * 获取附件配置及附件列表
     *
     * @param params
     * @return 附件配置及附件列表
     * @author JonnyJiang
     * @date 2019/5/22 17:54
     */

    @CrossOrigin
    @ApiOperation(value = "获取附件配置及附件列表")
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public R load(@RequestBody Map<String, Object> params) {
        try {
            String configurationKey = (String) params.get("configurationKey");
            String formId = (String) params.get("formId");
            String appId = (String) params.get(APP_ID);
            FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
            List<FileInfoDO> files = fileInfoService.listByFormId(configuration, formId);
            //如果是附件审查流程，则将附件审查位全部置为0；
//            String flowCode = configuration.getFlowCode();
//            if(StringUtils.isNotBlank(flowCode))
//            {
//                if(flowCode.equals("fileReview")){
//                for(FileInfoDO fileInfoDO:files){
//                    fileInfoDO.setNeedReview(0);
//                }
//            }
//            }
//            else
//            {
//                System.out.println("lalaland");
//            }
            String downloadToken = formId;
            if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
                downloadToken = UUID.randomUUID().toString();
                cacheUtil.set(downloadToken, formId, DOWNLOAD_TOKEN_CONFIG_EXPIRE);
            }
            // 处理支持的文件类型
            configuration.setAccept(AcceptUtil.resolve(configuration.getAccept()));
            List<SecretLevel> availableFileSecretLevels = new ArrayList<>();
            if (securityUtils.hasLoggedIn()) {
                availableFileSecretLevels = securityUtils.listAvailableFileSecretLevel();
            } else {
                if (IntegerUtils.isTrue(configuration.getAllowDownloadWithoutLogin())) {
                    availableFileSecretLevels = securityUtils.listAvailableFileSecretLevel(SecurityUtils
                            .MIN_SECRET_LEVEL);
                }
            }
            return R.ok().put("fileConfiguration", configuration)
                    .put("fileInfos", files)
                    .put("secretLevels", availableFileSecretLevels)
                    .put("allSecretLevels", securityUtils.listAllFileSecretLevel())
                    .put("downloadToken", downloadToken);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }


    /**
     * 附件审查流程请求的load地址只需要传formId
     *
     * @param formId
     * @return 附件配置及附件列表
     * @author Zhangzhaojun
     * @date 2021/11/26 17:54
     */

    @CrossOrigin
    @ApiOperation(value = "获取附件配置及附件列表")
    @RequestMapping(value = "/loadReviewFile", method = RequestMethod.POST)
    public R loadReviewFile(@RequestParam String formId) {
        try {
//            String configurationKey = (String) params.get("configurationKey");
            FileConfigurationDO configurationDO = fileConfigurationService.getOne(new QueryWrapper<FileConfigurationDO>().eq("FLOW_CODE","fileReview"));
            String configurationKey = configurationDO.getConfigurationKey();
            String appId = configurationDO.getAppId();
            FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
            List<FileInfoDO> files = fileInfoService.listByFormId(configuration, formId);
            //如果是附件审查流程，则将附件审查位全部置为0；
//            if(flowCode.equals("fileReview")){
//                for(FileInfoDO fileInfoDO:files){
//                    fileInfoDO.setNeedReview(0);
//                }
//            }
            List<FileInfoDO> fileInfoDOS = files.stream().filter(f->f.getNeedReview()==2).collect(Collectors.toList());
            String downloadToken = formId;
            if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
                downloadToken = UUID.randomUUID().toString();
                cacheUtil.set(downloadToken, formId, DOWNLOAD_TOKEN_CONFIG_EXPIRE);
            }
            // 处理支持的文件类型
            configuration.setAccept(AcceptUtil.resolve(configuration.getAccept()));
            List<SecretLevel> availableFileSecretLevels = new ArrayList<>();
            if (securityUtils.hasLoggedIn()) {
                availableFileSecretLevels = securityUtils.listAvailableFileSecretLevel();
            } else {
                if (IntegerUtils.isTrue(configuration.getAllowDownloadWithoutLogin())) {
                    availableFileSecretLevels = securityUtils.listAvailableFileSecretLevel(SecurityUtils
                            .MIN_SECRET_LEVEL);
                }
            }
            return R.ok().put("fileConfiguration", configuration)
                    .put("fileInfos", fileInfoDOS)
                    .put("secretLevels", availableFileSecretLevels)
                    .put("allSecretLevels", securityUtils.listAllFileSecretLevel())
                    .put("downloadToken", downloadToken);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取附件配置
     *
     * @param params configurationKey、appName
     * @return 附件配置
     * @author JonnyJiang
     * @date 2020/6/22 16:12
     */

    @CrossOrigin
    @ApiOperation(value = "获取附件配置")
    @RequestMapping("/feignLoadByKey")
    public FileConfiguration feignLoadByKey(@RequestBody Map<String, Object> params) {
        String configurationKey = (String) params.get("configurationKey");
        String appId = (String) params.get(APP_ID);
        FileConfigurationDO fileConfigurationDO = fileConfigurationService.loadByKey(configurationKey, appId);
        return fileConfigurationDO.toFileConfiguration();
    }

    /**
     * 附件审查结束
     *
     * @param formId
     * @return 附件配置
     * @author Zhangzhaojun
     * @date 2021/11/18 16:12
     */

    @CrossOrigin
    @ApiOperation(value = "审查附件结束")
    @RequestMapping("/setFileReview")
    public void setFileReview(@RequestParam String formId) {
        FileReviewDO fileReviewDO = fileReviewService.getOne(new QueryWrapper<FileReviewDO>().eq("ID",formId));
        //根据审查Id获取审查的文件信息
        String fileIds = fileReviewDO.getFileInfoId().replaceAll(" ","");
        String idSub = fileIds.substring(1,fileIds.length()-1);
        String idsArray[] = idSub.split(",");
        List<String> idsList = Arrays.asList(idsArray);
        List<FileInfoDO> fileList = fileInfoService.list(new QueryWrapper<FileInfoDO>().in("ID",idsList));
        for(FileInfoDO fileInfoDO:fileList){
            fileInfoDO.setNeedReview(0);
            fileInfoService.updateById(fileInfoDO);
        }

            List<FileInfoDO> fileInfoDOList1 = fileInfoService.list(new QueryWrapper<FileInfoDO>().in("FORM_ID",formId));
        for(FileInfoDO fileInfoDO:fileInfoDOList1){
            fileInfoDO.setNeedReview(0);
            fileInfoService.updateById(fileInfoDO);
        }
            //更新fileReview表
            List<FileReviewDO> fileReviewDOS = fileReviewService.list(new QueryWrapper<FileReviewDO>().eq("ID",formId));
            for(FileReviewDO fileReviewDO1:fileReviewDOS){
                fileReviewDO1.setReviewStatus(0);
                fileReviewDO1.setReviewer(securityUtils.getCurrentUserName());
                fileReviewService.updateById(fileReviewDO1);
            }
    }

    /**
     * 根据附件配置项id获取单个附件配置项数据
     *
     * @param id 对象主键
     * @return 单个应用对象
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个附件配置项", httpMethod = "GET", notes = "根据附件配置项id获取单个附件配置项数据")
    @AceAuth("获取单个附件配置项")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        FileConfigurationDO instance = fileConfigurationService.getById(id);
        if (StringUtils.isNotBlank(instance.getFileRepositoryId())) {
            FileRepositoryDO fileRepositoryDO = fileRepositoryService.getById(instance.getFileRepositoryId());
            if (fileRepositoryDO != null) {
                instance.setRepositoryKey(fileRepositoryDO.getRepositoryKey());
            }
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 查询附件配置项列表并分页
     *
     * @param params 请求参数map对象
     * @return 角色列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "查询附件配置项列表并分页", httpMethod = "GET", notes = "查询附件配置项列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("查询附件配置项列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<FileConfigurationDO> page = new Page<>(current, size);
        Integer configurationType = Integer.parseInt((String) params.get("configurationType"));
        String appId = (String) params.get("appId");
        String groupId = (String) params.get("groupId");
        String searching = (String) params.get("searching");
        IPage list = null;
        if (ObjectUtils.compare(configurationType, ConfigurationType.Tenant.ordinal()) == 0) {
            list = fileConfigurationService.page(page, new QueryWrapper<FileConfigurationDO>().like
                    ("CONFIGURATION_KEY", searching).eq
                    ("configuration_type", ConfigurationType.Tenant.ordinal()));
        } else if (ObjectUtils.compare(configurationType, ConfigurationType.Group.ordinal()) == 0 && StringUtils
                .isNotBlank(groupId)) {
            list = fileConfigurationService.page(page, new QueryWrapper<FileConfigurationDO>().like
                    ("CONFIGURATION_KEY", searching).eq("group_id",
                    groupId).eq("configuration_type", ConfigurationType.Group.ordinal()));
        } else if (ObjectUtils.compare(configurationType, ConfigurationType.App.ordinal()) == 0 && StringUtils
                .isNotBlank(appId) && StringUtils.isNotBlank(groupId)) {
            list = fileConfigurationService.page(page, new QueryWrapper<FileConfigurationDO>().like
                    ("CONFIGURATION_KEY", searching).eq("group_id",
                    groupId).eq("app_id", appId).eq("configuration_type", ConfigurationType.App.ordinal()));
        }
        return R.ok().put("page", list);
    }

    /**
     * 保存附件配置项
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "保存附件配置项", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "FileConfigurationDO")
    @AceAuth("保存附件配置项")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody FileConfigurationDO instance) {
        instance.setUploadAuthCode(instance.getUploadOperationKey());
        instance.setDeleteAuthCode(instance.getDeleteOperationKey());
        instance.setDownloadAuthCode(instance.getDownloadOperationKey());
        return fileConfigurationService.insert(instance);
    }

    /**
     * 修改附件配置项
     *
     * @param instance 对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "修改附件配置项", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "FileConfigurationDO")
    @AceAuth("修改附件配置项")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody FileConfigurationDO instance) {
        instance.setUploadAuthCode(instance.getUploadOperationKey());
        instance.setDeleteAuthCode(instance.getDeleteOperationKey());
        instance.setDownloadAuthCode(instance.getDownloadOperationKey());
        return fileConfigurationService.update(instance);
    }

    /**
     * 删除附件配置项
     *
     * @param ids ID数组
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "删除附件配置项", httpMethod = "DELETE")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除附件配置项")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        return fileConfigurationService.delete(ids);
    }
}