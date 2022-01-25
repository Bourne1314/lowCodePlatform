package com.csicit.ace.zuul.service;

import com.csicit.ace.baseimpl.BaseFileImpl;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.pojo.domain.file.ExportInfo;
import com.csicit.ace.common.pojo.domain.file.FileConfiguration;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IFile;
import com.csicit.ace.zuul.feign.ClientService;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2019/5/31 11:38
 */
@Service("file")
public class FileImpl extends BaseFileImpl implements IFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileImpl.class);
    @Autowired
    ClientService clientService;

    @Override
    protected void doDeleteByFileId(String configurationKey, String fileId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("fileId", fileId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        clientService.deleteByFileId(map);
    }

    @Override
    protected void downloadZip(String appId, String configurationKey, String downloadToken) throws IOException {
        clientService.downloadZipped(appId,configurationKey,downloadToken);
    }

    @Override
    protected void doDeleteByFormId(String configurationKey, String formId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("formId", formId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        clientService.deleteByFormId(map);
    }

    @Override
    protected void doDeleteAllByFormId(String formId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("formId", formId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        clientService.deleteAllByFormId(map);
    }

    @Override
    protected FileInfoDO doShareByFileId(String configurationKey, String fileId, String desConfigurationKey, String desFormId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("fileId", fileId);
        map.put("desConfigurationKey", desConfigurationKey);
        map.put("desFormId", desFormId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        return clientService.shareByFileId(map);
    }

    @Override
    protected void doShareByFormId(String configurationKey, String formId, String desConfigurationKey, String desFormId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("formId", formId);
        map.put("desConfigurationKey", desConfigurationKey);
        map.put("desFormId", desFormId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        clientService.shareByFormId(map);
    }

    @Override
    protected List<FileInfoDO> doListByFormId(String configurationKey, String formId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("formId", formId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        return clientService.listByFormId(map);
    }

    @Override
    protected FileInfoDO doGetByFileId(String configurationKey, String fileId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("fileId", fileId);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        return clientService.getByFileId(map);
    }

    @Override
    protected InputStream doDownload(String configurationKey, String fileToken) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("configurationKey", configurationKey);
        map.put("fileToken", fileToken);
        map.put(APP_ID, appName);
        LOGGER.debug("params: " + map);
        return convertToInputStream(clientService.download(map));
    }

    @Override
    protected FileConfiguration doLoadFileConfigurationByKey(String configurationKey) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("configurationKey", configurationKey);
        params.put(APP_ID, appName);
        LOGGER.debug("params: " + params);
        return clientService.loadFileConfigurationByKey(params);
    }

    @Override
    protected FileInfoDO doAllocateSpace(FileVO file) {
        LOGGER.debug("file: " + file);
        return clientService.allocateSpace(file);
    }

    @Override
    protected void doUpload(String configurationKey, String yfId, Integer chunkIndex, Integer chunks, byte[] bytes, Long size) throws Exception {
        Map<String, Object> uploadParams = new HashMap<>(16);
        uploadParams.put("configurationKey", configurationKey);
        uploadParams.put("yfId", yfId);
        uploadParams.put(APP_ID, appName);
        uploadParams.put("chunk", chunkIndex);
        uploadParams.put("size", size);
        uploadParams.put("chunks", chunks);
        uploadParams.put("bytes", Base64.getEncoder().encodeToString(bytes));
        LOGGER.debug("params: " + uploadParams);
        clientService.upload(uploadParams);
    }

    @Override
    protected InputStream doExportZip(String configurationKey, String formId, String appId) {
        return convertToInputStream(clientService.exportZip(configurationKey, formId, appId));
    }

    @Override
    protected InputStream doExportZipBatch(List<ExportInfo> exportInfos) {
        return convertToInputStream(clientService.exportZipBatch(exportInfos));
    }

    private InputStream convertToInputStream(Response response) {
        LOGGER.debug("response status: " + String.valueOf(response.status()));
        switch (response.status()) {
            case HttpServletResponse.SC_OK:
                Response.Body body = response.body();
                try {
                    return body.asInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                throw new RuntimeException(String.valueOf(response.body()));
        }
        throw new RuntimeException(response.reason());
    }


    protected R addReviewFile(String formId) {
        return clientService.addReviewFile(formId);
    }
    protected R setReviewFile(String formId) {
        return clientService.setReviewFile(formId);
    }
}