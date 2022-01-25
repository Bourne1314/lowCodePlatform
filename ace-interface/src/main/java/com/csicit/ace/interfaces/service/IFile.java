package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.file.ExceptConfiguration;
import com.csicit.ace.common.pojo.domain.file.ExceptForm;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.utils.server.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文件处理接口
 *
 * @author yansiyang
 * @version v1.0
 * @date 15:28 2019/3/28
 */
public interface IFile {

    /**
     * 删除指定文件
     *
     * @param configurationKey 附件配置标识
     * @param fileId           文件id
     * @author JonnyJiang
     * @date 2019/6/4 16:30
     */
    void deleteFileByFileId(String configurationKey, String fileId);

    /**
     * 删除表单文件
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @author JonnyJiang
     * @date 2019/6/4 16:30
     */
    void deleteFileByFormId(String configurationKey, String formId);

    /**
     * 删除表单文件
     *
     * @param formId 表单id
     * @author JonnyJiang
     * @date 2019/12/23 16:39
     */

    void deleteAllByFormId(String formId);

    /**
     * 加载指定表单关联的附件列表
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @return 返回附件列表
     * @author JonnyJiang
     * @date 2019/5/31 10:49
     */
    List<FileInfoDO> listFileByFormId(String configurationKey, String formId);

    /**
     * 获取指定文件
     *
     * @param configurationKey 附件配置标识
     * @param fileId           文件id
     * @return 文件信息
     * @author JonnyJiang
     * @date 2019/6/4 18:13
     */
    FileInfoDO getFile(String configurationKey, String fileId);

    /**
     * 与指定记录共享文件
     *
     * @param configurationKey    附件配置标识
     * @param fileId              文件id
     * @param desConfigurationKey 目标附件配置标识
     * @param desFormId           目标表单id
     * @return 共享后的文件信息
     * @author JonnyJiang
     * @date 2019/5/31 11:35
     */
    FileInfoDO shareFile(String configurationKey, String fileId, String desConfigurationKey, String desFormId);

    /**
     * 共享文件
     *
     * @param configurationKey    附件配置标识
     * @param formId              表单id
     * @param desConfigurationKey 目标附件配置标识
     * @param desFormId           目标表单id
     * @author JonnyJiang
     * @date 2019/11/22 9:25
     */

    void shareFileByFormId(String configurationKey, String formId, String desConfigurationKey, String desFormId);

    /**
     * 下载文件
     *
     * @param configurationKey 附件配置标识
     * @param fileToken        文件token
     * @return 文件流
     * @author JonnyJiang
     * @date 2020/6/22 9:17
     */

    InputStream download(String configurationKey, String fileToken) throws IOException;


    /**
     *
     * @param appId
     * @param configurationKey
     * @param downloadToken
     * @return
     * @throws IOException
     */
    void downloadZipped(String appId, String configurationKey, String downloadToken) throws IOException;

    /**
     * 上传文件
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @param fileName         文件名
     * @param fileSize         文件大小
     * @param inputStream      输入流
     * @return 文件信息
     * @author JonnyJiang
     * @date 2020/6/22 9:19
     */

    FileInfoDO upload(String configurationKey, String formId, String fileName, Long fileSize, InputStream inputStream) throws Exception;

    /**
     * 上传文件
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @param fileName         文件名
     * @param fileSize         文件大小
     * @param inputStream      输入流
     * @param secretLevel      密级
     * @return 文件信息
     * @author JonnyJiang
     * @date 2020/6/22 9:19
     */

    FileInfoDO upload(String configurationKey, String formId, String fileName, Long fileSize, InputStream inputStream, Integer secretLevel) throws Exception;

    /**
     * 导出文件压缩包
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单id
     * @return 压缩包文件流
     * @author JonnyJiang
     * @date 2020/7/2 18:06
     */

    InputStream exportZip(String configurationKey, String formId) throws IOException;


    /**
     * 导出文件压缩包
     *
     * @param exportInfo 导出文件信息列表
     * @return 压缩包文件流
     * @author JonnyJiang
     * @date 2020/7/7 18:01
     */

    InputStream exportZipBatch(ExportInfo exportInfo) throws IOException;

    /**
     * 导出文件压缩包
     *
     * @param exportInfos 导出文件信息列表
     * @return 压缩包文件流
     * @author JonnyJiang
     * @date 2020/7/7 18:01
     */

    InputStream exportZipBatch(List<ExportInfo> exportInfos) throws IOException;

    /**
     * 导入文件压缩包
     *
     * @param inputStream 压缩包文件流
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZip(InputStream inputStream) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream 压缩包文件流
     * @param appId       应用标识
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptApp(InputStream inputStream, String appId) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream 压缩包文件流
     * @param appIds      应用标识列表
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptApp(InputStream inputStream, List<String> appIds) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream         压缩包文件流
     * @param exceptConfiguration 排除附件配置
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptConfiguration(InputStream inputStream, ExceptConfiguration exceptConfiguration) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream          压缩包文件流
     * @param exceptConfigurations 排除附件配置列表
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptConfiguration(InputStream inputStream, List<ExceptConfiguration> exceptConfigurations) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream 压缩包文件流
     * @param exceptForm  排除表单信息
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptForm(InputStream inputStream, ExceptForm exceptForm) throws Exception;

    /**
     * 导入文件压缩包
     *
     * @param inputStream 压缩包文件流
     * @param exceptForms 排除表单信息列表
     * @author JonnyJiang
     * @date 2020/7/3 8:11
     */

    void importZipExceptForm(InputStream inputStream, List<ExceptForm> exceptForms) throws Exception;

    /**
     * 更新文件
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单ID
     * @param fileId           文件ID
     * @param fileName         文件名
     * @param fileSize         文件大小
     * @param inputStream      文件流
     * @return 文件信息
     * @author JonnyJiang
     * @date 2020/7/21 17:26
     */

    FileInfoDO update(String configurationKey, String formId, String fileId, String fileName, Long fileSize, InputStream inputStream) throws Exception;

    /**
     * 更新文件
     *
     * @param configurationKey 附件配置标识
     * @param formId           表单ID
     * @param fileId           文件ID
     * @param fileName         文件名
     * @param fileSize         文件大小
     * @param inputStream      文件流
     * @param secretLevel      密级
     * @return 文件信息
     * @author JonnyJiang
     * @date 2020/7/21 17:26
     */

    FileInfoDO update(String configurationKey, String formId, String fileId, String fileName, Long fileSize, InputStream inputStream, Integer secretLevel) throws Exception;

    /***
     * @description:新增发起流程记录
     * @params: 表单FormId
     * @return: java.lang.Boolean
     * @author: Zhangzhaojun
     * @time: 2021/12/3 9:21
     */
    R addReview(String formId);

    /***
     * @description:设置附件审查
     * @params: formId
     * @return: R
     * @author: Zhangzhaojun
     * @time: 2021/12/3 15:21
     */
    R setReview(String formId);
}