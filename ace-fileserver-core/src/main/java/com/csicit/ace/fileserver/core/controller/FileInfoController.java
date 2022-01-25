package com.csicit.ace.fileserver.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.domain.FileReviewDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.fileserver.core.exception.FileConfigurationNotFoundByIdException;
import com.csicit.ace.fileserver.core.exception.FileInfoNotFoundByIdException;
import com.csicit.ace.fileserver.core.exception.FileRepositoryNotFoundByIdException;
import com.csicit.ace.fileserver.core.exception.NoAccessToDeleteFileFromRecycleBinException;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.service.FileReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.csicit.ace.fileserver.core.controller.FileConfigurationController.APP_ID;

/**
 * 附件管理-文件信息
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@RestController
@Api("附件管理-文件信息")
@RequestMapping("/fileInfo")
public class FileInfoController {
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    FileConfigurationService fileConfigurationService;
    @Autowired
    FileRepositoryService fileRepositoryService;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    FileReviewService fileReviewService;

    /**
     * 单文件共享
     *
     * @param params fileId/configurationKey/desFormId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/5/22 18:11
     */

    @CrossOrigin
    @ApiOperation(value = "单文件共享")
    @RequestMapping(value = "/shareByFileId", method = RequestMethod.POST)
    public R shareByFileId(@RequestBody Map<String, Object> params) {
        try {
            return R.ok().put("fileInfo", feignShareByFileId(params));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 单文件共享
     *
     * @param params fileId/configurationKey/desFormId
     * @return 共享生成的新文件
     * @author JonnyJiang
     * @date 2019/11/21 15:59
     */

    @CrossOrigin
    @ApiOperation(value = "单文件共享")
    @RequestMapping(value = "/feignShareByFileId", method = RequestMethod.POST)
    public FileInfoDO feignShareByFileId(@RequestBody Map<String, Object> params) {
        String configurationKey = (String) params.get("configurationKey");
        String fileId = (String) params.get("fileId");
        String desConfigurationKey = (String) params.get("desConfigurationKey");
        String desFormId = (String) params.get("desFormId");
        String appId = (String) params.get(APP_ID);
        return fileInfoService.shareByFileId(appId, configurationKey, fileId, desConfigurationKey, desFormId);
    }

    /**
     * 表单文件共享
     *
     * @param params configurationKey/formId/desConfigurationKey/desFormId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/6/4 20:04
     */

    @CrossOrigin
    @ApiOperation(value = "表单文件共享")
    @RequestMapping(value = "/shareByFormId", method = RequestMethod.POST)
    public R shareByFormId(@RequestBody Map<String, Object> params) {
        try {
            feignShareByFormId(params);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 表单文件共享
     *
     * @param params configurationKey/formId/desConfigurationKey/desFormId
     * @author JonnyJiang
     * @date 2019/11/22 9:23
     */

    @CrossOrigin
    @ApiOperation(value = "表单文件共享")
    @RequestMapping(value = "/feignShareByFormId", method = RequestMethod.POST)
    public void feignShareByFormId(@RequestBody Map<String, Object> params) {
        String configurationKey = (String) params.get("configurationKey");
        String formId = (String) params.get("formId");
        String desConfigurationKey = (String) params.get("desConfigurationKey");
        String desFormId = (String) params.get("desFormId");
        String appId = (String) params.get(APP_ID);
        fileInfoService.shareByFormId(appId, configurationKey, formId, desConfigurationKey, desFormId);
    }

    /**
     * 删除文件
     *
     * @param params configurationKey/fileId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/6/5 11:22
     */

    @CrossOrigin
    @ApiOperation(value = "删除文件")
    @RequestMapping(value = "/deleteByFileId", method = RequestMethod.POST)
    public R deleteByFileId(@RequestBody Map<String, Object> params) {
        try {
            feignDeleteByFileId(params);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param params configurationKey/fileId
     * @author JonnyJiang
     * @date 2019/11/21 15:52
     */

    @CrossOrigin
    @ApiOperation(value = "删除文件")
    @RequestMapping(value = "/feignDeleteByFileId", method = RequestMethod.POST)
    public void feignDeleteByFileId(@RequestBody Map<String, Object> params) {
        String fileId = (String) params.get("fileId");
        String configurationKey = (String) params.get("configurationKey");
        String appId = (String) params.get(APP_ID);
        FileInfoDO fileInfoDO = fileInfoService.getOne(new QueryWrapper<FileInfoDO>().eq("Id",fileId));

        String oldFormId = fileInfoDO.getFormId();
        String fileName = fileInfoDO.getFileName();
        FileInfoDO fileInfoDO1 = fileInfoService.getOne(new QueryWrapper<FileInfoDO>().eq("OLD_FORM_ID",oldFormId).eq("FILE_NAME",fileName));
        if(fileInfoDO1!=null){
        fileInfoService.deleteByFileId(appId,configurationKey,fileInfoDO1.getId());
        fileInfoService.deleteByFileId(appId, configurationKey, fileId);}
        else{
            fileInfoService.deleteByFileId(appId, configurationKey, fileId);
        }
    }

    /**
     * 删除表单文件
     *
     * @param params configurationKey/formId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/6/5 11:21
     */

    @CrossOrigin
    @ApiOperation(value = "删除表单文件")
    @RequestMapping(value = "/deleteByFormId", method = RequestMethod.POST)
    public R deleteByFormId(@RequestBody Map<String, Object> params) {
        try {
            feignDeleteByFormId(params);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除表单所有文件
     *
     * @param params formId
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/12/23 11:29
     */

    @CrossOrigin
    @ApiOperation(value = "删除表单文件")
    @RequestMapping(value = "/deleteAllByFormId", method = RequestMethod.POST)
    public R deleteAllByFormId(@RequestBody Map<String, Object> params) {
        try {
            feignDeleteAllByFormId(params);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除表单文件
     *
     * @param params configurationKey/formId
     * @author JonnyJiang
     * @date 2019/6/5 11:21
     */

    @CrossOrigin
    @ApiOperation(value = "删除表单文件")
    @RequestMapping(value = "/feignDeleteByFormId", method = RequestMethod.POST)
    public void feignDeleteByFormId(@RequestBody Map<String, Object> params) {
        String formId = (String) params.get("formId");
        String configurationKey = (String) params.get("configurationKey");
        String appId = (String) params.get(APP_ID);
        fileInfoService.deleteByFormId(appId, configurationKey, formId);
    }

    /**
     * 删除表单所有文件
     *
     * @param params formId
     * @author JonnyJiang
     * @date 2019/12/23 11:30
     */

    @CrossOrigin
    @ApiOperation(value = "删除表单文件")
    @RequestMapping(value = "/feignDeleteAllByFormId", method = RequestMethod.POST)
    public void feignDeleteAllByFormId(@RequestBody Map<String, Object> params) {
        String formId = (String) params.get("formId");
        fileInfoService.deleteAllByFormId(formId);
    }

    /**
     * 获取表单文件列表
     *
     * @param params configurationKey/formId
     * @return 表单文件列表
     * @author JonnyJiang
     * @date 2019/6/5 11:21
     */

    @CrossOrigin
    @ApiOperation(value = "获取表单文件")
    @RequestMapping(value = "/listByFormId", method = RequestMethod.POST)
    public R listByFormId(@RequestBody Map<String, Object> params) {
        try {
            return R.ok().put("fileInfos", feignListByFormId(params));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取表单文件列表
     *
     * @param params configurationKey/formId
     * @return 表单文件列表
     * @author JonnyJiang
     * @date 2019/11/21 16:02
     */

    @CrossOrigin
    @ApiOperation(value = "获取表单文件")
    @RequestMapping(value = "/feignListByFormId", method = RequestMethod.POST)
    public List<FileInfoDO> feignListByFormId(@RequestBody Map<String, Object> params) {
        String formId = (String) params.get("formId");
        String configurationKey = (String) params.get("configurationKey");
        String appId = (String) params.get(APP_ID);
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        return fileInfoService.listByFormId(configuration, formId);
    }

    @CrossOrigin
    @ApiOperation(value = "获取文件数量")
    @RequestMapping("/getFileCountByFormId")
    public R getFileCountByFormId(@RequestParam(APP_ID) String appId, @RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        return R.ok().put("count", fileInfoService.getCountByFormId(configuration, formId));
    }

    /**
     * 获取文件信息
     *
     * @param params configurationKey/fileId
     * @return 文件信息
     * @author JonnyJiang
     * @date 2019/6/5 11:20
     */

    @CrossOrigin
    @ApiOperation(value = "获取文件")
    @RequestMapping(value = "/getByFileId", method = RequestMethod.POST)
    public R getByFileId(@RequestBody Map<String, Object> params) {
        try {
            return R.ok().put("fileInfo", feignGetByFileId(params));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取文件信息
     *
     * @param params configurationKey/fileId
     * @return 文件信息
     * @author JonnyJiang
     * @date 2019/11/21 16:05
     */

    @CrossOrigin
    @ApiOperation(value = "获取文件")
    @RequestMapping(value = "/feignGetByFileId", method = RequestMethod.POST)
    public FileInfoDO feignGetByFileId(@RequestBody Map<String, Object> params) {
        String configurationKey = (String) params.get("configurationKey");
        String fileId = (String) params.get("fileId");
        String appId = (String) params.get(APP_ID);
        return fileInfoService.getByFileToken(appId, configurationKey, fileId);
    }


    /***
     * @description: 获取已提交附件审查文件
     * @params: uploadFormId    旧表单的formId
     * @return: com.csicit.ace.common.utils.server.R
     * @author: Zhangzhaojun
     * @time: 2021/12/2 9:05
     */

    @CrossOrigin
    @ApiOperation(value = "获取已提交附件审查文件")
    @RequestMapping(value = "/getUploadedReviewFile", method = RequestMethod.GET)
    public R getUploadedReviewFile(@RequestParam String uploadFormId) {
        List<FileInfoDO> fileInfoDOList = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("OLD_FORM_ID",uploadFormId));
        Set<String> formIdSet = fileInfoDOList.stream().map(FileInfoDO::getFormId).collect(Collectors.toSet());
        if(formIdSet.isEmpty()){
            return R.ok();
        }
        else{
            List<FileReviewDO> fileReviewDOS = fileReviewService.list(new QueryWrapper<FileReviewDO>().in("ID",formIdSet));
            return R.ok().put("FileInfoList",fileReviewDOS);
        }

    }

    @CrossOrigin
    @RequestMapping(value = "/deleteByFileIdFromRecycleBin", method = RequestMethod.POST)
    public R deleteByFileIdFromRecycleBin(@RequestBody Map<String, Object> params)
    {
        String fileId = (String) params.get("fileId");
        String fileConfigurationId = (String) params.get("fileConfigurationId");
        FileConfigurationDO fileConfiguration = fileConfigurationService.getById(fileConfigurationId);
        if(fileConfiguration == null)
        {
            throw new FileConfigurationNotFoundByIdException(fileConfigurationId);
        }
        else
        {
            FileInfoDO fileInfo = fileInfoService.getActualFileInfo(fileConfiguration, fileId);
            if(fileInfo == null)
            {
                throw new FileInfoNotFoundByIdException(fileId);
            }
            else
            {
                FileRepositoryDO fileRepository = fileRepositoryService.getById(fileInfo.getFileRepositoryId());
                if(fileRepository == null)
                {
                    throw new FileRepositoryNotFoundByIdException(fileInfo.getFileRepositoryId());
                }
                else
                {
                    if(!securityUtils.isAdmin() && !StringUtils.equals(fileInfo.getRecyclerId(), securityUtils.getCurrentUserId()))
                    {
                        throw new NoAccessToDeleteFileFromRecycleBinException(fileInfo.getId(), fileInfo.getRecyclerId(), securityUtils.getCurrentUserId());
                    }
                    fileInfoService.deleteFilePhysical(fileInfo, fileRepository);
                }
            }
        }
        return R.ok();
    }

    @CrossOrigin
    @RequestMapping("/listRecycleBin")
    public R listRecycleBin(@RequestParam Map<String, Object> params)
    {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        params.put("IS_IN_RECYCLE_BIN", 1);
        if(!securityUtils.isAdmin())
        {
            // 如果不是管理员，只能查看自己回收的文件
            params.put("RECYCLER_ID", securityUtils.getCurrentUserId());
        }
        if(params.containsKey("token"))
        {
            params.remove("token");
        }
        Page<FileInfoDO> page = new Page<>(current, size);
        IPage<FileInfoDO> list = fileInfoService.page(page, MapWrapper.getEqualInstance(params));
        List<String> fileConfigurationIds = list.getRecords().stream().map(FileInfoDO::getFileConfigurationId).distinct().collect(Collectors.toList());
        Collection<FileConfigurationDO> fileConfigurations = fileConfigurationService.listByIds(fileConfigurationIds);
        for (FileInfoDO fileInfo: list.getRecords()) {
            FileConfigurationDO fileConfiguration = fileConfigurations.stream().filter(o->StringUtils.equals(fileInfo.getFileConfigurationId(), o.getId())).findAny().orElse(null);
            if(fileConfiguration != null)
            {
                if(IntegerUtils.isTrue(fileConfiguration.getEnableDownloadToken()))
                {
                    // 如果启用临时Token
                    fileInfoService.setDownloadToken(fileInfo);
                }
            }
        }
        return R.ok().put("page", list);
    }
}