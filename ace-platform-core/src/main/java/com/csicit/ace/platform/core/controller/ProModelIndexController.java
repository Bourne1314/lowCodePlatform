package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.ProModelIndexDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.ProModelIndexServiceD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 索引信息 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/proModelIndexDs")
@Api("索引信息")
public class ProModelIndexController extends BaseController {

    @Autowired
    private ProModelIndexServiceD proModelIndexService;

    /**
     * @param id
     * @return
     * @author shanwj
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ProModelIndexDO instance = proModelIndexService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param {modelId} 请求参数map对象
     * @return xxx集合
     * @author shanwj
     * @date 2019-11-04 14:49:22
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "modelId", value = "参数", required = true, dataType = "String")
    @RequestMapping(value = "/query/list/{modelId}", method = RequestMethod.GET)
    public R queryList(@PathVariable("modelId") String modelId) {
        List<ProModelIndexDO> list = proModelIndexService.list(new QueryWrapper<ProModelIndexDO>()
                .eq("model_id", modelId).orderByAsc("create_time"));
        return R.ok().put("list", list);
    }

}
