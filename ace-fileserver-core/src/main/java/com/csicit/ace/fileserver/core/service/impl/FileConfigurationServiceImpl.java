package com.csicit.ace.fileserver.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.FileConfigurationMapper;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.ConfigurationType;
import com.csicit.ace.fileserver.core.utils.GlobalFileUtil;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Service("fileConfigurationService")
public class FileConfigurationServiceImpl extends BaseServiceImpl<FileConfigurationMapper, FileConfigurationDO>
        implements FileConfigurationService {
    /**
     * 日志记录工具
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileRepository.class);

    @Autowired
    private SysGroupAppServiceD sysGroupAppServiceD;

    @Override
    public FileConfigurationDO loadByKey(String configurationKey, String appId) {
        LOGGER.debug("configurationKey:" + configurationKey);
        FileConfigurationDO configuration = GlobalFileUtil.getGlobalFileConfigurationByKey(configurationKey);
        if (configuration == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("configuration_type", ConfigurationType.App.ordinal());
            params.put("configuration_key", configurationKey);
            params.put("app_id", appId);
            List<FileConfigurationDO> configurations = list(new QueryWrapper<FileConfigurationDO>().allEq(params));
            if (configurations.size() == 0) {
                throw new ServerException(LocaleUtils.getConfigurationNotFoundByKey(configurationKey, appId));
            } else if (configurations.size() > 1) {
                throw new ServerException(LocaleUtils.getConfigurationKeyDuplicate(configurationKey));
            }
            configuration = configurations.get(0);
        } else {
            configuration.setAppId(appId);
            SysGroupAppDO sysGroupApp = sysGroupAppServiceD.getById(appId);
            if (sysGroupApp == null) {
                throw new ServerException(LocaleUtils.getGroupAppNotFoundById(appId));
            }
            configuration.setGroupId(sysGroupApp.getGroupId());
        }
        combineConfiguration(configuration);
        return configuration;
    }

    @Override
    public FileConfigurationDO loadById(String id) {
        LOGGER.debug("configuration id:" + id);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("configuration_type", ConfigurationType.App.ordinal());
        FileConfigurationDO configuration = GlobalFileUtil.getGlobalFileConfigurationById(id);
        if (configuration == null) {
            configuration = baseMapper.selectOne(new QueryWrapper<FileConfigurationDO>().allEq(params));
            if (configuration == null) {
                throw new ServerException(LocaleUtils.getConfigurationNotFoundById(id));
            }
        }
        combineConfiguration(configuration);
        return configuration;
    }

    /**
     * 根据集团、租户级的系统配置项合并配置
     *
     * @param configuration 附件配置
     * @author JonnyJiang
     * @date 2019/7/22 20:58
     */

    private void combineConfiguration(FileConfigurationDO configuration) {
        if (StringUtils.isEmpty(configuration.getGroupDatasourceId())) {
            String value = ConfigEnum.GROUP_DATASOURCE_ID.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setGroupDatasourceId(value);
            } else {
                throw new ServerException(LocaleUtils.getGroupDatasourceNotSet());
            }
        }
        if (StringUtils.isEmpty(configuration.getFileRepositoryId())) {
            String value = ConfigEnum.FILE_REPOSITORY_ID.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setFileRepositoryId(value);
            }
        }
        if (StringUtils.isEmpty(configuration.getSubDirFormat())) {
            String value = ConfigEnum.SUB_DIR_FORMAT.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setSubDirFormat(value);
            }
        }
        if (configuration.getEnableEncrypt() == null) {
            String value = ConfigEnum.ENABLE_ENCRYPT.getValue(configuration.getAppId());
            configuration.setEnableEncrypt(Integer.parseInt(value));
        }
        if (configuration.getEnableSecretLevel() == null) {
            String value = ConfigEnum.ENABLE_SECRET_LEVEL.getValue(configuration.getAppId());
            configuration.setEnableSecretLevel(Integer.parseInt(value));
        }
        if (configuration.getAllowUpload() == null) {
            String value = ConfigEnum.ALLOW_UPLOAD.getValue(configuration.getAppId());
            configuration.setAllowUpload(Integer.parseInt(value));
        }
        if (configuration.getAllowDelete() == null) {
            String value = ConfigEnum.ALLOW_DELETE.getValue(configuration.getAppId());
            configuration.setAllowDelete(Integer.parseInt(value));
        }
        if (configuration.getAllowDownload() == null) {
            String value = ConfigEnum.ALLOW_DOWNLOAD.getValue(configuration.getAppId());
            configuration.setAllowDownload(Integer.parseInt(value));
        }
        if (configuration.getEnableDownloadToken() == null) {
            String value = ConfigEnum.ENABLE_DOWNLOAD_TOKEN.getValue(configuration.getAppId());
            configuration.setEnableDownloadToken(Integer.parseInt(value));
        }
        if (configuration.getEnableUserSeparate() == null) {
            String value = ConfigEnum.ENABLE_USER_SEPARATE.getValue(configuration.getAppId());
            configuration.setEnableUserSeparate(Integer.parseInt(value));
        }
        if (configuration.getEnableImageCompress() == null) {
            String value = ConfigEnum.ENABLE_IMAGE_COMPRESS.getValue(configuration.getAppId());
            configuration.setEnableImageCompress(Integer.parseInt(value));
        }
        if (configuration.getEnablePreview() == null) {
            String value = ConfigEnum.ENABLE_PREVIEW.getValue(configuration.getAppId());
            configuration.setEnablePreview(Integer.parseInt(value));
        }
        if (configuration.getEnableReview() == null) {
            String value = ConfigEnum.ENABLE_REVIEW.getValue(configuration.getAppId());
            configuration.setEnableReview(Integer.parseInt(value));
        }
        if (StringUtils.isBlank(configuration.getAccept())) {
            String value = ConfigEnum.ACCEPT.getValue(configuration.getAppId());
            if (StringUtils.isNotBlank(value)) {
                configuration.setAccept(value);
            }
        }
        if (configuration.getFileNumLimit() == null) {
            String value = ConfigEnum.FILE_NUM_LIMIT.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setFileNumLimit(Integer.parseInt(value));
            }
        }
        if (configuration.getFileSizeLimit() == null) {
            String value = ConfigEnum.FILE_SIZE_LIMIT.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setFileSizeLimit(Long.parseLong(value));
            }
        }
        if (configuration.getFileSingleSizeLimit() == null) {
            String value = ConfigEnum.FILE_SINGLE_SIZE_LIMIT.getValue(configuration.getAppId());
            if (StringUtils.isNotEmpty(value)) {
                configuration.setFileSingleSizeLimit(Long.parseLong(value));
            }
        }
        if (configuration.getEnableEvtFileUploading() == null) {
            String value = ConfigEnum.ENABLE_EVT_FILE_UPLOADING.getValue(configuration.getAppId());
            configuration.setEnableEvtFileUploading(Integer.parseInt(value));
        }
        if (configuration.getEnableEvtFileUploaded() == null) {
            String value = ConfigEnum.ENABLE_EVT_FILE_UPLOADED.getValue(configuration.getAppId());
            configuration.setEnableEvtFileUploaded(Integer.parseInt(value));
        }
        if (configuration.getEnableEvtFileDeleting() == null) {
            String value = ConfigEnum.ENABLE_EVT_FILE_DELETING.getValue(configuration.getAppId());
            configuration.setEnableEvtFileDeleting(Integer.parseInt(value));
        }
        if (configuration.getEnableEvtFileDownloading() == null) {
            String value = ConfigEnum.ENABLE_EVT_FILE_DOWNLOADING.getValue(configuration.getAppId());
            configuration.setEnableEvtFileDownloading(Integer.parseInt(value));
        }
        if (configuration.getEnableEvtFileDownloaded() == null) {
            String value = ConfigEnum.ENABLE_EVT_FILE_DOWNLOADED.getValue(configuration.getAppId());
            configuration.setEnableEvtFileDownloaded(Integer.parseInt(value));
        }
        if (configuration.getAllowDownloadWithoutLogin() == null) {
            String value = ConfigEnum.ALLOW_DOWNLOAD_WITHOUT_LOGIN.getValue(configuration.getAppId());
            configuration.setAllowDownloadWithoutLogin(Integer.parseInt(value));
        }
        String value = ConfigEnum.CHUNK_SIZE.getValue(configuration.getAppId());
        if (StringUtils.isNotEmpty(value)) {
            configuration.setChunkSize(Long.parseLong(value));
        }
    }

    @Override
    public FileConfigurationDO getByFile(FileInfoDO fileInfo) {
        // 根据最终文件重新查找配置信息
        FileConfigurationDO configuration = loadById(fileInfo.getFileConfigurationId());
        if (configuration == null) {
            throw new ServerException(LocaleUtils.getConfigurationNotFoundById(fileInfo.getFileConfigurationId()));
        }
        return loadByKey(configuration.getConfigurationKey(), configuration.getAppId());
    }

    /**
     * 保存文件配置项
     *
     * @param instance
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/5/31 17:23
     */
    @Override
    public R insert(FileConfigurationDO instance) {
        int count = 0;
        if (Objects.equals(0, instance.getConfigurationType())) {
            // 租户级
            count = count(new QueryWrapper<FileConfigurationDO>()
                    .eq("configuration_key", instance.getConfigurationKey()));
        } else if (Objects.equals(1, instance.getConfigurationType())) {
            // 集团级
            count = count(new QueryWrapper<FileConfigurationDO>().eq("group_id", instance.getGroupId())
                    .eq("configuration_key", instance.getConfigurationKey()));
        } else if (Objects.equals(2, instance.getConfigurationType())) {
            // 应用级
            count = count(new QueryWrapper<FileConfigurationDO>().eq("app_id", instance.getAppId())
                    .eq("configuration_key", instance.getConfigurationKey()));
        }
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("CONFIG_KEY_EXIST"));
        }
        instance.setCreateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());
        if (save(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存文件配置项","保存文件配置项："+ instance.getConfigurationKey(),
                    instance
                            .getGroupId(),
                    instance.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改文件配置项
     *
     * @param instance
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/5/31 17:24
     */
    @Override
    public R update(FileConfigurationDO instance) {
        instance.setUpdateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());
        if (updateById(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改文件配置项", "修改文件配置项:" +
                            instance.getConfigurationKey(),
                    instance
                            .getGroupId(),
                    instance.getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除文件配置项
     *
     * @param ids
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/5/31 17:39
     */
    @Override
    public R delete(String[] ids) {
        if (CollectionUtils.isEmpty(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        List<FileConfigurationDO> list = list(new QueryWrapper<FileConfigurationDO>()
                .and(ids == null || ids.length == 0, i -> i.eq("1", "2")).in("id", ids)
                .select("configuration_key", "group_id", "app_id"));
        if (removeByIds(Arrays.asList(ids))) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除文件配置项", "删除文件配置项：" + list
                    .parallelStream()
                    .map
                            (FileConfigurationDO::getConfigurationKey)
                    .collect(Collectors.toList()), list.get(0).getGroupId(), list.get(0).getAppId())) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}