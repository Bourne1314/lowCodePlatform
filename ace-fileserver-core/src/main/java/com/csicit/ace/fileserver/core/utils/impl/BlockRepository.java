package com.csicit.ace.fileserver.core.utils.impl;

import com.csicit.ace.common.pojo.domain.FileChunkInfoDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.fileserver.core.DownloadExpcetion;
import com.csicit.ace.fileserver.core.ServerException;
import com.csicit.ace.fileserver.core.UploadExpcetion;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import com.csicit.ace.fileserver.core.utils.AbstractFileRepository;
import com.csicit.ace.fileserver.core.utils.LocaleUtils;

import java.io.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * 文件存储库
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
public class BlockRepository extends AbstractFileRepository {
    private String storageBaseDirectory;

    public BlockRepository(FileRepositoryService fileRepositoryService, String fileRepositoryId, String storageBaseDirectory) {
        super(fileRepositoryService, fileRepositoryId);
        this.storageBaseDirectory = storageBaseDirectory;
    }

    @Override
    public String transformFilePath(String filePath) {
        return filePath.replace("\\", File.separator);
    }

    /**
     * 如果存在前缀 则创建目录都带上前缀
     *
     * @author FourLeaves
     * @date 2020/12/1 10:16
     */
    private File makeFile(String path) {
//        String pathPrefix = System.getProperty("ace.config.file.pathPrefix");
//        if (StringUtils.isNotBlank(path)) {
//            if (path.startsWith(File.separator)) {
//                path = pathPrefix + path;
//            } else {
//                path = pathPrefix + File.separator + path;
//            }
//        }
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new UploadExpcetion(LocaleUtils.getDirectoryCreateError(file.getPath()));
            }
        }
        return file;
    }

    @Override
    public Boolean checkPhysicalStorage(Long size) {
        File file = makeFile(storageBaseDirectory);
        return file.getFreeSpace() >= size;
    }

    @Override
    public Long encryptSave(byte[] key, String chunkPath, InputStream inputStream) {
        Long encryptedSize = 0L;
        RandomAccessFile raFile = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            File dirFile = new File(combinePath(chunkPath));
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] encryptedBytes = encrypt(key, bufferedInputStream);
            logger.debug("加密后块大小：" + encryptedBytes.length);
            raFile.write(encryptedBytes);
            encryptedSize += encryptedBytes.length;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        } finally {
            onSaveComplete(bufferedInputStream, raFile);
        }
        return encryptedSize;
    }

    @Override
    public void normalSave(String chunkPath, InputStream fileStream) {
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        try {
            File dirFile = new File(combinePath(chunkPath));
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(fileStream);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                raFile.write(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传出错:" + e.getMessage());
            throw new UploadExpcetion(e.getMessage());
        } finally {
            onSaveComplete(inputStream, raFile);
        }
    }

    private String combinePath(String path) {
        if (StringUtils.isNotEmpty(storageBaseDirectory)) {
            if (storageBaseDirectory.endsWith(File.separator)) {
                path = storageBaseDirectory + path;
            } else {
                path = storageBaseDirectory + File.separator + path;
            }
        }
        return path;
    }

    @Override
    protected InputStream getChunkInputStream(FileChunkInfoDO chunkInfo) {
        String path = combinePath(chunkInfo.getChunkPath());
        File file = new File(path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new DownloadExpcetion(e.getMessage());
            }
        }
        throw new DownloadExpcetion("未找到切片“" + path + "”！");
    }

    @Override
    protected void createDirectory(String path) {
        File file = new File(combinePath(path));
        makeFile(file.getParent());
    }

    @Override
    protected void deleteChunk(FileChunkInfoDO chunkInfo) {
        File file = new File(combinePath(chunkInfo.getChunkPath()));
        if (file.exists()) {
            if (!file.delete()) {
                throw new ServerException(LocaleUtils.getFileChunkDeleteError(file.getPath()));
            }
        }
    }
}