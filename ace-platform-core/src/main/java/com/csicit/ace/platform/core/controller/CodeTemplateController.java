package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.CodeTemplateDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.CodeTemplateService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * 
 * @author shanwj
 * @date 2020/5/22 15:27
 */
@RestController
@RequestMapping("/codeTemplates")
public class CodeTemplateController extends BaseController {

    @Autowired
    CodeTemplateService codeTemplateService;

    /**
     * 根据id获取单个编码模板
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个编码模板", httpMethod = "GET", notes = "根据id获取单个编码模板" )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        CodeTemplateDO CodeTemplateDO = codeTemplateService.getById(id);
        return R.ok().put("instance", CodeTemplateDO);
    }


    /**
     * 根据条件获取编码模板列表并分页
     *
     * @param params 请求参数map对象
     * @return 编码模板列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取编码模板列表并分页", httpMethod = "GET", notes = "获取编码模板列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = codeTemplateService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<CodeTemplateDO> page = new Page<>(current, size);
        IPage list = codeTemplateService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增编码模板保存
     *
     * @param instance 编码模板对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存编码模板")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeTemplateDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody CodeTemplateDO instance) {
        return codeTemplateService.saveTemplate(instance);
    }


    /**
     * 修改编码模板保存
     *
     * @param instance 对象
     * @return 修改编码模板
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改编码模板", httpMethod = "PUT", notes = "修改编码模板")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeTemplateDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody CodeTemplateDO instance) {
        CodeTemplateDO oldTemplate = codeTemplateService.getById(instance.getId());
        if(!Objects.equals(oldTemplate.getTemplateKey(),instance.getTemplateKey())){
            int count =
                    codeTemplateService.count(
                            new QueryWrapper<CodeTemplateDO>().eq("app_id",instance.getAppId())
                                    .eq("template_key", instance.getTemplateKey()));
            if (count > 0) {
                return R.error("当前模板标识已存在");
            }
        }
        if (codeTemplateService.updateById(instance)) {
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
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除编码模板")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (codeTemplateService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    
}
