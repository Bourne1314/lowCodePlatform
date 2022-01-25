package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMsgTemplateService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author shanwj
 * @date 2020/4/7 11:16
 */
@RestController
@RequestMapping("/sysMsgTemplates")
public class SysMsgTemplateController extends BaseController{

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    /**
     * 根据id获取单个消息模板
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个消息模板", httpMethod = "GET", notes = "根据id获取单个消息模板" )
    @AceAuth("获取单个消息模板")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysMsgTemplateDO sysMsgTemplateDO = sysMsgTemplateService.getById(id);
        if(StringUtils.isNotEmpty(sysMsgTemplateDO.getAuth())){
            sysMsgTemplateDO.setAuthName(sysAuthService.getById(sysMsgTemplateDO.getAuth()).getName());
        }
        return R.ok().put("instance", sysMsgTemplateDO);
    }


    /**
     * 根据条件获取消息模板列表并分页
     *
     * @param params 请求参数map对象
     * @return 消息模板列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取消息模板列表并分页", httpMethod = "GET", notes = "获取消息模板列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取消息模板列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = sysMsgTemplateService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<SysMsgTemplateDO> page = new Page<>(current, size);
        IPage list = sysMsgTemplateService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增消息模板保存
     *
     * @param instance 消息模板对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存消息模板")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgTemplateDO")
    @AceAuth("保存消息模板对象")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysMsgTemplateDO instance) {
        // 判断消息模板名称是否已存在
        int count =
                sysMsgTemplateService.count(
                        new QueryWrapper<SysMsgTemplateDO>()
                                .eq("template_id", instance.getTemplateId()));
        if (count > 0) {
            return R.error("当前模板标识已存在");
        }
        if (sysMsgTemplateService.saveMsgTemplate(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


    /**
     * 修改消息模板保存
     *
     * @param instance 对象
     * @return 修改消息模板
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改消息模板", httpMethod = "PUT", notes = "修改消息模板")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgTemplateDO")
    @AceAuth("修改消息模板")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysMsgTemplateDO instance) {
        SysMsgTemplateDO oldTemplate = sysMsgTemplateService.getById(instance.getId());
        if(!Objects.equals(oldTemplate.getTemplateId(),instance.getTemplateId())){
            int count =
                    sysMsgTemplateService.count(
                            new QueryWrapper<SysMsgTemplateDO>()
                                    .eq("template_id", instance.getTemplateId()));
            if (count > 0) {
                return R.error("当前模板标识已存在");
            }
        }
        if (sysMsgTemplateService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除字典类型")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除消息模板对象")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysMsgTemplateService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 获取平台所有小程序消息模板列表
     * @return 小程序模板列表
     * @author shanwj
     * @date 2020/4/9 15:41
     */
    @ApiOperation(value = "获取平台所有小程序消息模板列表", httpMethod = "GET")
    @AceAuth("获取平台所有小程序消息模板列表")
    @RequestMapping(value = "/query/microAppTemplates",method = RequestMethod.GET)
    public R getMicroAppTemplateList(){
        return R.ok().put("list", sysMsgTemplateService.getMicroAppTemplateList());
    }

    /**
     * 导入选中小程序模板
     * @return 小程序模板列表
     * @author shanwj
     * @date 2020/4/9 15:41
     */
    @ApiOperation(value = "导入选中小程序模板", httpMethod = "POST")
    @ApiImplicitParam(name = "map", dataType = "Map")
    @AceAuth("导入选中小程序模板")
    @RequestMapping(value = "/action/importSelectedTemplates",method = RequestMethod.POST)
    public R saveImportMicroAppTemplates(@RequestBody Map<String, Object> map){
        return sysMsgTemplateService.importSelectedTemplates(map);
    }

    /**
     * 导入选中小程序模板
     * @return 小程序模板列表
     * @author shanwj
     * @date 2020/4/9 15:41
     */
    @ApiOperation(value = "导入选中小程序模板", httpMethod = "POST")
    @AceAuth("导入选中小程序模板")
    @RequestMapping(value = "/action/importAllTemplates",method = RequestMethod.POST)
    public R saveImportAllTemplates(@RequestBody Map<String, Object> map){
        return sysMsgTemplateService.importAllTemplates(map);
    }

}
