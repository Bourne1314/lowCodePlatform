package com.csicit.ace.fileserver.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.file.FileUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.file.delegate.EventListener;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.fileserver.core.controller.FileUploadController;
import com.csicit.ace.data.persistent.mapper.FileRepositoryMapper;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.EventUtils;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Service
public class FileRepositoryServiceImpl extends BaseServiceImpl<FileRepositoryMapper, FileRepositoryDO> implements FileRepositoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileRepositoryServiceImpl.class);
    @Autowired
    FileConfigurationService fileConfigurationService;
    @Autowired
    FileInfoService fileInfoService;
    @Autowired
    CacheUtil cacheUtil;

    private void onFileUploading(FileConfigurationDO configuration, FileVO file) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(EventListener.VARNAME_FORM_ID, file.getFormId());
        try {
            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_UPLOADING, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UploadExpcetion(e.getMessage());
        }
    }

    private void checkSuffix(FileConfigurationDO fileConfiguration, String suffix) {
        if (com.csicit.ace.common.utils.StringUtils.isNotBlank(fileConfiguration.getAccept())) {
            // 后缀名
            String[] suffixes = fileConfiguration.getAccept().replace(" ", "").split(",");
            for (String s : suffixes) {
                if (com.csicit.ace.common.utils.StringUtils.equals(s, suffix)) {
                    return;
                }
            }
            throw new UploadExpcetion(LocaleUtils.getAcceptNotFix(fileConfiguration.getAccept(), suffix));
        }
    }

    private void checkUpload(FileConfigurationDO fileConfiguration, String formId, FileVO file) {// 判断是否允许上传
        if (!IntegerUtils.isTrue(fileConfiguration.getAllowUpload())) {
            throw new UploadExpcetion(LocaleUtils.getNotAllowUpload(fileConfiguration.getConfigurationKey()));
        }
        // 判断单个是否超过文件大小限制
        if (fileConfiguration.getFileSingleSizeLimit() != null && fileConfiguration.getFileSingleSizeLimit() > 0) {
            if (fileConfiguration.getFileSingleSizeLimit() < file.getFileSize()) {
                throw new UploadExpcetion(LocaleUtils.getFileSingleSizeLimit(fileConfiguration.getFileSingleSizeLimit(), file.getFileSize()));
            }
        }
        String suffix = FileUtil.getSuffix(file.getFileName());
        // 判断后缀名是否满足
        checkSuffix(fileConfiguration, suffix);
        List<FileInfoDO> fileInfos = fileInfoService.listByFormId(fileConfiguration, formId);
        if (fileConfiguration.getFileNumLimit() != null && fileConfiguration.getFileNumLimit() > 0) {
            if (fileInfos.size() + 1 > fileConfiguration.getFileNumLimit()) {
                throw new UploadExpcetion(LocaleUtils.getFileNumLimit(fileConfiguration.getFileNumLimit(), fileInfos.size()));
            }
        }
        // 判断是否超过文件大小限制
        if (fileConfiguration.getFileSizeLimit() != null && fileConfiguration.getFileSizeLimit() > 0) {
            Long totalSize = fileInfos.stream().mapToLong(com.csicit.ace.common.pojo.domain.FileInfoDO::getFileSize).sum();
            if (totalSize + file.getFileSize() > fileConfiguration.getFileSizeLimit()) {
                throw new UploadExpcetion(LocaleUtils.getFileSizeLimit(fileConfiguration.getFileSizeLimit(), totalSize, file.getFileSize()));
            }
        }
    }

    @Override
    public FileInfoDO allocateSpace(FileConfigurationDO fileConfiguration, FileVO file) {
        if (StringUtils.isNotEmpty(file.getId())) {
            // 如果id不为空，则先删除id对应的文件
            FileInfoDO fileInfo = fileInfoService.getById(file.getId());
            if (fileInfo != null) {
                LOGGER.debug("file exist: " + file.getId());
                if (IntegerUtils.isTrue(fileConfiguration.getEnableSecretLevel())) {
                    if (file.getSecretLevel() == null) {
                        file.setSecretLevel(fileInfo.getSecretLevel());
                    }
                }
                fileInfoService.delete(fileConfiguration, fileInfo);
                LOGGER.debug("file deleted: " + file.getId());
            }
        }
        if (IntegerUtils.isTrue(fileConfiguration.getEnableSecretLevel())) {
            if (securityUtils.listAvailableFileSecretLevel().stream().noneMatch(o -> o.equals(file.getSecretLevel()))) {
                throw new UploadExpcetion(LocaleUtils.getFileSecretLevelNotMatch());
            }
        } else {
            file.setSecretLevel(SecurityUtils.MIN_SECRET_LEVEL);
        }
        checkUpload(fileConfiguration, file.getFormId(), file);
        onFileUploading(fileConfiguration, file);
        // 计算需要空间大小
        Long[] encryptResult = getEncryptedSizeAndChunks(fileConfiguration.getEnableEncrypt(), file.getFileSize(),
                fileConfiguration.getChunkSize());
        Long encryptedSize = encryptResult[0];
        Long chunks = encryptResult[1];
        FileInfoDO fileInfoDo = new FileInfoDO();
        if (StringUtils.isNotEmpty(file.getId())) {
            fileInfoDo.setId(file.getId());
        }
        fileInfoDo.setFileName(file.getFileName());
        fileInfoDo.setFormId(file.getFormId());
        fileInfoDo.setSecretLevel(file.getSecretLevel());
        fileInfoDo.setMd5(file.getMd5());
        fileInfoDo.setContentType(file.getContentType());
        fileInfoDo.setFileSize(file.getFileSize());
        fileInfoDo.setFileConfigurationId(fileConfiguration.getId());
        // 如果已启用文件审查，则将文件标记为需要审查
        fileInfoDo.setNeedReview(fileConfiguration.getEnableReview());
        fileInfoDo.setChunks(chunks);
        // 保存文件加密后大小
        fileInfoDo.setEncryptedSize(encryptedSize);
        AbstractFileRepository abstractFileRepository;
        if (StringUtils.isEmpty(fileConfiguration.getFileRepositoryId())) {
            abstractFileRepository = AbstractFileRepository.getAvailableRepository(this, sysAuditLogService,
                    securityUtils, encryptedSize);
            fileConfiguration.setFileRepositoryId(abstractFileRepository.getFileRepositoryId());
        } else {
            FileRepositoryDO fileRepository = baseMapper.selectById(fileConfiguration.getFileRepositoryId());
            if (fileRepository == null) {
                throw new UploadExpcetion(LocaleUtils.getFileRepositoryNotFoundById(fileConfiguration.getFileRepositoryId()));
            } else {
                abstractFileRepository = AbstractFileRepository.getFileRepository(this, sysAuditLogService,
                        securityUtils, fileRepository);
                if (!abstractFileRepository.tryAllocationStorage(encryptedSize)) {
                    throw new UploadExpcetion(LocaleUtils.getFileRepositoryInsufficient());
                }
            }
        }
        fileInfoDo.setFileRepositoryId(fileConfiguration.getFileRepositoryId());
        fileInfoDo.setUploaderId(securityUtils.getCurrentUserId());
        fileInfoDo.setUploadTime(LocalDateTime.now());
//        fileInfoService.save(fileInfoDo);
        //判断是否开启附件审查
        FileConfigurationDO fileConfigurationDO = fileConfigurationService.getById(fileConfiguration.getId());
        System.out.println(fileConfigurationDO.getId());
        Integer enableReview = fileConfigurationDO.getEnableReview();
        fileInfoDo.setNeedReview(enableReview);
        fileInfoService.save(fileInfoDo);

//        String fileName = file.getFileName();
//        List<FileInfoDO> fileInfoDOList = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName));
//        List<String> lists = fileInfoDOList.stream().map(FileInfoDO::getId).collect(Collectors.toList());
//        System.out.println(lists);
//
//        UpdateWrapper<FileInfoDO> fds = new UpdateWrapper<FileInfoDO>();
//        FileInfoDO fileInfoDO = new FileInfoDO();
//        fds.set("IS_NEED_REVIEW",2);
//        fds.in("ID",lists);
//        fileInfoService.update(fileInfoDO,fds);
//        fileInfoService.update(new UpdateWrapper<FileInfoDO>().in("ID",lists).set("IS_NEED_REVIEW",2));
//        if(enableReview==1){
//            Integer counts0 = fileInfoService.count(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName).eq("IS_NEED_REVIEW",0));
//            Integer counts1 = fileInfoService.count(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName).eq("IS_NEED_REVIEW",1));
//            Integer counts2 = fileInfoService.count(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName).eq("IS_NEED_REVIEW",2));
//            if(counts0>0){
//                fileInfoDo.setNeedReview(0);
//                fileInfoService.save(fileInfoDo);
//                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件上传，分配空间", fileInfoDo
//                        .getEncryptedSize() + "字节");
//            }
//            else if(counts2>0){
//                fileInfoDo.setNeedReview(2);
//                fileInfoService.save(fileInfoDo);
//                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件上传，分配空间", fileInfoDo
//                        .getEncryptedSize() + "字节");
//            }
//            else{
//                fileInfoDo.setNeedReview(1);
//                fileInfoService.save(fileInfoDo);
//                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件上传，分配空间", fileInfoDo
//                        .getEncryptedSize() + "字节");
//            }
//        }
//        else{
//            //如果附件配置关闭审查，则将该文件同名的所有记录均置为不需要审查。
//            List<FileInfoDO> fileInfoDOList = fileInfoService.list(new QueryWrapper<FileInfoDO>().eq("FILE_NAME",fileName));
//            List<String> lists = fileInfoDOList.stream().map(FileInfoDO::getId).collect(Collectors.toList());
//            System.out.println(lists);
//            UpdateWrapper<FileInfoDO> fds = new UpdateWrapper<FileInfoDO>();
//            FileInfoDO fileInfoDO = new FileInfoDO();
//            fds.set("IS_NEED_REVIEW",0);
//            fds.in("ID",lists);
//            fileInfoService.update(fileInfoDO,fds);
////            fileInfoService.update(new UpdateWrapper<FileInfoDO>().in("ID",lists).set("IS_NEED_REVIEW",2));
//            System.out.println(fileName);
//            fileInfoDo.setNeedReview(0);
//            fileInfoService.save(fileInfoDo);
//        }
//        System.out.println(enableReview);
//        fileInfoDo.setNeedReview(enableReview);
//        fileInfoService.save(fileInfoDo);
        sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件上传，分配空间", fileInfoDo
                .getEncryptedSize() + "字节");
        if (IntegerUtils.isTrue(fileConfiguration.getEnableDownloadToken())) {
            String id = fileInfoDo.getId();
            fileInfoDo.setId(UUID.randomUUID().toString());
            cacheUtil.set(fileInfoDo.getId(), id, AbstractFileRepository.FILE_TOKEN_CONFIG_EXPIRE);
        }
        // 将当前文件的附件配置信息存储于缓存中，在一定时间内直接使用，加快处理速度
        cacheUtil.set(FileUploadController.FILE_CONFIGURATION_PREFIX + file.getYfId(), fileConfiguration, AbstractFileRepository.FILE_CONFIGURATION_CONFIG_EXPIRE);
        return fileInfoDo;
    }

    @Override
    public Boolean updateUsedSizeById(String fileRepositoryId, Long size) {
        return baseMapper.updateUsedSizeById(fileRepositoryId, size) > 0;
    }

    @Override
    public FileRepositoryDO getByFileInfo(FileInfoDO fileInfo) {
        FileRepositoryDO fileRepository = baseMapper.selectById(fileInfo.getFileRepositoryId());
        if (fileRepository == null) {
            throw new ServerException(LocaleUtils.getFileRepositoryNotFoundById(fileInfo.getFileRepositoryId()));
        }
        return fileRepository;
    }

    @Override
    public void releaseSpace(String fileRepositoryId, Long size) {
        baseMapper.updateUsedSizeById(fileRepositoryId, size * -1);
    }

    /**
     * 保存文件存储库
     *
     * @param instance
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/5/31 17:23
     */
    @Override
    public R insert(FileRepositoryDO instance) {
        instance.setCreateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());
        if (save(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存文件存储库", instance)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改文件存储库
     *
     * @param instance
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/5/31 17:24
     */
    @Override
    public R update(FileRepositoryDO instance) {
        instance.setUpdateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());
        if (updateById(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新文件存储库", instance)) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除文件存储库
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
        List<FileRepositoryDO> list = list(new QueryWrapper<FileRepositoryDO>()
                .and(ids == null || ids.length == 0, i -> i.eq("1", "2")).in("id", ids)
                .select("repository_key"));
        if (removeByIds(Arrays.asList(ids))) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除文件存储库", list
                    .parallelStream().map
                            (FileRepositoryDO::getRepositoryKey)
                    .collect(Collectors.toList()))) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    @Override
    public Long[] getEncryptedSizeAndChunks(Integer enableEncrypt, Long size, Long chunkSize) {
        Long encryptedSize = size;
        Long chunks;
        if (IntegerUtils.isTrue(enableEncrypt)) {
            // 计算切片数量
            chunks = size / chunkSize;
            // 如果切片长度不是分组长度的整倍数，则增加补位字节数
            Long chunkMo = chunkSize % GMBaseUtil.SM4_GROUP_BYTES;
            if (chunkMo != 0) {
                encryptedSize += (GMBaseUtil.SM4_GROUP_BYTES - chunkMo) * chunks;
            }
            // 最后一个切片的大小
            Long sizeMo = size % chunkSize;
            if (sizeMo != 0) {
                chunks++;
                chunkMo = sizeMo % GMBaseUtil.SM4_GROUP_BYTES;
                if (chunkMo != 0) {
                    encryptedSize += GMBaseUtil.SM4_GROUP_BYTES - chunkMo;
                }
            }
        } else {
            // 计算切片数量
            chunks = size / chunkSize;
            Long sizeMo = size % chunkSize;
            if (sizeMo != 0) {
                chunks++;
            }
        }
        return new Long[]{encryptedSize, chunks};
    }
}