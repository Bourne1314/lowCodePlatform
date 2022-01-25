package com.csicit.ace.fileserver.core.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.cipher.SM4Util;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.fileserver.core.DownloadExpcetion;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.fileserver.core.service.FileChunkInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.impl.BlockRepository;
import com.csicit.ace.fileserver.core.utils.impl.FtpRepository;
import com.csicit.ace.fileserver.core.utils.impl.OssRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * 文件操作类
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public abstract class AbstractFileRepository {
    /**
     * 附件配置信息在Redis中的过期时间
     */
    public static final int FILE_CONFIGURATION_CONFIG_EXPIRE = 60;
    /**
     * 附件配置Token在Redis中的过期时间
     */
    public static final int FILE_TOKEN_CONFIG_EXPIRE = 7200;
    /**
     * 日志记录工具
     */
    protected static final Logger logger = LoggerFactory.getLogger(AbstractFileRepository.class);

    private FileRepositoryService fileRepositoryService;
    private String fileRepositoryId;

    public AbstractFileRepository(FileRepositoryService fileRepositoryService, String fileRepositoryId) {
        this.fileRepositoryService = fileRepositoryService;
        this.fileRepositoryId = fileRepositoryId;
    }

    /**
     * 获取文件存储库id
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/5/22 19:16
     */

    public String getFileRepositoryId() {
        return fileRepositoryId;
    }

    /**
     * 转换文件路径
     *
     * @param filePath 转换后的文件路径
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/5/22 19:18
     */

    protected abstract String transformFilePath(String filePath);

    /**
     * 检查物理存储
     *
     * @param size 所需存储空间
     * @return boolean
     * @author JonnyJiang
     * @date 2019/5/22 19:19
     */

    protected abstract Boolean checkPhysicalStorage(Long size);

    protected void onSaveComplete(InputStream inputStream, Closeable closeable) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        }
    }

    /**
     * 保存切片
     *
     * @param chunkInfo   切片信息
     * @param inputStream 切片流
     * @author JonnyJiang
     * @date 2019/5/20 23:19
     */

    public void save(FileChunkInfoDO chunkInfo, InputStream inputStream) {
        chunkInfo.setChunkPath(transformFilePath(chunkInfo.getChunkPath()));
        createDirectory(chunkInfo.getChunkPath());
        Long encryptedSize = chunkInfo.getChunkSize();
        if (IntegerUtils.isTrue(chunkInfo.getEncrypted())) {
            encryptedSize = encryptSave(Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY), chunkInfo.getChunkPath(), inputStream);
        } else {
            normalSave(chunkInfo.getChunkPath(), inputStream);
        }
        chunkInfo.setEncryptedSize(encryptedSize);
    }

    /**
     * 加密存储
     *
     * @param key         密钥
     * @param chunkPath   存储路径
     * @param inputStream 上传的文件
     * @return 加密后文件大小
     * @author JonnyJiang
     * @date 2019/10/12 8:19
     */
    protected abstract Long encryptSave(byte[] key, String chunkPath, InputStream inputStream);

    /**
     * 通常存储（非加密存储）
     *
     * @param chunkPath  存储路径
     * @param fileStream 上传的文件
     * @author JonnyJiang
     * @date 2019/10/12 8:19
     */

    protected abstract void normalSave(String chunkPath, InputStream fileStream);

    public static String getSuffix(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            Integer index = fileName.lastIndexOf(".");
            String suffix = fileName.substring(index + 1).toLowerCase();
            return suffix;
        }
        return "";
    }

    public static Boolean isImageFile(String suffix) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(suffix)) {
            return Objects.equals(suffix, "jpg") || Objects.equals(suffix, "jpeg") || Objects.equals(suffix, "gif") || Objects.equals(suffix, "png") || Objects.equals(suffix, "bmp");
        }
        return false;
    }

    public static Boolean isPdf(String suffix) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(suffix)) {
            return Objects.equals(suffix, "pdf");
        }
        return false;
    }

    public static Boolean isMsWord(String suffix) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(suffix)) {
            return Objects.equals(suffix, "doc") || Objects.equals(suffix, "docx");
        }
        return false;
    }

    public static Boolean isMsExcel(String suffix) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(suffix)) {
            return Objects.equals(suffix, "xls") || Objects.equals(suffix, "xlsx");
        }
        return false;
    }

    public static Boolean isMsPpt(String suffix) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(suffix)) {
            return Objects.equals(suffix, "ppt") || Objects.equals(suffix, "pptx");
        }
        return false;
    }

    private byte[] getKey() {
        return Base64Utils.decodeFromUrlSafeString(GMBaseUtil.SM4KEY);
    }

    public void download(OutputStream outputStream, FileChunkInfoService fileChunkInfoService, FileInfoDO fileInfo) {
        byte[] key = getKey();
        loadChunks(fileChunkInfoService, fileInfo, key, outputStream);
    }

    /**
     * 获取文件块流
     *
     * @param chunkInfo 文件块信息
     * @return java.io.InputStream 文件块流
     * @author JonnyJiang
     * @date 2019/6/12 14:05
     */

    protected abstract InputStream getChunkInputStream(FileChunkInfoDO chunkInfo);

    /**
     * 创建存储路径
     *
     * @param path 路径
     * @author JonnyJiang
     * @date 2019/6/12 14:05
     */

    protected abstract void createDirectory(String path);

    /**
     * 获取满足空间大小的附件库
     *
     * @param fileRepositoryService 文件存储库服务
     * @param fileSize              空间大小
     * @return AbstractFileRepository
     * @author JonnyJiang
     * @date 2019/5/22 10:32
     */

    public static AbstractFileRepository getAvailableRepository(FileRepositoryService fileRepositoryService, SysAuditLogService sysAuditLogService, SecurityUtils securityUtils, long fileSize) {
        List<FileRepositoryDO> repositories = fileRepositoryService.list(new QueryWrapper<>());
        AbstractFileRepository repositoryIntf;
        for (FileRepositoryDO repository : repositories) {
            repositoryIntf = getFileRepository(fileRepositoryService, sysAuditLogService, securityUtils, repository);
            if (repositoryIntf.tryAllocationStorage(fileSize)) {
                return repositoryIntf;
            }
        }
        throw new UploadExpcetion(LocaleUtils.getFileRepositoryInsufficient());
    }

    /**
     * 获取附件操作对象
     *
     * @param fileRepositoryService 文件存储库服务
     * @param fileRepository        文件存储库
     * @return AbstractFileRepository 文件存储库操作对象
     * @author JonnyJiang
     * @date 2019/5/22 10:32
     */

    public static AbstractFileRepository getFileRepository(FileRepositoryService fileRepositoryService, SysAuditLogService sysAuditLogService, SecurityUtils securityUtils, FileRepositoryDO fileRepository) {
        switch (fileRepository.getRepositoryType()) {
            case 0:
                return new BlockRepository(fileRepositoryService, fileRepository.getId(), fileRepository.getStorageBaseDirectory());
            case 1:
                return new OssRepository(fileRepositoryService, fileRepository.getId(), fileRepository.getOssProvider(), null, null, null, null, null);
            case 2:
                return new FtpRepository(fileRepositoryService, fileRepository.getId(), fileRepository.getFtpServerAddress(), fileRepository.getFtpUserName(), fileRepository.getFtpPassword());
            default:
                throw new ServerException(LocaleUtils.getRepositoryTypeNotSupport(fileRepository.getRepositoryType()));
        }
    }

    /**
     * 分配存储库
     *
     * @param size 所需空间大小
     * @return java.lang.Boolean
     * @author JonnyJiang
     * @date 2019/5/22 19:20
     */

    public Boolean tryAllocationStorage(Long size) {
        if (checkPhysicalStorage(size)) {
            return fileRepositoryService.updateUsedSizeById(fileRepositoryId, size);
        }
        return false;
    }

    /**
     * 加密
     *
     * @param key         密钥
     * @param inputStream 输入流
     * @return byte[] 加密后的字节数组
     * @author JonnyJiang
     * @date 2019/5/22 19:20
     */

    protected byte[] encrypt(byte[] key, BufferedInputStream inputStream) {
        try {
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            logger.debug("加密前块大小：" + count);
            byte[] data = new byte[count];
            int actualCnt = inputStream.read(data);
            if (actualCnt != count) {
                throw new UploadExpcetion(LocaleUtils.getFileStreamMissing());
            }
            byte[] encryptedBytes;
            if (count % SM4Util.SM4_GROUP_BYTES == 0) {
                encryptedBytes = SM4Util.encryptEcbNoPadding(key, data);
            } else {
                encryptedBytes = SM4Util.encryptEcbPadding(key, data);
            }
            logger.debug("加密后块大小：" + encryptedBytes.length);
            return encryptedBytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UploadExpcetion(LocaleUtils.getFileEncryptError(e.getMessage()));
        }
    }

    /**
     * 解密
     *
     * @param key         密钥
     * @param chunkSize   块大小
     * @param inputStream 输入流
     * @return byte[]
     * @author JonnyJiang
     * @date 2019/5/22 19:20
     */

    private byte[] decrypt(byte[] key, Long chunkSize, Long encryptedSize, InputStream inputStream) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        logger.debug("开始读取文件块...");
        byte[] data = new byte[Math.toIntExact(encryptedSize)];
        Integer offset = 0;
        int bytesRead;
        byte[] buff = new byte[1024];
        while ((bytesRead = inputStream.read(buff, 0, buff.length)) != -1) {
            System.arraycopy(buff, 0, data, offset, bytesRead);
            offset += bytesRead;
            logger.debug("文件块已经读取：" + offset + "/" + encryptedSize);
        }
        logger.debug("读取文件块完成");
        byte[] originalBytes;
        if (chunkSize % SM4Util.SM4_GROUP_BYTES == 0) {
            originalBytes = SM4Util.decryptEcbNoPadding(key, data);
        } else {
            originalBytes = SM4Util.decryptEcbPadding(key, data);
        }
        logger.debug("解密后块大小：" + originalBytes.length);
        return originalBytes;
    }

    protected void onChunkLoaded(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new DownloadExpcetion(e.getMessage());
            }
        }
    }

    private void loadChunks(FileChunkInfoService fileChunkInfoService, FileInfoDO fileInfo, byte[] key, OutputStream outputStream) {
        InputStream inputStream = null;
        List<FileChunkInfoDO> chunkInfos = fileChunkInfoService.listByFileInfoId(fileInfo.getId());
        for (FileChunkInfoDO chunkInfo : chunkInfos) {
            try {
                logger.debug("chunk input stream getting: " + chunkInfo.getChunkPath());
                inputStream = getChunkInputStream(chunkInfo);
                logger.debug("chunk input stream got: " + chunkInfo.getChunkPath());
                if (IntegerUtils.isTrue(chunkInfo.getEncrypted())) {
                    outputStream.write(decrypt(key, chunkInfo.getChunkSize(), chunkInfo.getEncryptedSize(), inputStream));
                } else {
                    byte[] bytes = new byte[BUFFER_SIZE];
                    int length;
                    while ((length = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DownloadExpcetion(e.getMessage());
            } finally {
                onChunkLoaded(inputStream);
            }
        }
    }

    /**
     * 删除文件块
     *
     * @param chunkInfo 文件块信息
     * @author JonnyJiang
     * @date 2019/6/11 17:24
     */

    protected abstract void deleteChunk(FileChunkInfoDO chunkInfo);

    public void deleteChunks(FileChunkInfoService fileChunkInfoService, FileInfoDO fileInfo, List<FileChunkInfoDO> chunkInfos) {
        for (FileChunkInfoDO chunkInfo : chunkInfos) {
            deleteChunk(chunkInfo);
            fileChunkInfoService.removeById(chunkInfo.getId());
            // 释放存储空间
            fileRepositoryService.releaseSpace(fileRepositoryId, fileInfo.getEncryptedSize());
        }
    }

    public InputStream getFileStream(FileChunkInfoService fileChunkInfoService, FileInfoDO fileInfo) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            loadChunks(fileChunkInfoService, fileInfo, getKey(), outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("下载出错:" + e.getMessage());
            throw new DownloadExpcetion(LocaleUtils.getFileDownloadError(e.getMessage()));
        }
    }
}