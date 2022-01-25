package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BdAppLibDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.BdAppLibService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 基础应用产品库 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-05-14 08:00:27
 */

@RestController
@RequestMapping("/bdAppLibs")
@Api("基础应用产品库")
public class BdAppLibController extends BaseController {

    @Autowired
    private BdAppLibService bdAppLibService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个基础应用产品库", httpMethod = "GET", notes = "获取单个基础应用产品库")
    @AceAuth("获取单个基础应用产品库")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BdAppLibDO instance = bdAppLibService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取基础应用产品库列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "获取基础应用产品库列表", httpMethod = "GET", notes = "获取基础应用产品库列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取基础应用产品库列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<BdAppLibDO> list = bdAppLibService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<BdAppLibDO> page = new Page<>(current, size);
        IPage list = bdAppLibService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }


    /**
     * 新增基础应用产品库
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "保存基础应用产品库", httpMethod = "POST", notes = "保存基础应用产品库")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdAppLibDO")
    @AceAuth("保存基础应用产品库")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BdAppLibDO instance) {
        if (bdAppLibService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改基础应用产品库
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "修改基础应用产品库", httpMethod = "PUT", notes = "修改基础应用产品库")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BdAppLibDO")
    @AceAuth("修改基础应用产品库")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BdAppLibDO instance) {
        if (bdAppLibService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除基础应用产品库
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-05-14 08:00:27
     */
    @ApiOperation(value = "删除基础应用产品库", httpMethod = "DELETE", notes = "删除基础应用产品库")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除基础应用产品库")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (bdAppLibService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));


    }
}
