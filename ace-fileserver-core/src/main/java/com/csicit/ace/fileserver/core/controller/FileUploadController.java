package com.csicit.ace.fileserver.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileReviewDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.fileserver.core.DownloadExpcetion;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileReviewService;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.csicit.ace.fileserver.core.controller.FileConfigurationController.APP_ID;
import static java.util.stream.Collectors.toList;

/**
 * 附件管理-文件上传
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@RestController
@Api("附件管理-文件上传")
@RequestMapping("/fileUpload")
public class FileUploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
    public static final String FILE_CONFIGURATION_PREFIX = "FILE_CONFIGURATION_";
    private static final Integer FILE_CONFIGURATION_EXPIRE = 3600;
    private static final long DOWNLOAD_TOKEN_CONFIG_EXPIRE = 600;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private FileConfigurationService fileConfigurationService;
    @Autowired
    private SysAuthMixService sysAuthMixService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private FileReviewService fileReviewService;
    /**
     * 文件上传
     *
     * @param file    文件
     * @param request 请求
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/5/22 18:15
     */

    @CrossOrigin
    @ApiOperation(value = "文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R upload(@RequestParam MultipartFile file, HttpServletRequest request) {
        String configurationKey = request.getParameter("configurationKey");
        String appName = request.getParameter(APP_ID);
        String yfId = request.getParameter("yfId");
        FileConfigurationDO configuration = getFileConfiguration(configurationKey, yfId, appName);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        }
        Integer chunk = NumberUtils.createInteger(request.getParameter("chunk"));
        Long size = NumberUtils.createLong(request.getParameter("size"));
        Integer chunks = NumberUtils.createInteger(request.getParameter("chunks"));
        try {
            fileInfoService.upload(configuration, inputStream, chunk, yfId, size, chunks);
        } catch (Exception e) {
            e.printStackTrace();
            String fileId = fileInfoService.getActualFileId(configuration, yfId);
            fileInfoService.moveToRecycleBin(fileId);
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 文件上传
     *
     * @param file    文件
     * @param request 请求
     * @return com.csicit.ace.common.utils.server.R
     * @author Zhangzhaojun
     * @date 2021 11/21 18:15
     */
    @CrossOrigin
    @ApiOperation(value = "文件上传审查")
    @RequestMapping(value = "/uploadReview", method = RequestMethod.POST)
    public R uploadReview(@RequestParam  MultipartFile file, HttpServletRequest request) {
        String configurationKey = request.getParameter("configurationKey");
        String appName = request.getParameter(APP_ID);
        String yfId = request.getParameter("yfId");
        FileConfigurationDO configuration = getFileConfiguration(configurationKey, yfId, appName);
        String flowCode = fileConfigurationService.getById(configuration.getId()).getFlowCode();
        Integer needReview = null;
        if(configuration.getEnableReview()==1){
            String fileName = request.getParameter("name");
            //判断该附件是否审查过
            FileInfoDO fileInfoDO = fileInfoService.getOne(new QueryWrapper<FileInfoDO>().eq("ID",yfId));
            needReview = fileInfoDO.getNeedReview();
        }else{
            needReview = 0;
            String fileName = request.getParameter("name");
            List<FileInfoDO> fileInfoDOList = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName));
            List<String> lists = fileInfoDOList.stream().map(FileInfoDO::getId).collect(toList());
            //更新附件needReview字段
            UpdateWrapper<FileInfoDO> fds = new UpdateWrapper<FileInfoDO>();
            FileInfoDO fileInfoDO = new FileInfoDO();
            fds.set("IS_NEED_REVIEW",0);
            fds.in("ID",lists);
            fileInfoService.update(fileInfoDO,fds);
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        }
        Integer chunk = NumberUtils.createInteger(request.getParameter("chunk"));
        Long size = NumberUtils.createLong(request.getParameter("size"));
        Integer chunks = NumberUtils.createInteger(request.getParameter("chunks"));
        try {
            fileInfoService.upload(configuration, inputStream, chunk, yfId, size, chunks);
        } catch (Exception e) {
            e.printStackTrace();
            String fileId = fileInfoService.getActualFileId(configuration, yfId);
            fileInfoService.moveToRecycleBin(fileId);
            return R.error(e.getMessage());
        }
        return R.ok().put("needReview",needReview);
    }

    /**
     * 文件上传
     *
     * @param uploadFormId    旧表单FormId
     * @return com.csicit.ace.common.utils.server.R
     * @author Zhangzhaojun
     * @date 2021 11/21 18:21
     */
    @CrossOrigin
    @ApiOperation(value = "启动审查")
    @RequestMapping(value = "/reviewFile", method = RequestMethod.POST)
    public R reviewFile(@RequestParam String uploadFormId) {
        //获取旧formId中的文件
        List<FileInfoDO> fileInfoDOS = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("FORM_ID",uploadFormId));
        List<FileInfoDO> fileInfoDOList = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("OLD_FORM_ID",uploadFormId));
        for(Integer i = 0;i<fileInfoDOS.size();i++){
            for(Integer j = 0;j<fileInfoDOList.size();j++){
                if(fileInfoDOS.get(i).getFileName().equals(fileInfoDOList.get(j).getFileName())){
                    fileInfoDOS.remove(fileInfoDOS.get(i));
                }
            }
        }
        String newFormId = UUID.randomUUID().toString();
        //添加已过滤好的文件
        for(FileInfoDO fileInfoDO:fileInfoDOS){
            fileInfoDO.setNeedReview(2);
            fileInfoService.updateById(fileInfoDO);
        }
        List<String> fileIds = new ArrayList<>();
        for(FileInfoDO fileInfoDO:fileInfoDOS){
            fileIds.add(fileInfoDO.getId());
        }
        for(FileInfoDO fileInfoDO:fileInfoDOS){
            fileInfoDO.setId(null);
            fileInfoDO.setFormId(newFormId);
            fileInfoDO.setNeedReview(2);
            fileInfoDO.setOldFormId(uploadFormId);
            fileInfoService.save(fileInfoDO);
        }

        //在审查表里添加审查记录
        FileReviewDO fileReviewDO = new FileReviewDO();
        fileReviewDO.setId(newFormId);
        fileReviewDO.setFileInfoId(fileIds.toString());
        fileReviewDO.setReviewDate(new Date());
        fileReviewDO.setReviewStatus(1);
        fileReviewService.save(fileReviewDO);
        return R.ok().put("formId",newFormId);
    }



    private FileConfigurationDO getFileConfiguration(String configurationKey, String yfId, String appId) {
        if (StringUtils.isEmpty(configurationKey)) {
            throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
        }
        String redisKey = FILE_CONFIGURATION_PREFIX + yfId;
        FileConfigurationDO configuration = cacheUtil.get(redisKey, FileConfigurationDO.class);
        if (configuration == null) {
            configuration = fileConfigurationService.loadByKey(configurationKey, appId);
            FileInfoDO fi = fileInfoService.getActualFileInfo(configuration, yfId);
            configuration.setFileRepositoryId(fi.getFileRepositoryId());
            cacheUtil.set(redisKey, configuration, FILE_CONFIGURATION_EXPIRE);
        }
        if (StringUtils.isNotEmpty(configuration.getUploadAuthCode())) {
            if (!sysAuthMixService.hasAuthCodeWithUserId(securityUtils.getCurrentUserId(), configuration.getUploadAuthCode(), appId)) {
                throw new UploadExpcetion(LocaleUtils.getNoAccess(configuration.getUploadAuthCode()));
            }
        }
        return configuration;
    }

    @CrossOrigin
    @RequestMapping(value = "/feignUpload", method = RequestMethod.POST)
    public void feignUpload(@RequestBody Map<String, Object> params) throws Exception {
        String configurationKey = (String) params.get("configurationKey");
        String yfId = (String) params.get("yfId");
        String appId = (String) params.get(APP_ID);
        FileConfigurationDO configuration = getFileConfiguration(configurationKey, yfId, appId);
        Integer chunk = (Integer) params.get("chunk");
        Long size = Long.valueOf(String.valueOf(params.get("size")));
        Integer chunks = (Integer) params.get("chunks");
        byte[] bytes = Base64.getDecoder().decode(((String) params.get("bytes")));
        fileInfoService.upload(configuration, bytes, chunk, yfId, size, chunks);
    }
}