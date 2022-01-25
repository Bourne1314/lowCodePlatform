package com.csicit.ace.testapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IMessage;
import com.csicit.ace.interfaces.service.ISecurity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interfaces")
@Api("接口请求")
public class interfaceController {
    @Autowired
    private ISecurity security;

    @Autowired
    private IMessage message;

    /**
     * 获取接收人部门信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取接收人部门信息", httpMethod = "GET", notes = "获取接收人部门信息")
    @RequestMapping(value = "/getManageDepName/{id}", method = RequestMethod.GET)
    @CrossOrigin
    public R getManageDepName(@PathVariable("id") String id) {
        OrgDepartmentDO orgDepartmentDO = security.getMainDeptByUserId(id);
        return R.ok().put("dep", orgDepartmentDO);
    }

    /**
     * 查询用户未读消息列表(分页)
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "获取历史信息", httpMethod = "GET", notes = "获取历史信息")
    @RequestMapping(value = "/sysMsgs/page/noReadMsg", method = RequestMethod.GET)
    public R listUserNoReadMsgInPage(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String userId = (String) params.get("userId");
        Page<SysMessageDO> page = message.listUserNoReadMsgInPage(userId, size, current);
        return R.ok().put("page", page);
    }

    /**
     * 查询用户已读消息列表(分页)
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "获取历史信息", httpMethod = "GET", notes = "获取历史信息")
    @RequestMapping(value = "/sysMsgs/page/readMsg", method = RequestMethod.GET)
    public R listUserReadMsgInPage(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String userId = (String) params.get("userId");
        Page<SysMessageDO> page = message.listUserReadMsgInPage(userId, size, current);
        return R.ok().put("page", page);
    }

    /**
     * 查询用户未读消息列表
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "查询用户未读消息列表", httpMethod = "GET", notes = "查询用户未读消息列表")
    @RequestMapping(value = "/sysMsgs/query/noReadMsg", method = RequestMethod.GET)
    public R listUserNoReadMsg(@RequestParam Map<String, Object> params) {
        String userId = (String) params.get("userId");
        List<SysMessageDO> list = message.listUserNoReadMsg(userId);
        return R.ok().put("list", list);
    }

    /**
     * 更新用户未读消息
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "更新用户未读消息", httpMethod = "POST", notes = "更新用户未读消息")
    @RequestMapping(value = "/sysMsgs/action/update/msgRead", method = RequestMethod.POST)
    public R updateUserNoReadMsg(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String msgId = params.get("msgId");
        if (message.updateUserNoReadMsg(userId, msgId)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 更新用户所有未读消息
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "更新用户所有未读消息", httpMethod = "POST", notes = "更新用户所有未读消息")
    @RequestMapping(value = "/sysMsgs/action/update/allMsgRead", method = RequestMethod.POST)
    public R updateUserAllNoReadMsg(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        if (message.updateUserAllNoReadMsg(userId)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除用户一条消息
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "删除用户一条消息", httpMethod = "POST", notes = "删除用户一条消息")
    @RequestMapping(value = "/sysMsgs/action/delete/msg", method = RequestMethod.POST)
    public R deleteMsg(@RequestBody Map<String, String> params) {
        String msgId = params.get("msgId");
        if (message.deleteMsg(msgId)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 删除用户所有条消息
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2020/4/21 9:30
     */
    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
    @ApiOperation(value = "删除用户所有条消息", httpMethod = "POST", notes = "删除用户所有条消息")
    @RequestMapping(value = "/sysMsgs/action/delete/msgs", method = RequestMethod.POST)
    public R deleteMsgs(@RequestBody Map<String, Object> params) {
        List<String> ids = (ArrayList)params.get("msgIds");
        if (message.deleteMsgs(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
