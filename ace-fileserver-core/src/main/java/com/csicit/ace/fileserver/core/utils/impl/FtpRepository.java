package com.csicit.ace.fileserver.core.utils.impl;

import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.fileserver.core.DownloadExpcetion;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public class FtpRepository extends AbstractFileRepository {
    private String serverAddress;
    private String ftpUserName;
    private String ftpPassword;
    private FTPClient ftpClient = null;

    public FtpRepository(FileRepositoryService fileRepositoryService, String fileRepositoryId, String serverAddress, String ftpUserName, String ftpPassword) {
        super(fileRepositoryService, fileRepositoryId);
        this.serverAddress = serverAddress;
        this.ftpUserName = ftpUserName;
        this.ftpPassword = ftpPassword;
        init();
    }

    /**
     * FTP FTP服务器连接初始化
     *
     * @author JonnyJiang
     * @date 2019/6/4 15:23
     */

    private void init() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            logger.debug("正在连接FTP服务器：" + serverAddress);
            // 连接ftp服务器
            ftpClient.connect(serverAddress);
            // 登录ftp服务器
            ftpClient.login(ftpUserName, ftpPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 是否成功登录服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.debug("连接FTP服务器失败:" + serverAddress);
            }
            logger.debug("连接FTP服务器成功:" + serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public String transformFilePath(String filePath) {
        logger.debug("FileSeparator:" + File.separator);
        return filePath.replace("\\", File.separator);
    }

    @Override
    public Boolean checkPhysicalStorage(Long size) {
        return true;
    }

    @Override
    protected Long encryptSave(byte[] key, String chunkPath, InputStream inputStream) {
        Long encryptedSize = 0L;
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            String chunkName = getChunkName(chunkPath);
            bufferedInputStream = new BufferedInputStream(inputStream);
            outputStream = ftpClient.storeFileStream(chunkName);
            byte[] encryptedBytes = encrypt(key, bufferedInputStream);
            logger.debug("加密后块大小：" + encryptedBytes.length);
            outputStream.write(encryptedBytes);
            encryptedSize += encryptedBytes.length;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(LocaleUtils.getFileUploadError(e.getMessage()));
        } finally {
            onSaveComplete(bufferedInputStream, outputStream);
        }
        return encryptedSize;
    }

    private String getChunkName(String chunkPath) {
        String[] arr = chunkPath.split("\\".equals(File.separator) ? "\\\\" : File.separator);
        return arr[arr.length - 1];
    }

    @Override
    protected void normalSave(String chunkPath, InputStream fileStream) {
        OutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        try {
            String chunkName = getChunkName(chunkPath);
            inputStream = new BufferedInputStream(fileStream);
            outputStream = ftpClient.storeFileStream(chunkName);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        } finally {
            onSaveComplete(inputStream, outputStream);
        }
    }

    @Override
    protected InputStream getChunkInputStream(FileChunkInfoDO chunkInfo) {
        try {
            ftpClient.changeWorkingDirectory("");
            return ftpClient.retrieveFileStream(chunkInfo.getChunkPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    @Override
    protected void onChunkLoaded(InputStream inputStream) {
        super.onChunkLoaded(inputStream);
        try {
            ftpClient.completePendingCommand();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadExpcetion(e.getMessage());
        }
    }

    @Override
    protected void createDirectory(String path) {
        try {
            ftpClient.changeWorkingDirectory("");
            String[] dirs = path.split("\\".equals(File.separator) ? "\\\\" : File.separator);
            for (int i = 0; i < dirs.length - 1; i++) {
                String dir = dirs[i];
                ftpClient.makeDirectory(dir);
                ftpClient.changeWorkingDirectory(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadExpcetion(e.getMessage());
        }
    }

    @Override
    protected void deleteChunk(FileChunkInfoDO chunkInfo) {
        try {
            ftpClient.deleteFile(chunkInfo.getChunkPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (ftpClient != null) {
            ftpClient.logout();
        }
        super.finalize();
    }
}
