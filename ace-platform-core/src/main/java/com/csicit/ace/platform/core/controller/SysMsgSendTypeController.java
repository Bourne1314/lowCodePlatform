package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysMsgSendTypeDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMsgSendTypeService;
import com.csicit.ace.data.persistent.service.SysMsgTypeExtendService;
import com.csicit.ace.dbplus.MapWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/8/27 9:16
 */
@RestController
@RequestMapping("/sysMsgSendTypes")
public class SysMsgSendTypeController extends BaseController{

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;
    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    /**
     * 根据id获取单个发送方式
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "单个发送方式", httpMethod = "GET", notes = "根据id获取单个发送方式")
    @AceAuth("获取单个发送方式")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysMsgSendTypeDO sendTypeDO = sysMsgSendTypeService.getById(id);
        if(sendTypeDO!=null){
            String sendMode = sendTypeDO.getSendMode();
            List<String> modeList = new ArrayList<>();
            if(sendMode!=null){
                if(sendMode.contains(",")){
                    modeList = Arrays.asList(sendMode.split(","));
                }else{
                    modeList.add(sendMode);
                }
            }
            sendTypeDO.setModeList(modeList);
        }
        return R.ok().put("instance", sendTypeDO);
    }


    /**
     * 根据条件获取发送方式列表并分页
     *
     * @param params 请求参数map对象
     * @return 发送方式列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取发送方式列表并分页", httpMethod = "GET", notes = "获取发送方式列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取发送方式列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List list = sysMsgSendTypeService.list(MapWrapper.getEqualInstance(params));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        Page<SysMsgSendTypeDO> page = new Page<>(current, size);
        IPage list = sysMsgSendTypeService.page(page, MapWrapper.getEqualInstance(params));
        return R.ok().put("page", list);
    }

    @AceAuth("获取发送方式")
    @RequestMapping(value = "/query/sendTypes/{appId}",method = RequestMethod.GET)
    public R listSendTypes(@PathVariable("appId")String appId){
        return R.ok().put("list",sysMsgSendTypeService.listSendTypes(appId));
    }

    /**
     * 新增发送方式保存
     *
     * @param instance 发送方式对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存发送方式")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgSendTypeDO")
    @AceAuth("保存发送方式对象")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysMsgSendTypeDO instance) {
        // 判断发送方式名称是否已存在
        int count =
                sysMsgSendTypeService.count(
                        new QueryWrapper<SysMsgSendTypeDO>()
                                .eq("channel_name", instance.getChannelName())
                                .eq("app_id",instance.getAppId()));
        if (count > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",instance.getChannelName()));
        }
        if (sysMsgSendTypeService.save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


    /**
     * 修改发送方式保存
     *
     * @param instance 对象
     * @return 修改发送方式
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改发送方式", httpMethod = "PUT", notes = "修改发送方式")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysMsgSendTypeDO")
    @AceAuth("修改发送方式")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysMsgSendTypeDO instance) {
        if (sysMsgSendTypeService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除字典类型")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除发送方式对象")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysMsgSendTypeService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

}
