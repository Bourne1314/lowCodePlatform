package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.orgauth.core.service.SysApiResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * api资源请求接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/5/22 9:28
 */
@RestController
@RequestMapping("/orgauth/sysApis")
@Api("系统api资源")
public class SysApiResourceControllerO {
    @Resource(name = "sysApiResourceServiceO")
    SysApiResourceService sysApiResourceService;

    /**
     * 保存应用api资源
     *
     * @param apis
     * @return boolean
     * @author shanwj
     * @date 2019/5/22 10:21
     */
    @ApiOperation(value = "保存应用api资源", httpMethod = "POST")
    @ApiImplicitParam(name = "apis", value = "应用api资源列表",
            required = true, dataType = "List<SysApiResourceDO>")
    @RequestMapping(method = RequestMethod.POST)
    public boolean save(@RequestBody List<SysApiResourceDO> apis) {
        return sysApiResourceService.saveAppApi(apis);
    }

    /**
     * 删除应用api资源
     *
     * @param appId
     * @return boolean
     * @author shanwj
     * @date 2019/5/22 10:21
     */
    @ApiOperation(value = "保存应用api资源", httpMethod = "POST")
    @ApiImplicitParam(name = "apis", value = "应用api资源列表",
            required = true, dataType = "List<SysApiResourceDO>")
    @RequestMapping(value = "/{appId}", method = RequestMethod.DELETE)
    public boolean save(@PathVariable("appId") String appId) {
        return sysApiResourceService.remove(new QueryWrapper<SysApiResourceDO>().eq("app_id", appId));
    }


}
