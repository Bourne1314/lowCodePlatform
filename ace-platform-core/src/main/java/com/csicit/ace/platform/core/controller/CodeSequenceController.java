package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.CodeSequenceService;
import com.csicit.ace.data.persistent.service.CodeTemplatePartService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2020/5/22 15:25
 */
@RestController
@RequestMapping("/codeSequences")
public class CodeSequenceController extends BaseController {

    @Autowired
    CodeSequenceService codeSequenceService;
    @Autowired
    CodeTemplatePartService codeTemplatePartService;
    @Autowired
    HttpServletRequest request;

    /**
     * 根据id获取单个数字序列
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个数字序列", httpMethod = "GET", notes = "根据id获取单个数字序列" )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        CodeSequenceDO CodeSequenceDO = codeSequenceService.getById(id);
        return R.ok().put("instance", CodeSequenceDO);
    }


    /**
     * 根据条件获取数字序列列表并分页
     *
     * @param params 请求参数map对象
     * @return 数字序列列表
     * @author shanwj
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取数字序列列表并分页", httpMethod = "GET", notes = "获取数字序列列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = codeSequenceService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<CodeSequenceDO> page = new Page<>(current, size);
        IPage list = codeSequenceService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 新增数字序列保存
     *
     * @param instance 数字序列对象
     * @return 保存响应结果
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存数字序列")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeSequenceDO")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody CodeSequenceDO instance) {
        return codeSequenceService.saveCodeSequence(instance);
    }


    /**
     * 修改数字序列保存
     *
     * @param instance 对象
     * @return 修改数字序列
     * @author shanwj
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改数字序列", httpMethod = "PUT", notes = "修改数字序列")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "CodeSequenceDO")
    @AceAuth("修改数字序列")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody CodeSequenceDO instance) {
        return codeSequenceService.updateCodeSequence(instance);
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
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if(codeTemplatePartService.count(
                new QueryWrapper<CodeTemplatePartDO>().eq("SEQUENCE_BIZ_TAG",codeSequenceService.getById(ids[0]).getBizTag()))>0){
            return R.error("当前序列标识已被引用，无法删除!");
        }
        if (codeSequenceService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
