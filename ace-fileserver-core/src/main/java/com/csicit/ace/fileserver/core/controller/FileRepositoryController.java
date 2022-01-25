package com.csicit.ace.fileserver.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import com.csicit.ace.common.pojo.vo.FileVO;
import com.csicit.ace.fileserver.core.service.FileConfigurationService;
import com.csicit.ace.fileserver.core.service.FileInfoService;
import com.csicit.ace.fileserver.core.service.FileRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 附件管理-文件存储库
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@RestController
@Api("附件管理-文件存储库")
@RequestMapping("/fileRepository")
public class FileRepositoryController {
    @Autowired
    FileRepositoryService fileRepositoryService;
    @Autowired
    FileConfigurationService fileConfigurationService;
    @Autowired
    FileInfoService fileInfoService;

    /**
     * 分配存储空间
     *
     * @param file {fileName,formId,secretLevel,md5,contentType,fileSize,chunks,yfId,configurationKey}
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2019/5/22 18:13
     */

    @CrossOrigin
    @ApiOperation(value = "分配存储空间")
    @RequestMapping(value = "/allocateSpace", method = RequestMethod.POST)
    public R allocateSpace(@RequestBody FileVO file) {
        try {
            FileConfigurationDO fileConfiguration = fileConfigurationService.loadByKey(file.getConfigurationKey(), file.getAppName());
            FileInfoDO fileInfo = fileRepositoryService.allocateSpace(fileConfiguration, file);
            return R.ok().put("yfId", file.getYfId()).put("id", fileInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/feginAllocateSpace", method = RequestMethod.POST)
    public FileInfoDO feginAllocateSpace(@RequestBody FileVO file) {
        FileConfigurationDO fileConfiguration = fileConfigurationService.loadByKey(file.getConfigurationKey(), file.getAppName());
        return fileRepositoryService.allocateSpace(fileConfiguration, file);
    }

    /**
     * 获取文件存储库列表
     *
     * @param params 请求参数map对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "获取文件存储库列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取文件存储库列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<FileRepositoryDO> list = fileRepositoryService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<FileRepositoryDO> page = new Page<>(current, size);
        IPage list = fileRepositoryService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 根据文件存储库id获取单个文件存储库数据
     *
     * @param id 对象主键
     * @return 单个应用对象
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个文件存储库", httpMethod = "GET", notes = "根据文件存储库id获取单个文件存储库数据")
    @AceAuth("获取单个文件存储库")
    @PostMapping
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        FileRepositoryDO instance = fileRepositoryService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 保存文件存储库
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "保存文件存储库", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "FileRepositoryDO")
    @AceAuth("保存文件存储库")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody FileRepositoryDO instance) {
        return fileRepositoryService.insert(instance);
    }

    /**
     * 修改文件存储库
     *
     * @param instance 对象
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "修改附件配置项", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "FileRepositoryDO")
    @AceAuth("修改文件存储库")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody FileRepositoryDO instance) {
        return fileRepositoryService.update(instance);
    }

    /**
     * 删除附件配置项
     *
     * @param ids ID数组
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "删除附件配置项", httpMethod = "DELETE")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除文件存储库")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        return fileRepositoryService.delete(ids);
    }
}