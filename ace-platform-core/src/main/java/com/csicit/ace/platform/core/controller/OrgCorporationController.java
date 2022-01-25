package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.OrgCorporationDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 组织-公司组织 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-16 15:31:10
 */

@RestController
@RequestMapping("/orgCorporations")
@Api("组织-公司组织管理")
public class OrgCorporationController extends BaseController {

    /**
     * 获取单个公司
     *
     * @param id
     * @return
     * @author generator
     * @date 2019-04-16 15:31:10
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个公司", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个公司")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        OrgCorporationDO instance = orgCorporationService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取公司列表并分页
     *
     * @param params 请求参数map对象
     * @return
     * @author generator
     * @date 2019-04-16 15:31:10
     */
    @ApiOperation(value = "获取公司列表并分页", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取公司列表并分页")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<OrgCorporationDO> page = new Page<>(current, size);
        IPage list = orgCorporationService.page(page, null);
        return R.ok().put("page", list);
    }

    /**
     * 保存公司
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-16 15:31:10
     */
    @ApiOperation(value = "保存公司", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgCorporationDO")
    @AceAuth("保存公司")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody OrgCorporationDO instance) {
        if (orgCorporationService.saveCorp(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改公司
     *
     * @param instance 对象
     * @return
     * @author generator
     * @date 2019-04-16 15:31:10
     */
    @ApiOperation(value = "修改公司", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgCorporationDO")
    @AceAuth("修改公司")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody OrgCorporationDO instance) {
        if (orgCorporationService.updateCorp(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除公司
     *
     * @param map
     * @return com.csicit.ace.common.utils.server.R
     * @author generator
     * @date 2019-04-16 15:31:10
     */
    @ApiOperation(value = "删除公司", httpMethod = "DELETE")
    @ApiImplicitParam(name = "map", value = "map", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除公司")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> map) {
        return orgCorporationService.deleteCorp(map);
    }

}
