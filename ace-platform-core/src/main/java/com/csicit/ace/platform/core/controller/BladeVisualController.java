package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BladeVisualService;
import com.csicit.ace.platform.core.service.SysAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 大屏信息数据表 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */

@RestController
@RequestMapping("/bladeVisuals")
@Api("大屏信息数据表")
public class BladeVisualController extends BaseController {

    @Autowired
    private BladeVisualService bladeVisualService;

    @Autowired
    private SysAuthService sysAuthService;

    /**
     * @param sqlCode
     * @param httpRequest
     * @return
     * @author zuogang
     * @date 2020/6/15 10:20
     */
    @RequestMapping(value = "/commonResult/{sqlCode}", method = RequestMethod.GET)
    public R getSqlResult(@PathVariable("sqlCode") String sqlCode, HttpServletRequest httpRequest) throws
            UnsupportedEncodingException {
        String queryString = httpRequest.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            queryString = URLDecoder.decode(queryString, "utf-8");
        }

        return bladeVisualService.getSqlResult(sqlCode, queryString);
    }

    /**
     * 通过title得到ID
     *
     * @param params
     * @return
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiImplicitParam(name = "params", value = "params", dataType = "Map", required = true)
    @ApiOperation(value = "通过title得到ID", httpMethod = "GET", notes = "通过title得到ID")
    @AceAuth("通过title得到ID")
    @RequestMapping(value = "/action/getIdByTitle", method = RequestMethod.GET)
    public R getIdByTitle(@RequestParam Map<String, String> params) {
        String title = params.get("title");
        String appId = params.get("appId");
        BladeVisualDO bladeVisualDO = bladeVisualService.getOne(new QueryWrapper<BladeVisualDO>()
                .eq("title", title).eq("app_id", appId));
        if (bladeVisualDO == null) {
            return R.error("当前大屏尚未设计");
        }
        if (!securityUtils.isAdmin() && StringUtils.isNotEmpty(bladeVisualDO.getAuthId())) {
            List<SysAuthMixDO> list = sysAuthMixService.list(
                    new QueryWrapper<SysAuthMixDO>()
                            .eq("auth_id", bladeVisualDO.getAuthId())
                            .eq("user_id", securityUtils.getCurrentUserId()));
            if (list == null || list.size() == 0) {
                return R.error("当前用户没有查看权限");
            }
        }
        return R.ok().put("id", bladeVisualDO.getId());
    }

    /**
     * @param id
     * @return
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        return bladeVisualService.getInfo(id);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<BladeVisualDO> page = new Page<>(current, size);
        IPage list = bladeVisualService.page(page, new QueryWrapper<BladeVisualDO>().eq("is_deleted", 0)
                .eq("category", params.get("category")));
        List<BladeVisualDO> bladeVisualDOList = list.getRecords();
        bladeVisualDOList.stream().forEach(bladeVisualDO -> {
            if (StringUtils.isNotBlank(bladeVisualDO.getAuthId())) {
                bladeVisualDO.setAuthName(sysAuthService.getById(bladeVisualDO.getAuthId()).getName());
            }
        });
        list.setRecords(bladeVisualDOList);
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BladeVisualDO instance) {
        if (bladeVisualService.count(new QueryWrapper<BladeVisualDO>().eq
                ("app_id", instance.getAppId()).eq("title", instance.getTitle()).eq("is_deleted", 0)) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getTitle()}
            ));
        }
        if (bladeVisualService.saveBladeVisual(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改大屏名称和类型
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/6/16 14:28
     */
    @ApiOperation(value = "修改大屏名称和类型", httpMethod = "POST", notes = "修改大屏名称和类型")
    @ApiImplicitParam(name = "map", value = "map", required = true, dataType = "map")
    @AceAuth("修修改大屏名称和类型改")
    @RequestMapping(value = "/update/bladeVisual", method = RequestMethod.POST)
    public R updateBladeVisual(@RequestBody Map<String, String> map) {
        String title = map.get("title");
        String category = map.get("category");
        String id = map.get("id");
        String authId = map.get("authId");
        BladeVisualDO old = bladeVisualService.getById(id);
        if (!Objects.equals(old.getTitle(), title)) {
            if (bladeVisualService.count(new QueryWrapper<BladeVisualDO>().eq
                    ("app_id", old.getAppId()).eq("title", title).eq("is_deleted", 0)) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), title}
                ));
            }
        }
        BladeVisualDO instance = bladeVisualService.getById(id);
        instance.setCategory(category);
        instance.setAuthId(authId);
        instance.setTitle(title);
        if (bladeVisualService.updateBladeVisual(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BladeVisualDO instance) {
        BladeVisualDO old = bladeVisualService.getById(instance.getId());
        if (!Objects.equals(old.getTitle(), instance.getTitle())) {
            if (bladeVisualService.count(new QueryWrapper<BladeVisualDO>().eq
                    ("app_id", instance.getAppId()).eq("title", instance.getTitle()).eq("is_deleted", 0)) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getTitle()}
                ));
            }
        }
        if (bladeVisualService.updateBladeVisual(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param map ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "map", value = "map", required = true, allowMultiple = true, dataType = "map")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, String> map) {
        if (bladeVisualService.deleteBladeVisual(map.get("id"))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 复制
     *
     * @param map
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "复制", httpMethod = "POST", notes = "复制")
    @ApiImplicitParam(name = "ids", value = "map", required = true, allowMultiple = true, dataType = "map")
    @AceAuth("复制")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public R copyBladeVisual(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if (!bladeVisualService.copyBladeVisual(id)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 触发消息推送方法
     *
     * @param map
     * @author generator
     * @date 2020-06-05 09:59:54
     */
    @ApiOperation(value = "触发推送方法", httpMethod = "POST", notes = "触发推送方法")
    @ApiImplicitParam(name = "map", value = "map", required = true, allowMultiple = true, dataType = "map")
    @AceAuth("触发推送方法")
    @RequestMapping(value = "/blade/messagePush", method = RequestMethod.POST)
    public void bladeMessagePush(@RequestBody Map<String, Object> map) {
        bladeVisualService.bladeMessagePush(map);
    }

}
