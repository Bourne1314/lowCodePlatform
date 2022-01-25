package com.csicit.ace.fileserver.core.service;

import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.pojo.vo.ChunkVO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Transactional(rollbackFor = Exception.class)
public interface FileInfoService extends IBaseService<FileInfoDO> {

    /**
     * 专门给 单机版应用使用
     * 文件下载 直接获取输入流
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/4/15 18:03
     */
    InputStream getFileInputStream(Map<String, Object> params);

    FileInfoDO upload(FileConfigurationDO configuration, byte[] bytes, Integer chunk, String yfId, Long size, Integer chunks) throws Exception;

    FileInfoDO upload(FileConfigurationDO configuration, InputStream inputStream, Integer chunk, String yfId, Long size, Integer chunks) throws Exception;

    /**
     * 文件上传
     *
     * @param inputStream   上传的文件
     * @param chunkVO       切片信息
     * @param configuration 附件配置项
     * @return FileInfoDO 全部上传完成后返回文件信息
     * @author JonnyJiang
     * @date 2019/5/22 18:55
     */

    FileInfoDO upload(InputStream inputStream, ChunkVO chunkVO, FileConfigurationDO configuration);

    /**
     * 获取文件id对应的真实文件
     *
     * @param fileInfoId
     * @return FileInfoDO
     * @author JonnyJiang
     * @date 2019/5/22 18:58
     */

    FileInfoDO getActualFileInfo(String fileInfoId);

    /**
     * 获取文件对应的真实文件
     *
     * @param fileInfo
     * @return FileInfoDO
     * @author JonnyJiang
     * @date 2019/5/22 19:00
     */

    FileInfoDO getActualFileInfo(FileInfoDO fileInfo);

    /**
     * 下载文件
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param fileToken        文件token
     * @param isThumbnail      是否预览图
     * @param width            宽度
     * @param height           高度
     * @param outputStream     输出流
     * @author JonnyJiang
     * @date 2020/7/9 16:00
     */

    void download(String appId, String configurationKey, String fileToken, Boolean isThumbnail, Integer width, Integer height, OutputStream outputStream) throws IOException;

    /**
     * 单文件下载
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param fileToken        文件token
     * @param isThumbnail      是否预览图
     * @param width            宽度
     * @param height           高度
     * @author JonnyJiang
     * @date 2019/5/22 19:05
     */

    void download(String appId, String configurationKey, String fileToken, Boolean isThumbnail, Integer width, Integer height);

    /**
     * 打包下载
     *
     * @param appId            应用标识
     * @param configurationKey 配件配置标识
     * @param downloadToken    文件标识
     * @author JonnyJiang
     * @date 2019/5/24 16:03
     */

    void downloadZipped(String appId, String configurationKey, String downloadToken);

    /**
     * 分享文件
     *
     * @param appId               应用标识
     * @param configurationKey    附件配置标识
     * @param fileId              文件id
     * @param desConfigurationKey 目标附件配置标识
     * @param desFormId           目标表单id
     * @return void
     * @author JonnyJiang
     * @date 2019/5/24 16:02
     */

    FileInfoDO shareByFileId(String appId, String configurationKey, String fileId, String desConfigurationKey, String desFormId);

    /**
     * 删除文件
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param fileToken        文件token
     * @return void
     * @author JonnyJiang
     * @date 2019/5/24 16:02
     */

    void deleteByFileId(String appId, String configurationKey, String fileToken);

    /**
     * 删除表单文件
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @author JonnyJiang
     * @date 2019/6/4 16:24
     */

    void deleteByFormId(String appId, String configurationKey, String formId);

    /**
     * 根据表单id查询文件
     *
     * @param configurationId 附件配置id
     * @param formId          表单id
     * @return java.util.List<FileInfoDO>
     * @author JonnyJiang
     * @date 2019/5/22 18:56
     */

    List<FileInfoDO> listByFormId(String configurationId, String formId);

    /**
     * 根据表单id查询文件
     *
     * @param configuration 附件配置
     * @param formId        表单id
     * @return java.util.List<FileInfoDO>
     * @author JonnyJiang
     * @date 2019/5/22 18:56
     */

    List<FileInfoDO> listByFormId(FileConfigurationDO configuration, String formId);

    /**
     * 根据表单id查询文件，并以DownloadToken代替真实id
     *
     * @param configurationId 附件配置id
     * @param formId          表单id
     * @return java.util.List<FileInfoDO>
     * @author JonnyJiang
     * @date 2019/6/4 17:44
     */

    List<FileInfoDO> listByFormIdWithDownloadToken(String configurationId, String formId);

    /**
     * 根据文件id查询文件
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param fileToken        文件id/downloadToken
     * @return FileInfoDO
     * @author JonnyJiang
     * @date 2019/6/4 17:49
     */

    FileInfoDO getByFileToken(String appId, String configurationKey, String fileToken);

    /**
     * 分享表单文件
     *
     * @param appId               应用标识
     * @param configurationKey    附件配置标识
     * @param formId              表单id
     * @param desConfigurationKey 目标附件配置标识
     * @param desFormId           目标表单id
     * @return void
     * @author JonnyJiang
     * @date 2019/6/4 20:05
     */

    void shareByFormId(String appId, String configurationKey, String formId, String desConfigurationKey, String desFormId);

    /**
     * 首文件下载
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @author JonnyJiang
     * @date 2019/6/25 16:51
     */

    void downloadFirstFile(String appId, String configurationKey, String formId);

    /**
     * 删除表单所有文件
     *
     * @param formId 表单id
     * @author JonnyJiang
     * @date 2019/12/23 11:37
     */

    void deleteAllByFormId(String formId);

    /**
     * 获取真实文件
     *
     * @param configuration 文件配置
     * @param fileToken     文件token
     * @return com.csicit.ace.fileserver.core.pojo.FileInfoDO
     * @author JonnyJiang
     * @date 2020/3/30 16:49
     */

    FileInfoDO getActualFileInfo(FileConfigurationDO configuration, String fileToken);

    /**
     * 文件预览
     *
     * @param appId            应用标识
     * @param configurationKey 附件配置标识
     * @param fileToken        文件token
     * @author JonnyJiang
     * @date 2020/4/21 14:19
     */

    void preview(String appId, String configurationKey, String fileToken);

    /**
     * 获取文件数量
     *
     * @param configuration 附件配置
     * @param formId        表单ID
     * @return 文件数量
     * @author JonnyJiang
     * @date 2020/7/1 8:57
     */

    Integer getCountByFormId(FileConfigurationDO configuration, String formId);

    /**
     * 导出文件
     *
     * @param zipOutputStream 压缩包输出流
     * @param exportInfo      导出信息
     * @author JonnyJiang
     * @date 2020/7/9 15:42
     */

    void exportZip(ZipOutputStream zipOutputStream, ExportInfo exportInfo) throws IOException;

    /**
     * 导出压缩包
     *
     * @param exportInfo 导出信息
     * @author JonnyJiang
     * @date 2020/7/6 9:40
     */

    void exportZip(ExportInfo exportInfo);

    /**
     * 导出压缩包
     *
     * @param exportInfos 导出信息列表
     * @author JonnyJiang
     * @date 2020/7/7 17:57
     */

    void exportZipBatch(List<ExportInfo> exportInfos);

    /**
     * 移动到回收站
     *
     * @param id
     * @return void
     * @author JonnyJiang
     * @date 2020/7/8 14:13
     */

    void moveToRecycleBin(String id);

    /**
     * 删除文件
     *
     * @param fileConfiguration 附件配置
     * @param fileInfo          文件信息
     * @author JonnyJiang
     * @date 2020/7/8 14:15
     */

    void delete(FileConfigurationDO fileConfiguration, FileInfoDO fileInfo);

    /**
     * 获取文件id
     *
     * @param configuration 附件配置
     * @param fileToken     文件token
     * @return 文件id
     * @author JonnyJiang
     * @date 2020/7/8 15:13
     */

    String getActualFileId(FileConfigurationDO configuration, String fileToken);

    /**
     * 设置临时token
     * @param fileInfo 文件信息
     * @author JonnyJiang
     * @date 2021/6/26 21:39
     */

    void setDownloadToken(FileInfoDO fileInfo);

    /**
     * 物理删除文件
     * @param fileInfo	文件信息
     * @param fileRepository	文件存储库
     * @author JonnyJiang
     * @date 2021/6/26 21:49
     */

    void deleteFilePhysical(FileInfoDO fileInfo, FileRepositoryDO fileRepository);

}