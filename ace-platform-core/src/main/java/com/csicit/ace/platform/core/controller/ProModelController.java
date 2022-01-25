package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.ProModelServiceD;
import com.csicit.ace.data.persistent.service.ProServiceServiceD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 实体模型 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:31:19
 */

@RestController
@RequestMapping("/proModelDs")
@Api("实体模型")
public class ProModelController extends BaseController {

    @Autowired
    private ProModelServiceD proModelService;
    @Autowired
    private ProServiceServiceD proServiceService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:31:19
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ProModelDO instance = proModelService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 不分页获取列表
     *
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "不分页获取列表", httpMethod = "GET", notes = "不分页获取列表")
    @ApiImplicitParam(name = "appId", value = "参数", required = true, dataType = "String")
    @RequestMapping(value = "/query/list/{appId}", method = RequestMethod.GET)
    public R listAll(@PathVariable("appId") String appId) {
        ProServiceDO proServiceDO = proServiceService.getOne(new QueryWrapper<ProServiceDO>().eq("app_id", appId));
        if (proServiceDO == null) {
            return R.ok().put("list", new ArrayList<>(16));
        }
        List<ProModelDO> list = proModelService.list(new QueryWrapper<ProModelDO>()
                .eq("service_id", proServiceDO.getId()).orderByDesc("create_time"));
        return R.ok().put("list", list);
    }

}
