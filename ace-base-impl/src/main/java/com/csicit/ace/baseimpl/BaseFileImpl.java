package com.csicit.ace.baseimpl;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.file.ExceptConfiguration;
import com.csicit.ace.common.pojo.domain.file.ExceptForm;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.file.FileUtil;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author JonnyJiang
 * @date 2020/7/9 11:22
 */
public abstract class BaseFileImpl extends BaseImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseFileImpl.class);
    public static final String APP_ID = "appName";

    protected abstract void doDeleteByFileId(String configurationKey, String fileId);

    protected abstract void doDeleteByFormId(String configurationKey, String formId);

    protected abstract void doDeleteAllByFormId(String formId);

    protected abstract FileInfoDO doShareByFileId(String configurationKey, String fileId, String desConfigurationKey, String desFormId);

    protected abstract void doShareByFormId(String configurationKey, String formId, String desConfigurationKey, String desFormId);

    protected abstract List<FileInfoDO> doListByFormId(String configurationKey, String formId);

    protected abstract FileInfoDO doGetByFileId(String configurationKey, String fileId);

    protected abstract InputStream doDownload(String configurationKey, String fileToken) throws IOException;

    protected abstract FileConfiguration doLoadFileConfigurationByKey(String configurationKey);

    protected abstract FileInfoDO doAllocateSpace(FileVO file);

    protected abstract void doUpload(String configurationKey, String yfId, Integer chunkIndex, Integer chunks, byte[] bytes, Long size) throws Exception;

    protected abstract InputStream doExportZip(String configurationKey, String formId, String appId) throws IOException;

    protected abstract InputStream doExportZipBatch(List<ExportInfo> exportInfos) throws IOException;

    protected abstract void downloadZip(String appId, String configurationKey, String downloadToken) throws IOException;

    protected abstract R addReviewFile(String formId);

    public R addReview(String formId) {
        return addReviewFile(formId);
    }
    protected abstract R setReviewFile(String formId);

    public R setReview(String formId) {
        return setReviewFile(formId);
    }

    public void deleteFileByFileId(String configurationKey, String fileId) {
        doDeleteByFileId(configurationKey, fileId);
    }

    public void deleteFileByFormId(String configurationKey, String formId) {
        doDeleteByFormId(configurationKey, formId);
    }

    public void deleteAllByFormId(String formId) {
        doDeleteAllByFormId(formId);
    }

    public List<FileInfoDO> listFileByFormId(String configurationKey, String formId) {
        return doListByFormId(configurationKey, formId);
    }

    public FileInfoDO getFile(String configurationKey, String fileId) {
        return doGetByFileId(configurationKey, fileId);
    }

    public FileInfoDO shareFile(String configurationKey, String fileId, String desConfigurationKey, String desFormId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("fileId", fileId);
        map.put("desConfigurationKey", desConfigurationKey);
        map.put("desFormId", desFormId);
        map.put(APP_ID, appName);
        return doShareByFileId(configurationKey, fileId, desConfigurationKey, desFormId);
    }

    public void shareFileByFormId(String configurationKey, String formId, String desConfigurationKey, String desFormId) {
        doShareByFormId(configurationKey, formId, desConfigurationKey, desFormId);
    }

    public InputStream download(String configurationKey, String fileToken) throws IOException {
        return doDownload(configurationKey, fileToken);
    }

    public void downloadZipped(String appId, String configurationKey, String downloadToken) throws IOException {
        downloadZip(appId,configurationKey, downloadToken);
    }

    public FileInfoDO upload(String configurationKey, String formId, String fileName, Long fileSize, InputStream inputStream) throws Exception {
        return upload(configurationKey, formId, fileName, fileSize, inputStream, null);
    }

    public FileInfoDO upload(String configurationKey, String formId, String fileName, Long fileSize, InputStream inputStream, Integer secretLevel) throws Exception {
        return upload(configurationKey, formId, null, fileName, fileSize, inputStream, secretLevel);
    }

    private FileInfoDO upload(String configurationKey, String formId, String fileId, String fileName, Long fileSize, InputStream inputStream, Integer secretLevel) throws Exception {
        LOGGER.debug("uploading...");
        LOGGER.debug("configurationKey: " + configurationKey);
        LOGGER.debug("formId: " + formId);
        LOGGER.debug("fileName: " + fileName);
        LOGGER.debug("fileSize: " + fileSize);
        // 获取附件配置
        FileConfiguration fileConfiguration = doLoadFileConfigurationByKey(configurationKey);
        String suffix = FileUtil.getSuffix(fileName);
        // 分配空间
        FileVO file = new FileVO();
        if (StringUtils.isNotEmpty(fileId)) {
            file.setId(fileId);
        }
        file.setYfId(UUID.randomUUID().toString());
        file.setFileName(fileName);
        file.setFormId(formId);
        if (IntegerUtils.isFalse(fileConfiguration.getEnableSecretLevel())) {
            // 如果不启用文件密级
            file.setSecretLevel(SecurityUtils.MIN_SECRET_LEVEL);
        } else {
            // 如果启用文件密级
            if (StringUtils.isEmpty(fileId)) {
                if (secretLevel == null) {
                    throw new RuntimeException("secret level cannot be null");
                }
                if (securityUtils.listAvailableFileSecretLevel().stream().noneMatch(o -> o.equals(secretLevel))) {
                    throw new RuntimeException("secret level invalid");
                }
            }
            file.setSecretLevel(secretLevel);
        }
        file.setContentType(FileUtil.getContentType(suffix));
        file.setFileSize(fileSize);
        Integer chunks = 1;
        if (fileConfiguration.getChunkSize() == null) {
            throw new RuntimeException("chunk size cannot be null");
        }
        if (fileConfiguration.getChunkSize() > 0) {
            chunks = Integer.valueOf(String.valueOf((fileSize / fileConfiguration.getChunkSize())));
            if (fileSize % fileConfiguration.getChunkSize() > 0) {
                chunks += 1;
            }
        }
        file.setChunks(chunks);
        file.setConfigurationKey(configurationKey);
        file.setAppName(appName);
        LOGGER.debug("file: " + file);
        LOGGER.debug("space allocating");
        FileInfoDO fileInfoDO = doAllocateSpace(file);
        LOGGER.debug("space allocated");
        LOGGER.debug("fileInfo: " + fileInfoDO);
        Long pendingFileSize = fileSize;
        Long size;
        for (int i = 0; i < chunks; i++) {
            if (pendingFileSize >= fileConfiguration.getChunkSize()) {
                size = fileConfiguration.getChunkSize();
                pendingFileSize -= fileConfiguration.getChunkSize();
            } else {
                size = pendingFileSize;
                pendingFileSize = 0L;
            }
            byte[] bytes = new byte[Integer.valueOf(String.valueOf(size))];
            int length;
            int offset = 0;
            while (offset < bytes.length && (length = inputStream.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += length;
            }
            if (offset < bytes.length) {
                throw new RuntimeException("input stream missing");
            }
            doUpload(configurationKey, fileInfoDO.getId(), i, chunks, bytes, fileInfoDO.getFileSize());
        }
        LOGGER.debug("uploaded");
        return fileInfoDO;
    }

    public InputStream exportZip(String configurationKey, String formId) throws IOException {
        return doExportZip(configurationKey, formId, appName);
    }



    public InputStream exportZipBatch(ExportInfo exportInfo) throws IOException {
        List<ExportInfo> exportInfos = new ArrayList<>();
        exportInfos.add(exportInfo);
        return doExportZipBatch(exportInfos);
    }

    public InputStream exportZipBatch(List<ExportInfo> exportInfos) throws IOException {
        return doExportZipBatch(exportInfos);
    }

    public void importZip(InputStream inputStream) throws Exception {
        importZip(inputStream, null, null, null);
    }

    public void importZipExceptApp(InputStream inputStream, String appId) throws Exception {
        List<String> appIds = new ArrayList<>();
        appIds.add(appId);
        importZipExceptApp(inputStream, appIds);
    }

    public void importZipExceptApp(InputStream inputStream, List<String> appIds) throws Exception {
        if (appIds == null) {
            appIds = new ArrayList<>();
        }
        importZip(inputStream, appIds, null, null);
    }

    public void importZipExceptConfiguration(InputStream inputStream, ExceptConfiguration exceptConfiguration) throws Exception {
        List<ExceptConfiguration> exceptConfigurations = new ArrayList<>();
        exceptConfigurations.add(exceptConfiguration);
        importZipExceptConfiguration(inputStream, exceptConfigurations);
    }

    public void importZipExceptConfiguration(InputStream inputStream, List<ExceptConfiguration> exceptConfigurations) throws Exception {
        if (exceptConfigurations == null) {
            exceptConfigurations = new ArrayList<>();
        }
        importZip(inputStream, null, exceptConfigurations, null);
    }

    public void importZipExceptForm(InputStream inputStream, ExceptForm exceptForm) throws Exception {
        List<ExceptForm> exceptForms = new ArrayList<>();
        exceptForms.add(exceptForm);
        importZipExceptForm(inputStream, exceptForms);
    }

    public void importZipExceptForm(InputStream inputStream, List<ExceptForm> exceptForms) throws Exception {
        if (exceptForms == null) {
            exceptForms = new ArrayList<>();
        }
        importZip(inputStream, null, null, exceptForms);
    }

    /**
     * @param inputStream          文件流
     * @param appIds               排除应用
     * @param exceptConfigurations 排除配置
     * @param exceptForms          排除表单
     * @return void
     * @author JonnyJiang
     * @date 2020/7/23 17:12
     */

    private void importZip(InputStream inputStream, List<String> appIds, List<ExceptConfiguration> exceptConfigurations, List<ExceptForm> exceptForms) throws Exception {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            resolveZipApp(zipInputStream, appIds, exceptConfigurations, exceptForms, zipInputStream.getNextEntry());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ByteArrayOutputStream getZipEntryStream(ZipInputStream zipInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int length;
        byte[] bytes = new byte[StreamUtils.BUFFER_SIZE];
        while ((length = zipInputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, length);
        }
        return byteArrayOutputStream;
    }

    /**
     * 处理APP信息
     *
     * @author JonnyJiang
     * @date 2020/7/15 15:54
     */

    private void resolveZipApp(ZipInputStream zipInputStream, List<String> appIds, List<ExceptConfiguration> exceptConfigurations, List<ExceptForm> exceptForms, ZipEntry zipEntry) throws Exception {
        if (zipEntry != null) {
            LOGGER.debug("name: " + zipEntry.getName());
            ByteArrayOutputStream byteArrayOutputStream = getZipEntryStream(zipInputStream);
            String appId = new String(byteArrayOutputStream.toByteArray(), UTF_8);
            byteArrayOutputStream.close();
            LOGGER.debug("appId: " + appId);
            Boolean exceptApp = appIds != null && appIds.contains(appId);
            LOGGER.debug("except app: " + exceptApp);
            byte[] bytes = appId.getBytes(UTF_8);
            String appIdFolder = Base64.getEncoder().encodeToString(bytes);
            String appIdPath = appIdFolder + "/";
            resolveZipConfiguration(zipInputStream, appIds, exceptConfigurations, exceptForms, exceptApp, appId, appIdPath, zipInputStream.getNextEntry());
        }
    }

    /**
     * 处理APP下的配置
     *
     * @param zipInputStream
     * @param appIds
     * @param exceptConfigurations
     * @param exceptForms
     * @param exceptApp
     * @param appId
     * @param appIdPath
     * @author JonnyJiang
     * @date 2020/7/15 15:57
     */

    private void resolveZipConfiguration(ZipInputStream zipInputStream, List<String> appIds, List<ExceptConfiguration> exceptConfigurations, List<ExceptForm> exceptForms, Boolean exceptApp, String appId, String appIdPath, ZipEntry zipEntry) throws Exception {
        if (zipEntry != null) {
            LOGGER.debug("name: " + zipEntry.getName());
            if (zipEntry.getName().startsWith(appIdPath)) {
                // 如果在APP文件夹下，则获取附件配置信息
                ByteArrayOutputStream byteArrayOutputStream = getZipEntryStream(zipInputStream);
                FileConfiguration fileConfiguration = JSONObject.parseObject(byteArrayOutputStream.toByteArray(), FileConfiguration.class);
                byteArrayOutputStream.close();
                LOGGER.debug("configurationKey: " + fileConfiguration.getConfigurationKey());
                Boolean exceptConfiguration;
                if (exceptApp) {
                    exceptConfiguration = true;
                } else {
                    exceptConfiguration = exceptConfigurations != null && exceptConfigurations.stream().anyMatch(o -> StringUtils.equals(appId, o.getAppId()) && o.getConfigurationKeys().contains(fileConfiguration.getConfigurationKey()));
                }
                LOGGER.debug("except configuration: " + exceptConfiguration);
                byte[] bytes = fileConfiguration.getConfigurationKey().getBytes(UTF_8);
                String configurationKeyFolder = Base64.getEncoder().encodeToString(bytes);
                String configurationKeyPath = appIdPath + configurationKeyFolder + "/";
                resolveZipForm(zipInputStream, appIds, exceptConfigurations, exceptForms, exceptApp, appId, appIdPath, exceptConfiguration, configurationKeyPath, fileConfiguration, zipInputStream.getNextEntry());
            } else {
                // 如果不在APP文件夹下，则获取下一个APP文件夹，暂不支持跨应用导入导出文件
//                resolveZipApp(zipInputStream, zipEntry);
            }
        }
    }

    private void resolveZipForm(ZipInputStream zipInputStream, List<String> appIds, List<ExceptConfiguration> exceptConfigurations, List<ExceptForm> exceptForms, Boolean exceptApp, String appId, String appIdPath, Boolean exceptConfiguration, String configurationKeyPath, FileConfiguration fileConfiguration, ZipEntry zipEntry) throws Exception {
        if (zipEntry != null) {
            LOGGER.debug("name: " + zipEntry.getName());
            // 获取文件列表
            if (zipEntry.getName().startsWith(configurationKeyPath)) {
                // 从文件地址中截取formId，最后5位为/info
                String formIdFolder = zipEntry.getName().substring(0, zipEntry.getName().length() - 5);
                String formIdCode = formIdFolder.substring(configurationKeyPath.length());
                String formId = new String(Base64.getDecoder().decode(formIdCode), UTF_8);
                LOGGER.debug("formId: " + formId);
                Boolean exceptForm;
                if (exceptConfiguration) {
                    exceptForm = true;
                } else {
                    exceptForm = exceptForms != null && exceptForms.stream().anyMatch(o -> StringUtils.equals(appId, o.getAppId()) && (!o.getNeedMatchConfigurationKey() || StringUtils.equals(fileConfiguration.getConfigurationKey(), o.getConfigurationKey())) && o.getFormIds().contains(formId));
                }
                LOGGER.debug("except form", exceptForm);
                String formIdPath = formIdFolder + "/";
                ByteArrayOutputStream byteArrayOutputStream = getZipEntryStream(zipInputStream);
                List<FileInfoDO> fileInfos = JSONObject.parseArray(new String(byteArrayOutputStream.toByteArray(), UTF_8), FileInfoDO.class);
                byteArrayOutputStream.close();
                LOGGER.debug("fileInfos: " + fileInfos);
                String filesPath = formIdPath + "files/";
                String fileCode;
                FileInfoDO fileInfo;
                for (int i = 0; i < fileInfos.size(); i++) {
                    zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) {
                        throw new RuntimeException("file quantity not match");
                    }
                    LOGGER.debug("name: " + zipEntry.getName());
                    if (zipEntry.getName().startsWith(filesPath)) {
                        fileCode = zipEntry.getName().substring(filesPath.length());
                        String fileId = new String(Base64.getDecoder().decode(fileCode), UTF_8);
                        fileInfo = fileInfos.stream().filter(o -> StringUtils.equals(o.getId(), fileId)).findAny().orElse(null);
                        if (fileInfo != null) {
                            LOGGER.debug("file match: " + fileInfo);
                            if (!exceptForm) {
                                upload(fileConfiguration.getConfigurationKey(), fileInfo.getFormId(), fileInfo.getId(), fileInfo.getFileName(), fileInfo.getFileSize(), zipInputStream, fileInfo.getSecretLevel());
                            }
                        }
                    }
                }
                resolveZipForm(zipInputStream, appIds, exceptConfigurations, exceptForms, exceptApp, appId, appIdPath, exceptConfiguration, configurationKeyPath, fileConfiguration, zipInputStream.getNextEntry());
            } else if (zipEntry.getName().startsWith(appIdPath)) {
                resolveZipConfiguration(zipInputStream, appIds, exceptConfigurations, exceptForms, exceptApp, appId, appIdPath, zipEntry);
            } else {
                resolveZipApp(zipInputStream, appIds, exceptConfigurations, exceptForms, zipEntry);
            }
        }
    }

    public FileInfoDO update(String configurationKey, String formId, String fileId, String fileName, Long fileSize, InputStream inputStream) throws Exception {
        return update(configurationKey, formId, fileId, fileName, fileSize, inputStream, null);
    }

    public FileInfoDO update(String configurationKey, String formId, String fileId, String fileName, Long fileSize, InputStream inputStream, Integer secretLevel) throws Exception {
        return upload(configurationKey, formId, fileId, fileName, fileSize, inputStream, secretLevel);
    }
}