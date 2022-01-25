package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.BladeVisualMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 大屏消息 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:49:49
 */

@RestController
@RequestMapping("/bladeVisualMsgs")
@Api("大屏消息")
public class BladeVisualMsgController extends BaseController {

    @Autowired
    private BladeVisualMsgService bladeVisualMsgService;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BladeVisualMsgDO instance = bladeVisualMsgService.getInfo(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<BladeVisualMsgDO> page = new Page<>(current, size);
        IPage list = bladeVisualMsgService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    /**
     * 不分页获取列表
     *
     * @param appId 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiOperation(value = "不分页获取列表", httpMethod = "GET", notes = "不分页获取列表")
    @ApiImplicitParam(name = "appId", value = "参数", required = true, dataType = "String")
    @AceAuth("不分页获取列表")
    @RequestMapping(value = "/query/listNoPage/{appId}", method = RequestMethod.GET)
    public R listNoPage(@PathVariable("appId") String appId) {
        List<BladeVisualMsgDO> list = bladeVisualMsgService.list(new QueryWrapper<BladeVisualMsgDO>().eq("app_id",
                appId));
        return R.ok().put("list", list);

    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualMsgDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody BladeVisualMsgDO instance) {
        if (bladeVisualMsgService.count(new QueryWrapper<BladeVisualMsgDO>().eq("app_id", instance.getAppId())
                .eq("name", instance.getName())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
            ));
        }
        if (bladeVisualMsgService.count(new QueryWrapper<BladeVisualMsgDO>().eq("app_id", instance.getAppId())
                .eq("code", instance.getCode())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
            ));
        }
        if (bladeVisualMsgService.saveBladeVisualMsg(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "BladeVisualMsgDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody BladeVisualMsgDO instance) {
        BladeVisualMsgDO bladeVisualMsgDO = bladeVisualMsgService.getById(instance.getId());
        if (!Objects.equals(bladeVisualMsgDO.getName(), instance.getName())) {
            if (bladeVisualMsgService.count(new QueryWrapper<BladeVisualMsgDO>().eq("app_id", instance.getAppId())
                    .eq("name", instance.getName())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), instance.getName()}
                ));
            }
        }
        if(!Objects.equals(bladeVisualMsgDO.getCode(), instance.getCode())){
            if (bladeVisualMsgService.count(new QueryWrapper<BladeVisualMsgDO>().eq("app_id", instance.getAppId())
                    .eq("code", instance.getCode())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("CODE"), instance.getCode()}
                ));
            }
        }
        if (bladeVisualMsgService.updBladeVisualMsg(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param map
     * @return 删除响应结果
     * @author generator
     * @date 2020-07-29 16:49:49
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "map", value = "map", required = true, allowMultiple = true, dataType = "map")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, String> map) {
        if (bladeVisualMsgService.delBladeVisualMsg(map.get("id"))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 业务平台内部定时任务
     * 扫描大屏消息表数据，在时间范围内向前台推送消息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2020/7/30 10:13
     */
    @ApiOperation(value = "扫描大屏消息表数据，在时间范围内向前台推送消息", httpMethod = "GET", notes = "扫描大屏消息表数据，在时间范围内向前台推送消息")
    @RequestMapping(value = "/bladeVisual/msg/push", method = RequestMethod.GET)
    public void bladeVisualMsgPush() {
        bladeVisualMsgService.bladeVisualMsgPush();
    }

}
