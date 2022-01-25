package com.csicit.ace.fileserver.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.domain.file.FileInfo;
import com.csicit.ace.common.pojo.vo.ChunkVO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.FileInfoMapper;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.file.delegate.EventListener;
import com.csicit.ace.fileserver.core.*;
import com.csicit.ace.fileserver.core.controller.FileConfigurationController;
import com.csicit.ace.fileserver.core.service.FileChunkInfoService;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.EventUtils;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Service
public class FileInfoServiceImpl extends BaseServiceImpl<FileInfoMapper, FileInfoDO> implements FileInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Logger.class);
    private static final int THUMBNAIL_DEFAULT_WIDTH = 38;
    private static final int THUMBNAIL_DEFAULT_HEIGHT = 38;
    private static final Charset ZIP_ENCODING = UTF_8;
    /**
     * 第一个切片索引
     */
    private static final int FIRST_CHUNK_INDEX = 0;

    @Autowired
    private FileChunkInfoService fileChunkInfoService;
    @Autowired
    private FileRepositoryService fileRepositoryService;
    @Autowired
    private FileConfigurationService fileConfigurationService;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private SysAuthMixService sysAuthMixService;
    @Autowired
    private FileInfoService fileInfoService;

    @Override
    public InputStream getFileInputStream(Map<String, Object> params) {
        String configurationKey = (String) params.get("configurationKey");
        if (StringUtils.isEmpty(configurationKey)) {
            throw new DownloadExpcetion(LocaleUtils.getConfigurationKeyIsNull());
        }
        String fileToken = (String) params.get("fileToken");
        String appId = (String) params.get(FileConfigurationController.APP_ID);
        Boolean isThumbnail = false;
        Integer width = THUMBNAIL_DEFAULT_WIDTH;
        Integer height = THUMBNAIL_DEFAULT_HEIGHT;
        String t = (String) params.get("t");
        if (org.apache.commons.lang.ObjectUtils.equals(t, String.valueOf(IntegerUtils.TRUE_VALUE))) {
            isThumbnail = true;
            String w = (String) params.get("w");
            if (StringUtils.isNotEmpty(w)) {
                width = Integer.valueOf(w);
            }
            String h = (String) params.get("h");
            if (StringUtils.isNotEmpty(h)) {
                height = Integer.valueOf(h);
            }
        }
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        String fileInfoId;
        if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
            fileInfoId = cacheUtil.get(fileToken);
            if (StringUtils.isEmpty(fileInfoId)) {
                throw new DownloadExpcetion(LocaleUtils.getFileTokenExpried(fileToken));
            }
        } else {
            fileInfoId = fileToken;
        }
        //文件信息
        FileInfoDO fileInfo = getFileInfo(fileInfoId);
        onFileDownloading(configuration, fileInfo);
        // 最终指向的文件
        fileInfo = getActualFileInfo(fileInfoId);
        // 根据最终文件重新查找配置信息
        configuration = fileConfigurationService.getByFile(fileInfo);
        // 文件最终指向的附件库
        FileRepositoryDO fileRepository = fileRepositoryService.getByFileInfo(fileInfo);
        AbstractFileRepository abstractFileRepository = AbstractFileRepository.getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, fileRepository);
        InputStream inputStream = getFileStream(abstractFileRepository, fileInfo, isThumbnail, width, height);
        onFileDownloaded(configuration, fileInfo);
        return inputStream;
    }

    @Override
    public FileInfoDO upload(FileConfigurationDO configuration, byte[] bytes, Integer chunk, String yfId, Long size, Integer chunks) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return upload(configuration, inputStream, chunk, yfId, size, chunks);
    }

    @Override
    public FileInfoDO upload(FileConfigurationDO configuration, InputStream inputStream, Integer chunk, String yfId, Long size, Integer chunks) throws Exception {
        try {
            ChunkVO chunkVO = new ChunkVO();
            chunkVO.setChunk(chunk);
            chunkVO.setYfId(yfId);
            chunkVO.setSize(size);
            chunkVO.setChunks(chunks);
            FileInfoDO fileInfo = upload(inputStream, chunkVO, configuration);
            if (fileInfo != null) {
                Map<String, Object> params = new HashMap<>(16);
                params.put(EventListener.VARNAME_FILE_INFO, fileInfo.getFileInfoJson(configuration, true));
                EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_UPLOADED, params);
            }
            return fileInfo;
        } catch (Exception e) {
            String fileId = getActualFileId(configuration, yfId);
            moveToRecycleBin(fileId);
            throw e;
        }
    }

    @Override
    public FileInfoDO upload(InputStream inputStream, ChunkVO chunkVO, FileConfigurationDO configuration) {
        FileRepositoryDO fileRepository = fileRepositoryService.getById(configuration.getFileRepositoryId());
        if (fileRepository == null) {
            throw new UploadExpcetion(LocaleUtils.getFileRepositoryNotFoundById(configuration.getFileRepositoryId()));
        }
        AbstractFileRepository abstractFileRepository = AbstractFileRepository.getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, fileRepository);
        Integer chunk = chunkVO.getChunk();
        FileChunkInfoDO fileChunkInfo = new FileChunkInfoDO();
        String yfId = chunkVO.getYfId();
        String fileId = getActualFileId(configuration, yfId);
        fileChunkInfo.setFileInfoId(fileId);
        String chunkPath = "";
        if (StringUtils.isNotBlank(configuration.getSubDirFormat())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.applyPattern("yyyy");
            Date date = new Date();
            String year = simpleDateFormat.format(date);
            simpleDateFormat.applyPattern("MM");
            String mm = simpleDateFormat.format(date);
            simpleDateFormat.applyPattern("dd");
            String dd = simpleDateFormat.format(date);
            chunkPath = configuration.getSubDirFormat().replace("{yyyy}", year)
                    .replace("{MM}", mm)
                    .replace("{dd}", dd)
                    .replace("{groupId}", configuration.getGroupId())
                    .replace("{appId}", configuration.getAppId())
                    .replace("{userName}", securityUtils.getCurrentUserName())
                    .replace("/", File.separator)
                    .replaceAll("\\\\\\\\+", File.separator)
                    .replaceAll("^\\\\+", "");
        }
        String chunkName = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(chunkPath)) {
            chunkPath = chunkName;
        } else {
            chunkPath = chunkPath + File.separator + UUID.randomUUID();
        }
        fileChunkInfo.setChunkPath(chunkPath);
        Long size = chunkVO.getSize();
        Boolean isLastChunk = false;
        if (size > configuration.getChunkSize()) {
            Integer chunks = chunkVO.getChunks();
            if (chunks > 1) {
                System.out.println("chunk：" + chunk);
                fileChunkInfo.setChunkIndex(chunk);
                if (chunk == chunks - 1) {
                    // 如果是最后一块切片
                    fileChunkInfo.setChunkSize(size % configuration.getChunkSize());
                    isLastChunk = true;
                } else {
                    fileChunkInfo.setChunkSize(configuration.getChunkSize());
                }
            } else {
                fileChunkInfo.setChunkSize(size % configuration.getChunkSize());
                fileChunkInfo.setChunkIndex(FIRST_CHUNK_INDEX);
            }
        } else {
            fileChunkInfo.setChunkSize(size);
            fileChunkInfo.setChunkIndex(FIRST_CHUNK_INDEX);
            isLastChunk = true;
        }
        fileChunkInfo.setEncrypted(configuration.getEnableEncrypt());
        abstractFileRepository.save(fileChunkInfo, inputStream);
        fileChunkInfoService.save(fileChunkInfo);
        FileInfoDO fileInfo = null;
        if (IntegerUtils.isTrue(configuration.getEnableImageCompress())) {
            // 如果启用了图片压缩，并且是第一块切片，则需要更新文件占用空间，并释放存储空间
            if (fileChunkInfo.getChunkIndex() == FIRST_CHUNK_INDEX) {
                fileInfo = getById(fileId);
                if (fileInfo != null) {
                    Long sizeReleasing = fileInfo.getEncryptedSize();
                    Long[] encryptResult = fileRepositoryService.getEncryptedSizeAndChunks(configuration.getEnableEncrypt(), size, configuration.getChunkSize());
                    Long encryptedSize = encryptResult[0];
                    Long chunks = encryptResult[1];
                    fileInfo.setFileSize(size);
                    fileInfo.setEncryptedSize(encryptedSize);
                    fileInfo.setChunks(chunks);
                    sizeReleasing -= encryptedSize;
                    update(new FileInfoDO(), new UpdateWrapper<FileInfoDO>().set("FILE_SIZE", fileInfo.getFileSize()).set("ENCRYPTED_SIZE", fileInfo.getEncryptedSize()).set("CHUNKS", fileInfo.getChunks()).eq("ID", fileInfo.getId()));
                    fileRepositoryService.releaseSpace(fileRepository.getId(), sizeReleasing);
                }
            }
        }
        if (isLastChunk) {
            if (fileInfo == null) {
                fileInfo = getById(fileId);
            }
            if (fileInfo != null) {
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件上传", "文件名" + fileInfo.getFileName() + "，密级：" +
                        securityUtils.getFileSecretLevelText(fileInfo.getSecretLevel()));
                LOGGER.info("file upload: " + fileInfo.getFileName());
                return fileInfo;
            }
        }
        return null;
    }

    private FileInfoDO getFileInfo(String fileInfoId) {
        FileInfoDO fileInfo = getById(fileInfoId);
        if (fileInfo == null) {
            throw new ServerException(LocaleUtils.getFileNotFoundById(fileInfoId));
        }
        return fileInfo;
    }

    @Override
    public FileInfoDO getActualFileInfo(String fileInfoId) {
        FileInfoDO fileInfo = getFileInfo(fileInfoId);
        return getActualFileInfo(fileInfo);
    }

    @Override
    public FileInfoDO getActualFileInfo(FileInfoDO fileInfo) {
        if (!StringUtils.isEmpty(fileInfo.getSharedFileInfoId())) {
            return getFileInfo(fileInfo.getSharedFileInfoId());
        }
        return fileInfo;
    }

    private void onFileDownloading(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        LOGGER.debug("on file downloading begin");
        checkDownloadAuth(configuration, fileInfo);
        Map<String, Object> map = new HashMap<>(16);
        map.put(EventListener.VARNAME_FILE_INFO, fileInfo.getFileInfoJson(configuration, true));
        try {
            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_DOWNLOADING, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
        LOGGER.debug("on file downloading end");
    }

    private void onFileDownloaded(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        LOGGER.debug("on file downloaded begin");
        if (securityUtils.hasLoggedIn()) {
            LOGGER.debug("user has logged in");
            sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件访问", "文件名：" + fileInfo.getFileName() + "，密级：" +
                    securityUtils.getFileSecretLevelText(fileInfo.getSecretLevel()));
        } else {
            LOGGER.debug("user has not logged in");
            if (IntegerUtils.isTrue(configuration.getAllowDownloadWithoutLogin())) {
                sysAuditLogService.saveLogWithUserName(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件访问", "文件名：" + fileInfo.getFileName() + "，密级：" +
                        securityUtils.getFileSecretLevelText(fileInfo.getSecretLevel()), null, null);
            }
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put(EventListener.VARNAME_FILE_INFO, fileInfo.getFileInfoJson(configuration, true));
        try {
            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_DOWNLOADED, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
        LOGGER.debug("on file downloaded end");
        LOGGER.info("file download: " + fileInfo.getFileName());
    }

    private void onFileDeleting(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        if (StringUtils.isNotEmpty(configuration.getDeleteAuthCode())) {
            if (!sysAuthMixService.hasAuthCodeWithUserId(securityUtils.getCurrentUserId(), configuration.getDeleteAuthCode(), configuration.getAppId())) {
                throw new UploadExpcetion(LocaleUtils.getNoAccess(configuration.getDeleteAuthCode()));
            }
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put(EventListener.VARNAME_FILE_INFO, fileInfo.getFileInfoJson(configuration, true));
        try {
            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_DELETING, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException(e.getMessage());
        }
    }

    private void download(OutputStream outputStream, String fileInfoId, FileConfigurationDO configuration, Boolean isThumbnail, Integer width, Integer height, Boolean initReponseHeaderByFile) throws IOException {
        // 最终指向的文件
        FileInfoDO fileInfo = getActualFileInfo(fileInfoId);
        // 根据最终文件重新查找配置信息
        configuration = fileConfigurationService.getByFile(fileInfo);
        if (initReponseHeaderByFile) {
            initResponseHeader(fileInfo.getContentType(), fileInfo.getFileName(), fileInfo.getFileSize());
        }
        download(outputStream, configuration, fileInfo, isThumbnail, width, height);
    }

    private void download(OutputStream outputStream, FileConfigurationDO configuration, FileInfoDO fileInfo, Boolean isThumbnail, Integer width, Integer height) throws IOException {
        onFileDownloading(configuration, fileInfo);
        download(outputStream, fileInfo, isThumbnail, width, height);
        onFileDownloaded(configuration, fileInfo);
    }

    @Override
    public void download(String appId, String configurationKey, String fileToken, Boolean isThumbnail, Integer width, Integer height, OutputStream outputStream) throws IOException {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        String fileInfoId;
        if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
            fileInfoId = cacheUtil.get(fileToken);
            if (StringUtils.isEmpty(fileInfoId)) {
                throw new DownloadExpcetion(LocaleUtils.getFileTokenExpried(fileToken));
            }
        } else {
            fileInfoId = fileToken;
        }
        LOGGER.debug("fileInfoId: " + fileInfoId);
        download(outputStream, fileInfoId, configuration, isThumbnail, width, height, true);
    }

    @Override
    public void download(String appId, String configurationKey, String fileToken, Boolean isThumbnail, Integer width, Integer height) {
        try (OutputStream outputStream = response.getOutputStream()) {
            download(appId, configurationKey, fileToken, isThumbnail, width, height, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    @Override
    public void downloadZipped(String appId, String configurationKey, String downloadToken) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        String formId = downloadToken;
        if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
            formId = cacheUtil.get(downloadToken, String.class);
            if (formId == null) {
                throw new DownloadExpcetion(LocaleUtils.getFileTokenExpried(downloadToken));
            }
        }
        List<FileInfoDO> fileInfos = listByFormId(configuration.getId(), formId);
        try (ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream())) {
            if (fileInfos.size() > 0) {
                FileInfoDO firstFileInfo = fileInfos.get(0);
                initResponseHeader("application/zip", firstFileInfo.getFileName() + "等" + fileInfos.size() + "个文件.zip", null);
                Map<String, Integer> fileNames = new HashMap<>(16);
                for (FileInfoDO fileInfo : fileInfos) {
                    fileInfo = getActualFileInfo(fileInfo);
                    String fileName = fileInfo.getFileName();
                    if (fileNames.containsKey(fileName)) {
                        Integer index = fileNames.get(fileName);
                        index++;
                        String suffix = fileName.substring(fileName.lastIndexOf("."));
                        fileName = fileName.substring(0, fileName.length() - suffix.length());
                        fileName += "(" + index + ")" + suffix;
                        fileNames.put(fileInfo.getFileName(), index);
                    } else {
                        fileNames.put(fileInfo.getFileName(), 0);
                    }
                    outputStream.putNextEntry(new ZipEntry(fileName));
                    download(outputStream, configuration, fileInfo, false, null, null);
                    outputStream.closeEntry();
                }
            } else {
                outputStream.putNextEntry(new ZipEntry("没有可下载的文件.txt"));
                outputStream.closeEntry();
            }
            outputStream.setEncoding(ZIP_ENCODING.name());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("下载出错:" + e.getMessage());
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    private void initResponseHeader(String contentType, String fileName, Long contentLength) throws UnsupportedEncodingException {
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        // 火狐浏览器设置
        if (StringUtils.containsIgnoreCase(agent, "firefox")) {
            response.reset();
            response.setCharacterEncoding(UTF_8.name());
            // ContentType 可以不设置
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        } else {
            // 其他浏览器设置
            fileName = URLEncoder.encode(fileName, UTF_8.name());
            response.reset();
            response.setCharacterEncoding(UTF_8.name());
            // ContentType 可以不设置
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }
        if (contentLength != null) {
            response.setContentLengthLong(contentLength);
        }
    }

    @Override
    public FileInfoDO shareByFileId(String appId, String configurationKey, String fileId, String desConfigurationKey, String desFormId) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        FileInfoDO srcFileInfo = getByFileToken(configuration, fileId, true);
        FileConfigurationDO desConfiguration = fileConfigurationService.loadByKey(desConfigurationKey, appId);
        return shareFile(srcFileInfo, desConfiguration, desFormId);
    }

    private FileInfoDO shareFile(FileInfoDO srcFileInfo, FileConfigurationDO desConfiguration, String desFormId) {
        if (ObjectUtils.compare(srcFileInfo.getSecretLevel(), securityUtils.getCurrentUser().getSecretLevel()) < 0) {
            throw new ServerException(LocaleUtils.getFileSecretLevelNotMatch());
        }
        FileInfoDO desFileInfo = new FileInfoDO();
        desFileInfo.setFileConfigurationId(desConfiguration.getId());
        desFileInfo.setFileName(srcFileInfo.getFileName());
        desFileInfo.setFormId(desFormId);
        desFileInfo.setUploaderId(securityUtils.getCurrentUserId());
        desFileInfo.setUploadTime(LocalDateTime.now());
        desFileInfo.setSecretLevel(srcFileInfo.getSecretLevel());
        desFileInfo.setMd5(srcFileInfo.getMd5());
        desFileInfo.setContentType(srcFileInfo.getContentType());
        desFileInfo.setFileSize(srcFileInfo.getFileSize());
        desFileInfo.setEncryptedSize(srcFileInfo.getEncryptedSize());
        desFileInfo.setChunks(srcFileInfo.getChunks());
        desFileInfo.setNeedReview(desConfiguration.getEnableReview());
        desFileInfo.setFileRepositoryId(srcFileInfo.getFileRepositoryId());
        if (StringUtils.isEmpty(srcFileInfo.getSharedFileInfoId())) {
            desFileInfo.setSharedFileInfoId(srcFileInfo.getId());
        } else {
            desFileInfo.setSharedFileInfoId(srcFileInfo.getSharedFileInfoId());
        }
        desFileInfo.setImageRotationAngle(srcFileInfo.getImageRotationAngle());
        save(desFileInfo);
        sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件共享", "文件名：" + srcFileInfo.getFileName() + "，密级：" +
                securityUtils.getFileSecretLevelText(srcFileInfo.getSecretLevel()));
        if (IntegerUtils.isTrue(desConfiguration.getEnableDownloadToken())) {
            setDownloadToken(desFileInfo);
        }
        return desFileInfo;
    }

    @Override
    public String getActualFileId(FileConfigurationDO configuration, String fileToken) {
        String fileId = fileToken;
        LOGGER.debug("FILE_TOKEN: " + fileToken);
        if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
            if (StringUtils.isNotEmpty(fileToken)) {
                fileId = cacheUtil.get(fileToken);
                if (StringUtils.isEmpty(fileId)) {
                    throw new DownloadExpcetion(LocaleUtils.getFileTokenExpried(fileToken));
                }
            }
        }
        return fileId;
    }

    @Override
    public void deleteByFileId(String appId, String configurationKey, String fileToken) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        FileInfoDO fileInfo = getByFileToken(configuration, fileToken, true);
        delete(configuration, fileInfo);
    }

    @Override
    public void delete(FileConfigurationDO fileConfiguration, FileInfoDO fileInfo) {
        onFileDeleting(fileConfiguration, fileInfo);
        deleteFile(fileConfiguration, fileInfo);
    }

    private void deleteFile(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        checkUserSeparate(configuration, fileInfo);
        // 删除文件信息
        sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件删除", "文件名：" + fileInfo.getFileName() + "，密级：" + securityUtils.getFileSecretLevelText(fileInfo.getSecretLevel()));
        if (StringUtils.isEmpty(fileInfo.getSharedFileInfoId())) {
            // 如果不是共享文件
            // 获取共享本文件的文件列表
            List<FileInfoDO> sharingFileInfos = baseMapper.selectList(new QueryWrapper<FileInfoDO>().eq("SHARED_FILE_INFO_ID", fileInfo.getId()).orderByAsc("UPLOAD_TIME"));
            if (sharingFileInfos.size() > 0) {
                // 如果文件被共享，则将切片重定向给第一个共享该文件的文件信息，更新所有共享该文件的sharedFileInfoId
                FileInfoDO sharingFileInfo = sharingFileInfos.get(0);
                // 切片重定向
                fileChunkInfoService.update(new FileChunkInfoDO(), new UpdateWrapper<FileChunkInfoDO>().set("FILE_INFO_ID", sharingFileInfo.getId()).eq("FILE_INFO_ID", fileInfo.getId()));
                // 去除共享文件引用
                update(new FileInfoDO(), new UpdateWrapper<FileInfoDO>().set("SHARED_FILE_INFO_ID", null).eq("ID", sharingFileInfo.getId()));
                // 更新共享当前文件信息的文件信息
                update(new FileInfoDO(), new UpdateWrapper<FileInfoDO>().set("SHARED_FILE_INFO_ID", sharingFileInfo.getId()).eq("SHARED_FILE_INFO_ID", fileInfo.getId()));
                // 删除当前文件
                removeById(fileInfo.getId());
            } else {
                // 如果文件没被共享，则将文件移入回收站
                moveToRecycleBin(fileInfo.getId());
            }
        } else {
            // 如果是共享文件，直接删除
            removeById(fileInfo.getId());
        }
        // 20210712 物理删除
        deleteFilePhysical(fileInfo, fileRepositoryService.getByFileInfo(fileInfo));
    }

    @Value("${ace.config.file.deleteRecycle:true}")
    Boolean deleteRecycle;

    @Override
    public void moveToRecycleBin(String id) {
        if (!deleteRecycle) {
            String recycleId = UUID.randomUUID().toString();
            update(new FileInfoDO(), new UpdateWrapper<FileInfoDO>().set("ID", recycleId).set("RECYCLE_FILE_ID", id).set("IS_IN_RECYCLE_BIN", 1).set("RECYCLE_TIME", LocalDateTime.now()).set("RECYCLER_ID", securityUtils.getCurrentUserId()).eq("ID", id));
            fileChunkInfoService.update(new FileChunkInfoDO(), new UpdateWrapper<FileChunkInfoDO>().set("FILE_INFO_ID", recycleId).eq("FILE_INFO_ID", id));
        }
    }

    /**
     * 物理删除文件
     *
     * @param fileInfo 文件信息
     * @author JonnyJiang
     * @date 2020/7/8 11:22
     */
    @Override
    public void deleteFilePhysical(FileInfoDO fileInfo, FileRepositoryDO fileRepository) {
        AbstractFileRepository abstractFileRepository = AbstractFileRepository.getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, fileRepository);
        List<FileChunkInfoDO> chunkInfos = fileChunkInfoService.listByFileInfoId(fileInfo.getId());
        removeById(fileInfo.getId());
        abstractFileRepository.deleteChunks(fileChunkInfoService, fileInfo, chunkInfos);
    }

    private void checkUserSeparate(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        if (IntegerUtils.isTrue(configuration.getEnableUserSeparate())) {
            if (!StringUtils.equals(fileInfo.getUploaderId(), securityUtils.getCurrentUserId())) {
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.primary, "文件操作"), "文件删除", "删除他人文件被系统拒绝");
                throw new ServerException(LocaleUtils.getFileUserIsolation());
            }
        }
    }

    @Override
    public void deleteByFormId(String appId, String configurationKey, String formId) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        if (StringUtils.isNotEmpty(configuration.getDeleteAuthCode())) {
            if (!sysAuthMixService.hasAuthCodeWithUserId(securityUtils.getCurrentUserId(), configuration.getDeleteAuthCode(), appId)) {
                throw new DeleteException(LocaleUtils.getNoAccess(configuration.getDeleteAuthCode()));
            }
        }
        List<FileInfoDO> files = listByFormId(configuration.getId(), formId);
        deleteFiles(configuration, formId, files);
    }

    private void deleteFiles(FileConfigurationDO configuration, String formId, List<FileInfoDO> files) {
        Map<String, Object> map = new HashMap<>(16);
        map.put(EventListener.VARNAME_FORM_ID, formId);
        List<FileInfo> fileInfos = new ArrayList<>();
        for (FileInfoDO file : files) {
            fileInfos.add(file.toFileInfo(configuration));
        }
        map.put(EventListener.VARNAME_FILE_INFO_LIST, JSONObject.toJSONString(fileInfos));
        try {
            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_DELETING_BY_FORM, map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException(e.getMessage());
        }
        FileRepositoryDO fileRepository = null;
        for (FileInfoDO fileInfo : files) {
            if (fileRepository == null || !fileRepository.getId().equals(fileInfo.getFileRepositoryId())) {
                fileRepository = fileRepositoryService.getByFileInfo(fileInfo);
            }
            deleteFile(configuration, fileInfo);
        }
    }

    private QueryWrapper<FileInfoDO> getQueryByFormId(String configurationId, String formId) {
        return new QueryWrapper<FileInfoDO>().eq("FILE_CONFIGURATION_ID", configurationId).eq("FORM_ID", formId).and(o -> o.isNull("IS_IN_RECYCLE_BIN").or().eq("IS_IN_RECYCLE_BIN", 0)).orderByAsc("UPLOAD_TIME");
    }

    @Override
    public List<FileInfoDO> listByFormId(String configurationId, String formId) {
//        baseMapper.deleteInvalidFilesByFormId(configurationId, formId);
        return baseMapper.selectList(getQueryByFormId(configurationId, formId));
    }

    @Override
    public Integer getCountByFormId(FileConfigurationDO configuration, String formId) {
        return count(getQueryByFormId(configuration.getId(), formId));
    }

    @Override
    public void exportZip(ZipOutputStream zipOutputStream, ExportInfo exportInfo) throws IOException {
        ExportApp exportApp = new ExportApp(exportInfo.getAppId());
        exportApp.appId = exportInfo.getAppId();
        ExportConfiguration exportConfiguration = new ExportConfiguration(exportInfo.getConfigurationKey());
        exportConfiguration.configurationKey = exportInfo.getConfigurationKey();
        for (String formId : exportInfo.getFormIds()) {
            exportConfiguration.addFormId(formId);
        }
        exportApp.exportConfigurations.add(exportConfiguration);
        exportZip(zipOutputStream, exportApp);
    }

    private void putNextEntry(ZipOutputStream zipOutputStream, String name, byte[] bytes) throws IOException {
        ZipEntry zipEntry = new ZipEntry(name);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(bytes);
        zipOutputStream.closeEntry();
    }

    private void initExportZipResponse() throws UnsupportedEncodingException {
        response.setContentType("application/zip");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssSSS");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(simpleDateFormat.format(new Date()) + ".zip", "UTF-8"));
    }

    @Override
    public void exportZip(ExportInfo exportInfo) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            initExportZipResponse();
            exportZip(zipOutputStream, exportInfo);
            zipOutputStream.setEncoding(ZIP_ENCODING.name());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    private class ExportConfiguration {
        private String configurationKey;
        private List<String> formIds = new ArrayList<>();

        public ExportConfiguration(String configurationKey) {
            this.configurationKey = configurationKey;
        }

        public void addFormId(String formId) {
            if (!formIds.contains(formId)) {
                formIds.add(formId);
            }
        }
    }

    private class ExportApp {
        private String appId;
        private List<ExportConfiguration> exportConfigurations = new ArrayList<>();

        private ExportApp(String appId) {
            this.appId = appId;
        }

        public ExportConfiguration getExportConfiguration(String configurationKey) {
            return exportConfigurations.stream().filter(o -> StringUtils.equals(o.configurationKey, configurationKey)).findAny().orElse(null);
        }

        public void addExportConfiguration(ExportConfiguration exportConfiguration) {
            exportConfigurations.add(exportConfiguration);
        }
    }

    @Override
    public void exportZipBatch(List<ExportInfo> exportInfos) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            initExportZipResponse();
            List<ExportApp> exportApps = new ArrayList<>();
            for (ExportInfo exportInfo : exportInfos) {
                ExportApp exportApp = exportApps.stream().filter(o -> StringUtils.equals(o.appId, exportInfo.getAppId())).findAny().orElse(null);
                if (exportApp == null) {
                    exportApp = new ExportApp(exportInfo.getAppId());
                    exportApps.add(exportApp);
                }
                ExportConfiguration exportConfiguration = exportApp.getExportConfiguration(exportInfo.getConfigurationKey());
                if (exportConfiguration == null) {
                    exportConfiguration = new ExportConfiguration(exportInfo.getConfigurationKey());
                    exportApp.addExportConfiguration(exportConfiguration);
                }
                for (String formId : exportInfo.getFormIds()) {
                    exportConfiguration.addFormId(formId);
                }
            }
            for (ExportApp exportApp : exportApps) {
                exportZip(zipOutputStream, exportApp);
            }
            zipOutputStream.setEncoding(ZIP_ENCODING.name());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    public void exportZip(ZipOutputStream zipOutputStream, ExportApp exportApp) throws IOException {
        byte[] bytes = exportApp.appId.getBytes(UTF_8);
        String appIdFolder = Base64.getEncoder().encodeToString(bytes);
        putNextEntry(zipOutputStream, appIdFolder + "/info", bytes);
        for (ExportConfiguration exportConfiguration : exportApp.exportConfigurations) {
            FileConfigurationDO configuration = fileConfigurationService.loadByKey(exportConfiguration.configurationKey, exportApp.appId);
            bytes = exportConfiguration.configurationKey.getBytes(UTF_8);
            String configurationKeyFolder = appIdFolder + "/" + Base64.getEncoder().encodeToString(bytes);
            // 写入配置信息
            putNextEntry(zipOutputStream, configurationKeyFolder + "/info", JSONObject.toJSONBytes(configuration.toFileConfiguration()));
            for (String formId : exportConfiguration.formIds) {
                exportZip(zipOutputStream, configuration, formId, configurationKeyFolder);
            }
        }
    }

    private void exportZip(ZipOutputStream zipOutputStream, FileConfigurationDO configuration, String formId, String configurationKeyFolder) throws IOException {
        List<FileInfoDO> fileInfos = listByFormId(configuration.getId(), formId);
        String formIdFolder = configurationKeyFolder + "/" + Base64.getEncoder().encodeToString(formId.getBytes(UTF_8));
        // 写入文件信息
        putNextEntry(zipOutputStream, formIdFolder + "/info", JSONObject.toJSONString(fileInfos).getBytes(UTF_8));
        ZipEntry zipEntry;
        String fileName;
        for (FileInfoDO fileInfo : fileInfos) {
            fileName = Base64.getEncoder().encodeToString(fileInfo.getId().getBytes(UTF_8));
            zipEntry = new ZipEntry(formIdFolder + "/files/" + fileName);
            zipOutputStream.putNextEntry(zipEntry);
            download(zipOutputStream, fileInfo.getId(), configuration, false);
            zipOutputStream.closeEntry();
        }
    }

    @Override
    public List<FileInfoDO> listByFormId(FileConfigurationDO configuration, String formId) {
        if (StringUtils.isEmpty(formId)) {
            return new ArrayList<>();
        } else {
            if (IntegerUtils.isTrue(configuration.getEnableDownloadToken())) {
                return listByFormIdWithDownloadToken(configuration.getId(), formId);
            } else {
                return listByFormId(configuration.getId(), formId);
            }
        }
    }

    @Override
    public void setDownloadToken(FileInfoDO fileInfo) {
        String id = fileInfo.getId();
        fileInfo.setId(UUID.randomUUID().toString());
        cacheUtil.set(fileInfo.getId(), id, AbstractFileRepository.FILE_TOKEN_CONFIG_EXPIRE);
    }

    @Override
    public List<FileInfoDO> listByFormIdWithDownloadToken(String configurationId, String formId) {
        List<FileInfoDO> files = listByFormId(configurationId, formId);
        for (FileInfoDO file : files) {
            setDownloadToken(file);
        }
        return files;
    }

    @Override
    public FileInfoDO getByFileToken(String appId, String configurationKey, String fileToken) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        return getByFileToken(configuration, fileToken);
    }

    @Override
    public void shareByFormId(String appId, String configurationKey, String formId, String desConfigurationKey, String desFormId) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        List<FileInfoDO> fileInfos = listByFormId(configuration.getId(), formId);
        FileConfigurationDO desConfiguration = fileConfigurationService.loadByKey(desConfigurationKey, appId);
        for (FileInfoDO file : fileInfos) {
            shareFile(file, desConfiguration, desFormId);
        }
    }

    @Override
    public void downloadFirstFile(String appId, String configurationKey, String formId) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        List<FileInfoDO> fileInfos = listByFormId(configuration.getId(), formId);
        try (OutputStream outputStream = response.getOutputStream()) {
            if (fileInfos.size() > 0) {
                FileInfoDO fileInfo = fileInfos.get(0);
                initResponseHeader(fileInfo.getContentType(), fileInfo.getFileName(), fileInfo.getFileSize());
                download(outputStream, fileInfos.get(0).getId(), configuration, true);
            } else {
                outputStream.write(LocaleUtils.getFileNotExist().getBytes("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    @Override
    public void deleteAllByFormId(String formId) {
        List<FileInfoDO> allFiles = listAllByFormId(formId);
        List<String> configurationIds = allFiles.stream().map(FileInfoDO::getFileConfigurationId).distinct().collect(Collectors.toList());
        for (String configurationId : configurationIds) {
            List<FileInfoDO> fileInfos = new ArrayList<>();
            for (int i = 0; i < allFiles.size(); ) {
                FileInfoDO fileInfo = allFiles.get(i);
                if (Objects.equals(fileInfo.getFileConfigurationId(), configurationId)) {
                    fileInfos.add(fileInfo);
                    allFiles.remove(fileInfo);
                } else {
                    i++;
                }
            }
            if (fileInfos.size() > 0) {
                FileConfigurationDO configuration = fileConfigurationService.loadById(configurationId);
                deleteFiles(configuration, formId, fileInfos);
            }
        }
    }

    @Override
    public FileInfoDO getActualFileInfo(FileConfigurationDO configuration, String fileToken) {
        String fileId = getActualFileId(configuration, fileToken);
        return getById(fileId);
    }

    @Override
    public void preview(String appId, String configurationKey, String fileToken) {
        FileConfigurationDO configuration = fileConfigurationService.loadByKey(configurationKey, appId);
        FileInfoDO fileInfo = getActualFileInfo(configuration, fileToken);
        preview(fileInfo, configuration, response);
    }

    private List<FileInfoDO> listAllByFormId(String formId) {
        // 首先清理无效附件
        baseMapper.deleteAllInvalidFilesByFormId(formId);
        return baseMapper.selectList(new QueryWrapper<FileInfoDO>().eq("FORM_ID", formId).orderByAsc("UPLOAD_TIME"));
    }

    private void download(OutputStream outputStream, String fileInfoId, FileConfigurationDO configuration, Boolean initReponseHeaderByFile) throws IOException {
        download(outputStream, fileInfoId, configuration, false, null, null, initReponseHeaderByFile);
    }

    /**
     * 单文件下载
     *
     * @param outputStream 输出流
     * @param fileInfo     文件信息
     * @param isThumbnail  是否预览图
     * @param width        长度
     * @param height       宽度
     * @author JonnyJiang
     * @date 2019/5/22 19:19
     */

    private void download(OutputStream outputStream, FileInfoDO fileInfo, Boolean isThumbnail, Integer width, Integer height) throws IOException {
        LOGGER.debug("download begin");
        // 文件最终指向的附件库
        FileRepositoryDO fileRepository = fileRepositoryService.getByFileInfo(fileInfo);
        AbstractFileRepository abstractFileRepository = AbstractFileRepository.getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, fileRepository);
        if (isThumbnail && AbstractFileRepository.isImageFile(AbstractFileRepository.getSuffix(fileInfo.getFileName()))) {
            downloadThumbnail(outputStream, abstractFileRepository, fileInfo, width, height);
        } else {
            abstractFileRepository.download(outputStream, fileChunkInfoService, fileInfo);
        }
        LOGGER.debug("download end");
    }

    private void checkDownloadAuth(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        Integer secretLevel;
        LOGGER.debug("allow download without login: " + configuration.getAllowDownloadWithoutLogin());
        if (IntegerUtils.isFalse(configuration.getAllowDownloadWithoutLogin())) {
            if (StringUtils.isNotEmpty(configuration.getDownloadAuthCode())) {
                if (!sysAuthMixService.hasAuthCodeWithUserId(securityUtils.getCurrentUserId(), configuration.getDownloadAuthCode(), configuration.getAppId())) {
                    throw new DownloadExpcetion(LocaleUtils.getNoAccess(configuration.getDownloadAuthCode()));
                }
            }
            secretLevel = securityUtils.getCurrentUser().getSecretLevel();
        } else {
            try {
                secretLevel = securityUtils.getCurrentUser().getSecretLevel();
            } catch (Exception e) {
                secretLevel = SecurityUtils.MIN_SECRET_LEVEL;
            }
        }
        if (IntegerUtils.isTrue(configuration.getEnableSecretLevel())) {
            LOGGER.debug("user secret level: " + secretLevel);
            if (SecurityUtils.compareSecretLevel(fileInfo.getSecretLevel(), secretLevel) > 0) {
                throw new DownloadExpcetion(LocaleUtils.getFileSecretLevelNotMatch());
            }
        }
    }

    private void onFilePreviewing(FileConfigurationDO configuration, FileInfoDO fileInfo) {
        checkDownloadAuth(configuration, fileInfo);
//        Map<String, Object> map = new HashMap<>(16);
//        map.put(EventListener.VARNAME_FILE_INFO, fileInfo.getFileInfoJson(configuration));
//        try {
//            EventUtils.notify(configuration, EventListener.EVENTNAME_FILE_DOWNLOADING, map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new DownloadExpcetion(e.getMessage());
//        }
    }

    private void preview(FileInfoDO fileInfo, FileConfigurationDO configuration, HttpServletResponse response) {
        //文件信息
        onFilePreviewing(configuration, fileInfo);
        // 最终指向的文件
        fileInfo = getActualFileInfo(fileInfo.getId());
        // 根据最终文件重新查找配置信息
        configuration = fileConfigurationService.getByFile(fileInfo);
        // 文件最终指向的附件库
        FileRepositoryDO fileRepository = fileRepositoryService.getByFileInfo(fileInfo);
        AbstractFileRepository abstractFileRepository = AbstractFileRepository.getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, fileRepository);
        preview(abstractFileRepository, fileInfo);
        onFilePreviewed(configuration, fileInfo);
    }

    private void onFilePreviewed(FileConfigurationDO configuration, FileInfoDO fileInfo) {

    }

    private FileInfoDO getByFileToken(FileConfigurationDO configuration, String fileToken) {
        return getByFileToken(configuration, fileToken, false);
    }

    private FileInfoDO getByFileToken(FileConfigurationDO configuration, String fileToken, Boolean check) {
        String fileId = getActualFileId(configuration, fileToken);
        FileInfoDO fileInfo = baseMapper.selectById(fileId);
        if (check) {
            if (fileInfo == null) {
                throw new ServerException(LocaleUtils.getFileNotFoundById(fileId));
            }
        }
        return fileInfo;
    }

    private Date parseTime(String dateString) {
        dateString = dateString.replace("GMT", "").replaceAll("\\(.*\\)", "");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
        Date dateTrans = null;
        try {
            dateTrans = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTrans;
    }

    /**
     * 单文件下载
     *
     * @param abstractFileRepository
     * @param fileInfo               文件信息
     * @param isThumbnail            是否预览图
     * @param width                  长度
     * @param height                 宽度
     * @return
     * @author FourLeaves
     * @date 2020/4/16 14:14
     */
    public InputStream getFileStream(AbstractFileRepository abstractFileRepository, FileInfoDO fileInfo, Boolean isThumbnail, Integer width, Integer height) {
        byte[] key = Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
        if (isThumbnail && AbstractFileRepository.isImageFile(AbstractFileRepository.getSuffix(fileInfo.getFileName()))) {
            return getFileStreamThumbnail(abstractFileRepository, key, fileInfo, width, height);
        } else {
            return abstractFileRepository.getFileStream(fileChunkInfoService, fileInfo);
        }
    }

    private void downloadThumbnail(OutputStream outputStream, AbstractFileRepository abstractFileRepository, FileInfoDO fileInfo, Integer width, Integer height) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        abstractFileRepository.download(byteArrayOutputStream, fileChunkInfoService, fileInfo);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Thumbnailator.createThumbnail(byteArrayInputStream, outputStream, width, height);
    }

    private InputStream getFileStreamThumbnail(AbstractFileRepository abstractFileRepository, byte[] key, FileInfoDO fileInfo, Integer width, Integer height) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        abstractFileRepository.download(byteArrayOutputStream, fileChunkInfoService, fileInfo);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnailator.createThumbnail(byteArrayInputStream, outputStream, width, height);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("下载出错:" + e.getMessage());
            throw new DownloadExpcetion(LocaleUtils.getFileDownloadError(e.getMessage()));
        }
    }

    private void preview(AbstractFileRepository abstractFileRepository, FileInfoDO fileInfo) {
        try (OutputStream outputStream = response.getOutputStream()) {
            String suffix = AbstractFileRepository.getSuffix(fileInfo.getFileName());
            if (AbstractFileRepository.isImageFile(suffix)) {
                abstractFileRepository.download(outputStream, fileChunkInfoService, fileInfo);
            } else if (AbstractFileRepository.isMsWord(suffix)) {
                previewWord(suffix, fileInfo, abstractFileRepository, outputStream);
            } else if (AbstractFileRepository.isMsExcel(suffix)) {
                previewExcel(suffix, fileInfo, abstractFileRepository, outputStream);
            } else if (AbstractFileRepository.isMsPpt(suffix)) {
                previewPpt(suffix, fileInfo, abstractFileRepository, outputStream);
            } else if (AbstractFileRepository.isPdf(suffix)) {
                abstractFileRepository.download(outputStream, fileChunkInfoService, fileInfo);
            } else {
                throw new PreviewException(LocaleUtils.getFileTypeNotSupport(suffix));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new PreviewException(e.getMessage());
        }
    }

    private void previewWord(String suffix, FileInfoDO fileInfo, AbstractFileRepository abstractFileRepository, OutputStream outputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        LOGGER.trace("chunks loading...");
        abstractFileRepository.download(byteArrayOutputStream, fileChunkInfoService, fileInfo);
        LOGGER.trace("chunks loaded.");
        String fileName = fileInfo.getFileName().substring(0, fileInfo.getFileName().length() - suffix.length()) + "pdf";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = new Document(byteArrayInputStream);
        com.aspose.words.PdfSaveOptions saveOptions = new com.aspose.words.PdfSaveOptions();
        saveOptions.getOutlineOptions().setHeadingsOutlineLevels(4);
        saveOptions.getOutlineOptions().setExpandedOutlineLevels(4);
        document.save(outputStream, saveOptions);
    }

    private void previewExcel(String suffix, FileInfoDO fileInfo, AbstractFileRepository abstractFileRepository, OutputStream outputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        LOGGER.trace("chunks loading...");
        abstractFileRepository.download(byteArrayOutputStream, fileChunkInfoService, fileInfo);
        LOGGER.trace("chunks loaded.");
        String fileName = fileInfo.getFileName() + ".pdf";
        initResponseHeader("application/pdf", fileName, fileInfo.getFileSize());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Workbook workbook = new Workbook(byteArrayInputStream);
        workbook.save(outputStream, com.aspose.cells.SaveFormat.PDF);
    }

    private void previewPpt(String suffix, FileInfoDO fileInfo, AbstractFileRepository abstractFileRepository, OutputStream outputStream) throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        LOGGER.trace("chunks loading...");
        abstractFileRepository.download(byteArrayOutputStream, fileChunkInfoService, fileInfo);
        LOGGER.trace("chunks loaded.");
        String fileName = fileInfo.getFileName() + ".pdf";
        initResponseHeader("application/pdf", fileName, fileInfo.getFileSize());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Presentation presentation = new Presentation(byteArrayInputStream);
        presentation.save(outputStream, com.aspose.slides.SaveFormat.Pdf);
    }
}