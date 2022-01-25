package com.csicit.ace.fileserver.core.utils;

import com.csicit.ace.common.utils.internationalization.InternationUtils;

/**
 * 本地化工具
 *
 * @author JonnyJiang
 * @date 2019/12/6 14:04
 */
public class LocaleUtils {
    public static String getMessage(String key, Object... args) {
        return InternationUtils.getInternationalMsgWithPath("com.csicit.ace.fileserver.core.resources.locale", key, args);
    }

    public static String getNoAccess(String authCode) {
        return getMessage("FILE_SERVER1", authCode);
    }

    public static String getConfigurationKeyIsNull() {
        return getMessage("FILE_SERVER2");
    }

    public static String getConfigurationNotFoundByKey(String configurationKey, String appId) {
        return getMessage("FILE_SERVER3", configurationKey, appId);
    }

    public static String getConfigurationNotFoundById(String id) {
        return getMessage("FILE_SERVER4", id);
    }

    public static String getConfigurationKeyDuplicate(String configurationKey) {
        return getMessage("FILE_SERVER5", configurationKey);
    }

    public static String getGroupDatasourceNotSet() {
        return getMessage("FILE_SERVER6");
    }

    public static String getFileNotFoundById(String id) {
        return getMessage("FILE_SERVER7", id);
    }

    public static String getFileTokenExpried(String fileToken) {
        return getMessage("FILE_SERVER8", fileToken);
    }

    public static String getFileSecretLevelNotMatch() {
        return getMessage("FILE_SERVER9");
    }

    public static String getFileUserIsolation() {
        return getMessage("FILE_SERVER10");
    }

    public static String getFileNotExist() {
        return getMessage("FILE_SERVER11");
    }

    public static String getFileRepositoryInsufficient() {
        return getMessage("FILE_SERVER12");
    }

    public static String getDirectoryCreateError(String directory) {
        return getMessage("FILE_SERVER13", directory);
    }

    public static String getFileChunkDeleteError(String path) {
        return getMessage("FILE_SERVER14", path);
    }

    public static String getFileUploadError(String errorMessage) {
        return getMessage("FILE_SERVER15", errorMessage);
    }

    public static String getFileDownloadError(String errorMessage) {
        return getMessage("FILE_SERVER16", errorMessage);
    }

    public static String getRepositoryTypeNotSupport(Integer repositoryType) {
        return getMessage("FILE_SERVER17", repositoryType);
    }

    public static String getFileStreamMissing() {
        return getMessage("FILE_SERVER18");
    }

    public static String getFileEncryptError(String errorMessage) {
        return getMessage("FILE_SERVER19", errorMessage);
    }

    public static String getFileRepositoryNotFoundById(String id) {
        return getMessage("FILE_SERVER20", id);
    }

    public static String getChunkSizeExceeded(Long chunkSize, Long maxChunkSize) {
        return getMessage("FILE_SERVER21", chunkSize, maxChunkSize);
    }

    public static String getFileTypeNotSupport(String suffix) {
        return getMessage("FILE_SERVER22", suffix);
    }

    public static String getAcceptTitle() {
        return getMessage("FILE_SERVER23");
    }

    public static String getNotAllowUpload(String configurationKey) {
        return getMessage("FILE_SERVER24", configurationKey);
    }

    public static String getFileSizeLimit(Long fileSizeLimit, Long totalSize, Long fileSize) {
        return getMessage("FILE_SERVER25", fileSizeLimit, totalSize, fileSize);
    }

    public static String getAcceptNotFix(String accept, String suffix) {
        return getMessage("FILE_SERVER26", accept, suffix);
    }

    public static String getFileNumLimit(Integer fileNumLimit, int size) {
        return getMessage("FILE_SERVER27", fileNumLimit, size);
    }

    public static String getFileSingleSizeLimit(Long fileSingleSizeLimit, Long fileSize) {
        return getMessage("FILE_SERVER28", fileSingleSizeLimit, fileSize);
    }

    public static String getGroupAppNotFoundById(String appId) {
        return getMessage("FILE_SERVER29", appId);
    }

    public static String getFileConfigurationNotFoundById() {
        return getMessage("FILE_SERVER30");
    }

    public static String getNoAccessToDeleteFileFromRecycleBin() {
        return getMessage("FILE_SERVER31");
    }
}