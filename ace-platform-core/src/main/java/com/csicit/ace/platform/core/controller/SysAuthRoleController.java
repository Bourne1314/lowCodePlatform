package com.csicit.ace.platform.core.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysAuthRoleDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 权限角色管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/sysAuthRole")
@Api("权限角色管理")
public class SysAuthRoleController extends BaseController {

    /**
     * 获取权限角色列表
     *
     * @param params 请求参数map对象
     * @return 权限用户列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取权限角色列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取权限角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<SysAuthRoleDO> page = new Page<>(current, size);
        IPage list = sysAuthRoleService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }


}
