package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.platform.core.service.SysAuthMixService;
import com.csicit.ace.platform.core.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zuogang
 * @date Created in 15:49 2019/11/20
 */
@RestController
@RequestMapping("/commonMethods")
@Api("公用方法")
public class CommonMethodController extends BaseController {

    private static int ADD_INDEX = 10;

    @Autowired
    AceSqlUtils aceSqlUtils;

    /**
     * 根据参数获取当前数据下的最大排序号
     *
     * @param params 参数
     * @return 单个权限
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiOperation(value = "根据参数获取当前数据下的最大排序号", httpMethod = "POST", notes = "根据参数获取当前数据下的最大排序号")
    @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Map")
    @AceAuth("根据参数获取当前数据下的最大排序号")
    @RequestMapping(value = "/getMaxSortNo", method = RequestMethod.POST)
    public R getMaxSortNo(@RequestBody Map<String, Object> params) {
        int addIndex = ADD_INDEX;
        if (StringUtils.isNotBlank((String) params.get("groupId"))) {
            String addIndexStr = sysConfigService.getValueByGroup((String) params.get("groupId"), "INDEX_SPACE");
            if (StringUtils.isNotBlank(addIndexStr)) {
                addIndex = Integer.parseInt(addIndexStr);
            }
            return R.ok().put("maxSortNo", addIndex + aceSqlUtils.getMaxSort(params));
        } else if (StringUtils.isNotBlank((String) params.get("appId"))) {
            String addIndexStr = sysConfigService.getValueByApp((String) params.get("appId"), "INDEX_SPACE");
            if (StringUtils.isNotBlank(addIndexStr)) {
                addIndex = Integer.parseInt(addIndexStr);
            }
            return R.ok().put("maxSortNo", addIndex + aceSqlUtils.getMaxSort(params));

        } else {
            String addIndexStr = sysConfigService.getValue("INDEX_SPACE");
            if (StringUtils.isNotBlank(addIndexStr)) {
                addIndex = Integer.parseInt(addIndexStr);
            }
            return R.ok().put("maxSortNo", addIndex + aceSqlUtils.getMaxSort(params));

        }
    }

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @RequestMapping(value = "/action/saveAuthMix/{type}", method = RequestMethod.POST)
    public R saveAuthMix(@PathVariable("type") int type, @RequestBody List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            String appId = sysUserRoleService.getOne(new QueryWrapper<SysUserRoleDO>().eq("role_id", ids.get(0)))
                    .getAppId();
            if (type == 1) {
                // 同步角色权限
                sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>().in("role_id", ids))
                        .stream()
                        .map(SysUserRoleDO::getUserId).collect(Collectors.toList()).forEach(id -> {
                    sysAuthMixService.saveAuthMixForApp(id,appId);
                });
            }
        }
        return R.ok();
    }

}
