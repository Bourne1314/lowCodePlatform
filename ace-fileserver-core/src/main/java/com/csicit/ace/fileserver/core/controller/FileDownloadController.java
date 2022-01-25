package com.csicit.ace.fileserver.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.fileserver.core.DownloadExpcetion;
import com.csicit.ace.fileserver.core.PreviewException;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.csicit.ace.fileserver.core.controller.FileConfigurationController.APP_ID;

/**
 * 附件管理-文件下载
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@RestController
@Api("附件管理-文件下载")
@RequestMapping("/fileDownload")
public class FileDownloadController {
    private static final int THUMBNAIL_DEFAULT_WIDTH = 38;
    private static final int THUMBNAIL_DEFAULT_HEIGHT = 38;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private SecurityUtils securityUtils;

    /**
     * 单文件下载
     *
     * @param configurationKey 附件标识
     * @param fileToken        文件token
     * @param t                是否缩略图
     * @param w                缩略图宽度
     * @param h                缩略图高度
     * @author JonnyJiang
     * @date 2020/4/21 16:24
     */

    @CrossOrigin
    @ApiOperation(value = "单文件下载")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("configurationKey") String configurationKey, @RequestParam("fileToken") String fileToken, @RequestParam(value = "t", required = false) String t, @RequestParam(value = "w", required = false) String w, @RequestParam(value = "h", required = false) String h, @RequestParam(APP_ID) String appName) {
        try {
            if (StringUtils.isEmpty(configurationKey)) {
                throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
            }
            Boolean isThumbnail = false;
            Integer width = THUMBNAIL_DEFAULT_WIDTH;
            Integer height = THUMBNAIL_DEFAULT_HEIGHT;
            if (ObjectUtils.equals(t, String.valueOf(IntegerUtils.TRUE_VALUE))) {
                isThumbnail = true;
                if (StringUtils.isNotEmpty(w)) {
                    width = Integer.valueOf(w);
                }
                if (StringUtils.isNotEmpty(h)) {
                    height = Integer.valueOf(h);
                }
            }
            fileInfoService.download(appName, configurationKey, fileToken, isThumbnail, width, height);
        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                if (response.getOutputStream() == null) {
                    response.getWriter().append(e.getMessage());
                } else {
                    throw new DownloadExpcetion(e.getMessage());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * 单文件下载
     *
     * @param configurationKey 附件标识
     * @param fileToken        文件token
     * @param t                是否缩略图
     * @param w                缩略图宽度
     * @param h                缩略图高度
     * @author JonnyJiang
     * @date 2020/4/21 16:24
     */

    @CrossOrigin
    @ApiOperation(value = "单文件下载")
    @RequestMapping(value = "/downloadReviewFile", method = RequestMethod.GET)
    public void downloadReviewFile(@RequestParam("configurationKey") String configurationKey, @RequestParam("fileToken") String fileToken, @RequestParam(value = "t", required = false) String t, @RequestParam(value = "w", required = false) String w, @RequestParam(value = "h", required = false) String h, @RequestParam(APP_ID) String appName) {
        try {
            if (StringUtils.isEmpty(configurationKey)) {
                throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
            }
            Boolean isThumbnail = false;
            Integer width = THUMBNAIL_DEFAULT_WIDTH;
            Integer height = THUMBNAIL_DEFAULT_HEIGHT;
            if (ObjectUtils.equals(t, String.valueOf(IntegerUtils.TRUE_VALUE))) {
                isThumbnail = true;
                if (StringUtils.isNotEmpty(w)) {
                    width = Integer.valueOf(w);
                }
                if (StringUtils.isNotEmpty(h)) {
                    height = Integer.valueOf(h);
                }
            }
            String tokenFromCookie = securityUtils.getTokenFromCookie();
            if(StringUtils.isNotEmpty(tokenFromCookie)){
                String token = securityUtils.getToken();
                String tokenTemp = tokenFromCookie.replace("%3D","=");
                if(tokenTemp.equals(token)){
                    FileInfoDO fileInfoDO = fileInfoService.getOne(new QueryWrapper<FileInfoDO>().eq("ID",fileToken));
                    String fileName = fileInfoDO.getFileName();
                    String fileFormId = fileInfoDO.getOldFormId();
                    FileInfoDO fileInfoDO1 = fileInfoService.getOne(new QueryWrapper<FileInfoDO>().eq("FORM_ID",fileFormId).eq("FILE_NAME",fileName));
                    String fileToken1 = fileInfoDO1.getId();
                    fileInfoService.download(appName, configurationKey, fileToken1, isThumbnail, width, height);
                }else{
                    throw new DownloadExpcetion("无权限下载！");
                }
            }
            else{
                throw new DownloadExpcetion("无权限下载！");
            }
        } catch (Exception e) {
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                if (response.getOutputStream() == null) {
                    response.getWriter().append(e.getMessage());
                } else {
                    throw new DownloadExpcetion(e.getMessage());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 首文件下载
     *
     * @param configurationKey 附件配置
     * @param formId           表单id
     * @param appName          应用标识
     * @author JonnyJiang
     * @date 2019/11/15 9:22
     */

    @CrossOrigin
    @ApiOperation(value = "首文件下载")
    @RequestMapping(value = "/downloadFirstFile", method = RequestMethod.GET)
    public void downloadFirstFile(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId, @RequestParam(APP_ID) String appName) {
        if (StringUtils.isEmpty(configurationKey)) {
            throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
        }
        fileInfoService.downloadFirstFile(appName, configurationKey, formId);
    }

    /**
     * 打包下载
     *
     * @param configurationKey 附件配置
     * @param downloadToken    表单token
     * @author JonnyJiang
     * @date 2019/5/21 21:18
     */

    @CrossOrigin
    @ApiOperation(value = "打包下载")
    @RequestMapping(value = "/downloadZipped", method = RequestMethod.GET)
    public void downloadZipped(@RequestParam("configurationKey") String configurationKey, @RequestParam("downloadToken") String downloadToken, @RequestParam(APP_ID) String appName) {
        if (StringUtils.isEmpty(configurationKey)) {
            throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
        }
        fileInfoService.downloadZipped(appName, configurationKey, downloadToken);
    }

    /**
     * 预览
     *
     * @param configurationKey 附件标识
     * @param fileToken        文件token
     * @author JonnyJiang
     * @date 2020/4/21 16:10
     */

    @CrossOrigin
    @ApiOperation(value = "预览")
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public void preview(@RequestParam("configurationKey") String configurationKey, @RequestParam("fileToken") String fileToken, @RequestParam(APP_ID) String appName) {
        if (StringUtils.isEmpty(configurationKey)) {
            throw new PreviewException(LocaleUtils.getConfigurationKeyIsNull());
        }
        fileInfoService.preview(appName, configurationKey, fileToken);
    }

    @CrossOrigin
    @ApiOperation(value = "导出压缩包")
    @RequestMapping(value = "/exportZip", method = RequestMethod.GET)
    public void exportZip(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId, @RequestParam(APP_ID) String appId) {
        ExportInfo exportInfo = new ExportInfo();
        exportInfo.setAppId(appId);
        exportInfo.setConfigurationKey(configurationKey);
        exportInfo.addFormId(formId);
        fileInfoService.exportZip(exportInfo);
    }

    @CrossOrigin
    @ApiOperation(value = "批量导出压缩包")
    @RequestMapping(value = "/exportZipBatch", method = RequestMethod.POST)
    public void exportZipBatch(@RequestBody List<ExportInfo> exportInfos) {
        fileInfoService.exportZipBatch(exportInfos);
    }
}