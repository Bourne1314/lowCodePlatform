package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.SysMsgTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/7/5 11:13
 */
@RestController
@RequestMapping("/orgauth/sysMsgs")
public class SysMessageController {

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    @RequestMapping(value = "/action/getMsgTem/{appId}/{code}", method = RequestMethod.GET)
    public SysMsgTemplateDO get(@PathVariable("appId") String appId, @PathVariable("code") String code) {
        if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(code)) {
            return sysMsgTemplateService.getOne(new QueryWrapper<SysMsgTemplateDO>().eq("app_id", appId)
                    .eq("template_Id", code));
        }
        return new SysMsgTemplateDO();
    }

    @RequestMapping(method = RequestMethod.POST)
    public R sendMsg(@RequestBody SysMessageDO sysMessageDO) {
        return sysMessageService.sendMsg(sysMessageDO);
    }

    @RequestMapping(value = "/action/fire/socketEvent", method = RequestMethod.POST)
    public R fireSocketEvent(@RequestBody SocketEventVO socketEvent) {
        return sysMessageService.fireSocketEvent(socketEvent);
    }

    /**
     * 查询用户所有消息列表
     *
     * @param userId 用户id
     * @param appId  应用id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:41
     */
    @RequestMapping(value = "/query/allMsg/{userId}/{appId}", method = RequestMethod.GET)
    public List<SysMessageDO> listUserAllMsg(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId) {
        return sysMessageService.listAllMsg(userId, appId);
    }

    @RequestMapping(value = "/page/allMsg/{userId}/{appId}/{size}/{current}", method = RequestMethod.GET)
    Page<SysMessageDO> getPageAllMsgList(@PathVariable("userId") String userId,
                                         @PathVariable("appId") String appId,
                                         @PathVariable("size") int size,
                                         @PathVariable("current") int current) {
        return sysMessageService.pageAllMsg(userId, appId, size, current);
    }


    /**
     * 查询用户已读消息列表
     *
     * @param userId 用户id
     * @param appId  应用id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:41
     */
    @RequestMapping(value = "/query/readMsg/{userId}/{appId}", method = RequestMethod.GET)
    public List<SysMessageDO> listUserReadMsg(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId) {
        return sysMessageService.listRead(userId, appId);
    }

    @RequestMapping(value = "/page/readMsg/{userId}/{appId}/{size}/{current}", method = RequestMethod.GET)
    Page<SysMessageDO> getPageReadMsgList(@PathVariable("userId") String userId,
                                          @PathVariable("appId") String appId,
                                          @PathVariable("size") int size,
                                          @PathVariable("current") int current) {
        return sysMessageService.pageRead(userId, appId, size, current);
    }


    /**
     * 查询用户未读消息列表
     *
     * @param userId 用户id
     * @param appId  应用id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:41
     */
    @RequestMapping(value = "/query/noReadMsg/{userId}/{appId}", method = RequestMethod.GET)
    public List<SysMessageDO> listUserNoReadMsg(@PathVariable("userId") String userId, @PathVariable("appId") String
            appId) {
        return sysMessageService.listNoRead(userId, appId);
    }

    @RequestMapping(value = "/page/noReadMsg/{userId}/{appId}/{size}/{current}", method = RequestMethod.GET)
    Page<SysMessageDO> getPageNoReadMsgList(@PathVariable("userId") String userId,
                                            @PathVariable("appId") String appId,
                                            @PathVariable("size") int size,
                                            @PathVariable("current") int current) {
        return sysMessageService.pageNoRead(userId, appId, size, current);
    }


    /**
     * 更新用户消息阅读状态
     *
     * @param userId
     * @param msgId
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:42
     */
    @RequestMapping(value = "/action/update/msgRead", method = RequestMethod.POST)
    public boolean updateMsgRead(@RequestParam("userId") String userId, @RequestParam("msgId") String msgId) {
        return sysMessageService.updateMsgRead(userId, msgId);
    }

    @RequestMapping(value = "/action/update/allMsgRead", method = RequestMethod.POST)
    public boolean updateUserAllNoReadMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId) {
        return sysMessageService.updateUserAllNoReadMsg(userId, appId);
    }


    @RequestMapping(value = "/action/delete/msg", method = RequestMethod.POST)
    public boolean deleteMsg(@RequestParam("msgId") String msgId, @RequestParam("userId") String userId) {
        return sysMessageService.deleteMsg(msgId, userId);
    }

    @RequestMapping(value = "/action/delete/msgs", method = RequestMethod.POST)
    public boolean deleteMsgs(@RequestParam("ids[]") String[] ids, @RequestParam("userId") String userId) {
        return sysMessageService.deleteMsgs(Arrays.asList(ids), userId);
    }

    @RequestMapping(value = "/action/delete/msgs/forInterface", method = RequestMethod.POST)
    public boolean deleteMsgsForInterface(@RequestBody List<String> ids,@RequestParam("userId") String userId) {
        return sysMessageService.deleteMsgs(ids, userId);
    }


    @RequestMapping(value = "/action/delete/all", method = RequestMethod.POST)
    boolean deleteAllMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId) {
        return sysMessageService.deleteAllMsgs(userId, appId);
    }

    @RequestMapping(value = "/action/delete/readAll", method = RequestMethod.POST)
    boolean deleteAllReadMsg(@RequestParam("userId") String userId, @RequestParam("appId") String appId) {
        return sysMessageService.deleteAllReadMsgs(userId, appId);
    }

}
