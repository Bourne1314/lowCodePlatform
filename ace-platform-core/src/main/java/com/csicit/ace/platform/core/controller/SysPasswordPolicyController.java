package com.csicit.ace.platform.core.controller;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.SysPasswordPolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 密码策略 接口访问层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/19 11:49
 */
@RestController
@RequestMapping("/sysPasswordPolicys")
@Api("密码策略管理")
public class SysPasswordPolicyController extends BaseController {

    @Autowired
    SysPasswordPolicyService sysPasswordPolicyService;

    /**
     * 获取单个密码策略
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个密码策略", httpMethod = "GET", notes = "获取单个密码策略")
    @AceAuth("获取单个密码策略")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysPasswordPolicyDO instance = sysPasswordPolicyService.getById(id);
        String useEl = cacheUtil.get("ace-password-policy-useEl");
        if (Objects.equals(useEl,"yes")) {
            instance.setUseEl(true);
        } else {
            instance.setUseEl(false);
        }
        return R.ok().put("instance", instance);
    }

//    /**
//     * 获取密码策略列表
//     *
//     * @param params 请求参数map对象
//     * @return
//     * @author yansiyang
//     * @date 2019-04-15 15:20:38
//     */
//    @ApiOperation(value = "获取密码策略列表", httpMethod = "GET")
//    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
//    @AceAuth("获取密码策略列表")
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public R list(@RequestParam Map<String, Object> params) {
//        int current = Integer.parseInt((String) params.get("current"));
//        int size = Integer.parseInt((String) params.get("size"));
//        Page<SysPasswordPolicyDO> page = new Page<>(current, size);
//        IPage list = sysPasswordPolicyService.page(page, null);
//        return R.ok().put("page", list);
//    }

//    /**
//     * 保存密码策略
//     *
//     * @param instance 对象
//     * @return 保存响应结果
//     * @author yansiyang
//     * @date 2019-04-15 15:20:38
//     */
//    @ApiOperation(value = "保存密码策略", httpMethod = "POST")
//    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysPasswordPolicyDO")
//    @AceAuth("保存密码策略")
//    @RequestMapping(value = "", method = RequestMethod.POST)
//    public R save(@RequestBody SysPasswordPolicyDO instance) {
//        /**
//         * 只允许非密单位维护密码策略
//         */
//        return sysPasswordPolicyService.insert(instance);
//    }

    /**
     * 修改密码策略
     *
     * @param instance 对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:38
     */
    @ApiOperation(value = "修改密码策略", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysPasswordPolicyDO")
    @AceAuth("修改密码策略")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysPasswordPolicyDO instance) {
        /**
         * 只允许非密单位维护密码策略
         */
        if (instance.isUseEl()) {
            cacheUtil.set("ace-password-policy-useEl", "yes");
        } else {
            cacheUtil.set("ace-password-policy-useEl", "no");
        }
        instance.setUpdateTime(LocalDateTime.now());
        return sysPasswordPolicyService.update(instance);
    }

//    /**
//     * 删除密码策略
//     *
//     * @param ids ID数组
//     * @return com.csicit.ace.common.utils.server.R
//     * @author yansiyang
//     * @date 2019-04-15 15:20:38
//     */
//    @ApiOperation(value = "删除密码策略", httpMethod = "DELETE")
//    @ApiImplicitParam(name = "groupIds", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
//    @AceAuth("删除密码策略")
//    @RequestMapping(value = "", method = RequestMethod.DELETE)
//    public R delete(@RequestBody String[] ids) {
//        /**
//         * 只允许非密单位维护密码策略
//         */
//        return sysPasswordPolicyService.delete(ids);
//    }
}
