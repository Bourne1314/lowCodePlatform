package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.google.common.collect.ImmutableMap;
import com.csicit.ace.data.persistent.service.CodeTemplatePartService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2020/5/22 15:27
 */
@RestController
@RequestMapping("/codeTemplateParts")
public class CodeTemplatePartController extends BaseController {
    @Autowired
    CodeTemplatePartService codeTemplatePartService;

    private static Map<Integer,String> codeTypes = new ImmutableMap.Builder<Integer,String>()
            .put(0,"静态文本")
            .put(1,"EL表达式")
            .put(2,"参数值")
            .build();

    /**
     * 根据id获取单个模板部件
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个模板部件", httpMethod = "GET", notes = "根据id获取单个模板部件" )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        CodeTemplatePartDO codeTemplatePart = codeTemplatePartService.getById(id);
        return R.ok().put("instance", codeTemplatePart);
    }


    /**
     * 根据条件获取模板部件列表并分页
     *
     * @param params 请求参数map对象
     * @return 模板部件列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取模板部件列表并分页", httpMethod = "GET", notes = "获取模板部件列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        MapWrapper equalInstance = MapWrapper.getEqualInstance(params);
        equalInstance = (MapWrapper)equalInstance.orderByAsc("sort_index");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = codeTemplatePartService.list(equalInstance);
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<CodeTemplatePartDO> page = new Page<>(current, size);
        IPage list = codeTemplatePartService.page(page, equalInstance);
        return R.ok().put("page", list);
    }

    /**
     * 新增模板部件保存
     *
     * @param instance 模板部件对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存模板部件")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeTemplatePartDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody CodeTemplatePartDO instance) {
        return codeTemplatePartService.saveTemplatePart(instance);
    }


    /**
     * 修改模板部件保存
     *
     * @param instance 对象
     * @return 修改模板部件
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改模板部件", httpMethod = "PUT", notes = "修改模板部件")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeTemplatePartDO")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody CodeTemplatePartDO instance) {
        return codeTemplatePartService.updateTemplatePart(instance);
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除模板部件")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (codeTemplatePartService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 获取当前模板下模板非数字序列部件
     * @param tid 模板id
     * @return java.util.List<com.csicit.ace.common.pojo.vo.KeyValueVO>
     * @author shanwj
     * @date 2020/7/13 9:42
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取当前模板下模板非数字序列部件", httpMethod = "GET", notes = "获取当前模板下模板非数字序列部件" )
    @RequestMapping(value = "/parts/{tid}", method = RequestMethod.GET)
    public List<KeyValueVO> getParts(@PathVariable("tid") String tid) {
        List<CodeTemplatePartDO> parts =
                codeTemplatePartService.list(new QueryWrapper<CodeTemplatePartDO>().select("id", "sort_index", "code_type")
                .eq("template_id", tid).in("code_type", 0, 1, 2).orderByAsc("sort_index"));
        List<KeyValueVO> keyValues = new ArrayList<>(16);
        for (CodeTemplatePartDO part:parts){
            KeyValueVO keyValue = new KeyValueVO();
            keyValue.setKey(part.getId());
            keyValue.setValue(codeTypes.get(part.getCodeType())+"_"+part.getSortIndex());
            keyValues.add(keyValue);
        }
        return keyValues;
    }

}
