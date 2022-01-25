package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOServer;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.SysMessageMapper;
import com.csicit.ace.data.persistent.messenger.AbstractMessengerImpl;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import com.csicit.ace.data.persistent.service.*;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shanwj
 * @date 2019/7/5 11:12
 */
@Slf4j
@Service
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessageMapper, SysMessageDO>
        implements SysMessageService {

    @Value("${server.port:2100}")
    private Integer port;

//    @Value("${ace.socket.sendAppType:0}")
//    private Integer sendAppType;
//
//    @Value("${ace.socket.sendApps:#{null}}")
//    private String sendApps;
//
//    @Value("${ace.socket.unReadApps:#{null}}")
//    private String unReadApps;

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    SysMsgUnReadService sysMsgUnReadService;

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    @Autowired
    SysMsgReadService sysMsgReadService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    @Autowired
    SysMsgTemplateConfigService sysMsgTemplateConfigService;

    @Autowired
    HttpClient client;

    @Autowired
    private Map<String, IMessenger> messengerMap;

    @Autowired
    @Nullable
    SocketIOServer socketIOServer;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    @Override
    public R sendMsg(SysMessageDO sysMessageDO) {
        if (StringUtils.isEmpty(sysMessageDO.getReceiveUsers())) {
            return R.error("消息接收人不能为空!");
        }
        if (StringUtils.isEmpty(sysMessageDO.getChannelName())) {
            return R.error("消息频道不能为空!");
        }
        if (StringUtils.isEmpty(sysMessageDO.getTemplateId())) {
            return R.error("消息模板标识不能为空!");
        }
        SysMsgSendTypeDO sysMsgSendType = sysMsgSendTypeService.getOne(
                new QueryWrapper<SysMsgSendTypeDO>()
                        .eq("channel_name", sysMessageDO.getChannelName())
                        .eq("app_id", sysMessageDO.getAppId()));
        if (sysMsgSendType == null) {
            return R.error("查不到当前消息频道!");
        }
        String[] sendTypes = sysMsgSendType.getSendMode().split(",");
        if (sendTypes.length == 0) {
            return R.error("消息频道定义发送方式为空!");
        }
        SysMsgTemplateDO sysMsgTemplate = sysMsgTemplateService.getOne(new QueryWrapper<SysMsgTemplateDO>()
                .eq("template_id", sysMessageDO.getTemplateId())
                .eq("app_id", sysMessageDO.getAppId()));
        if (sysMsgTemplate == null) {
            return R.error("查不到当前消息模板!");
        }
        if (StringUtils.isNotEmpty(sysMsgTemplate.getAuth())) {
            String auth = sysMsgTemplate.getAuth();
            String userId = securityUtils.getCurrentUserId();
            if (sysAuthMixService.count(
                    new QueryWrapper<SysAuthMixDO>().eq("auth_id", auth).eq("user_id", userId)) == 0) {
                return R.error("当前用户没有使用当前消息模板权限!");
            }
        }
        sysMsgTemplate.setTemplateConfigs(
                sysMsgTemplateConfigService.list(
                        new QueryWrapper<SysMsgTemplateConfigDO>().eq("tid", sysMsgTemplate.getId())));

        sysMessageDO.setSysMsgTemplate(sysMsgTemplate);

        List<String> appIds = sysGroupAppServiceD.list(new QueryWrapper<SysGroupAppDO>().select("id")
                .inSql("group_id", "select group_id from sys_group_app where id='" + sysMessageDO.getAppId() + "'")).stream().map(SysGroupAppDO::getId).collect(Collectors.toList());

        //自定义消息拓展方式
        List<SysMsgTypeExtendDO> msgTypeExtends = new ArrayList<>(16);
        for (int i = 0; i < sendTypes.length; i++) {
            String name = sendTypes[i];
            SysMsgTypeExtendDO typeExtend =
                    sysMsgTypeExtendService.getOne(
                            new QueryWrapper<SysMsgTypeExtendDO>().eq("name", name)
                                    .in("app_id", appIds));
            if (typeExtend != null) {
                msgTypeExtends.add(typeExtend);
            }
        }
        Set<Map.Entry<String, IMessenger>> messengers = messengerMap.entrySet();
        //发送站内信会进行信息保存!
        for (Map.Entry<String, IMessenger> entry : messengers) {
            if (Objects.equals(entry.getKey(), "socketMessengerImpl")) {
                SysMessageDO message = AbstractMessengerImpl.replaceTempplateByData(sysMessageDO, Constants.SocketMessenger);
                List<String> userIds = Arrays.asList(sysMessageDO.getReceiveUsers().split(","));
                List<SysMsgUnReadDO> unReads = new ArrayList<>(16);
                sysMessageDO.setTitle(message.getTitle());
                sysMessageDO.setContent(message.getContent());
                sysMessageDO.setUrl(message.getUrl());
                sysMessageDO.setCreateTime(LocalDateTime.now());
                boolean flag = false;
                if (save(sysMessageDO)) {
                    for (String uId : userIds) {
                        SysMsgUnReadDO unRead = new SysMsgUnReadDO();
                        unRead.setUserId(uId);
                        unRead.setMsgId(sysMessageDO.getId());
                        unRead.setAppId(sysMessageDO.getAppId());
                        unReads.add(unRead);
                    }
                    flag = sysMsgUnReadService.saveBatch(unReads);
                }
                if (!flag) {
                    return R.error("保存站内信消息异常!");
                }
            }
        }
        for (Map.Entry<String, IMessenger> entry : messengers) {
            if (sysMsgSendType.getSendMode().contains(entry.getKey())) {
                entry.getValue().sendMsg(sysMessageDO);
            }
        }
        sendExtendMsg(msgTypeExtends, sysMessageDO);
        return R.ok();
    }

    @Override
    public R fireSocketEvent(SocketEventVO socketEvent) {
        if (Constants.isMonomerApp || Constants.isZuulApp) {
            if (CollectionUtils.isNotEmpty(socketEvent.getUserIds()) && !Objects.isNull(socketIOServer)) {
                for (String userId : socketEvent.getUserIds()) {
                    socketIOServer.getRoomOperations(userId).sendEvent("aceSocketEvent", socketEvent.getData());
                }
            }
        } else {
            Map<String, Object> map = new HashMap<>(16);
            map.put("to", socketEvent.getReceivers());
            map.put("appId", socketEvent.getAppId());
            map.put("data", socketEvent.getData());
            ServiceInstance push = discoveryUtils.getOneHealthyInstance(Constants.PUSHSERVER);
            if (Objects.nonNull(push)) {
                System.out.println("http://" + push.getHost() + ":" + push.getPort() + "/");
                httpClient.postReturnString("http://" + push.getHost() + ":" + push.getPort() + "/", map);
            }
        }
        return R.ok();
    }

    @Override
    public List<SysMessageDO> listAllMsg(String userId, String appId) {
        List<String> msgIds = new ArrayList<>(16);
        msgIds.addAll(sysMsgReadService.list(
                new QueryWrapper<SysMsgReadDO>().eq("user_id", userId))
                .stream().map(SysMsgReadDO::getMsgId).collect(Collectors.toList()));
        msgIds.addAll(sysMsgUnReadService.list(
                new QueryWrapper<SysMsgUnReadDO>().eq("user_id", userId))
                .stream().map(SysMsgUnReadDO::getMsgId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(msgIds)) {
            return null;
        }
        return list(new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc("create_time"));
    }

    @Override
    public Page<SysMessageDO> pageAllMsg(String userId, String appId, int size, int current) {
        List<String> msgIds = new ArrayList<>(16);
        msgIds.addAll(sysMsgReadService.list(
                new QueryWrapper<SysMsgReadDO>().eq("user_id", userId))
                .stream().map(SysMsgReadDO::getMsgId).collect(Collectors.toList()));
        msgIds.addAll(sysMsgUnReadService.list(
                new QueryWrapper<SysMsgUnReadDO>().eq("user_id", userId))
                .stream().map(SysMsgUnReadDO::getMsgId).collect(Collectors.toList()));
        Page<SysMessageDO> page = new Page<>(current, size);
        if (CollectionUtils.isEmpty(msgIds)) {
            return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().apply("1=2"));
        }
        return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc
                ("create_time"));
    }

    @Override
    public List<SysMessageDO> listRead(String userId, String appId) {
        List<String> msgIds = sysMsgReadService.list(
                new QueryWrapper<SysMsgReadDO>().eq("user_id", userId))
                .stream().map(SysMsgReadDO::getMsgId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(msgIds)) {
            return null;
        }
        return list(new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc("create_time"));
    }

    @Override
    public Page<SysMessageDO> pageRead(String userId, String appId, int size, int current) {
        List<String> msgIds = sysMsgReadService.list(
                new QueryWrapper<SysMsgReadDO>().eq("user_id", userId))
                .stream().map(SysMsgReadDO::getMsgId).collect(Collectors.toList());
        Page<SysMessageDO> page = new Page<>(current, size);
        if (CollectionUtils.isEmpty(msgIds)) {
            return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().apply("1=2"));
        }
        return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc
                ("create_time"));
    }

    @Override
    public List<SysMessageDO> listNoRead(String userId) {
        List<String> msgIds = sysMsgUnReadService.list(
                new QueryWrapper<SysMsgUnReadDO>().eq("user_id", userId))
                .stream().map(SysMsgUnReadDO::getMsgId).collect(Collectors.toList());

        List<SysMessageDO> list = new ArrayList<>(16);
        msgIds.stream().forEach(msgId -> {
            list.add(getById(msgId));
        });
        return list.stream()
                .sorted(Comparator.comparing(SysMessageDO::getCreateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<SysMessageDO> listNoRead(String userId, String appId) {
        List<String> msgIds = sysMsgUnReadService.list(
                new QueryWrapper<SysMsgUnReadDO>().eq("user_id", userId)
                        .select("id", "msg_id"))
                .stream().map(SysMsgUnReadDO::getMsgId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(msgIds)) {
            return null;
        }
//        if (sendAppType > 0) {
//            if (sendApps.contains(appId) && StringUtils.isNotBlank(unReadApps)) {
//                List<String> appIds = new ArrayList<>();
//                appIds.add(appId);
//                appIds.addAll(Arrays.asList(unReadApps.split(",")));
//                return list(new QueryWrapper<SysMessageDO>().in("app_id", appIds).in("id", msgIds).orderByDesc("create_time"));
//            }
//        }
        return list(new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc("create_time"));

    }

    @Override
    public Page<SysMessageDO> pageNoRead(String userId, String appId, int size, int current) {
        List<String> msgIds = sysMsgUnReadService.list(
                new QueryWrapper<SysMsgUnReadDO>().eq("user_id", userId))
                .stream().map(SysMsgUnReadDO::getMsgId).collect(Collectors.toList());
        Page<SysMessageDO> page = new Page<>(current, size);
        if (CollectionUtils.isEmpty(msgIds)) {
            return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().apply("1=2"));
        }
        return (Page<SysMessageDO>) page(page, new QueryWrapper<SysMessageDO>().eq("app_id", appId).in("id", msgIds).orderByDesc
                ("create_time"));
    }

    @Override
    public boolean updateMsgRead(String userId, String msgId) {
        SysMsgUnReadDO unReadDO =
                sysMsgUnReadService.getOne(
                        new QueryWrapper<SysMsgUnReadDO>()
                                .eq("user_id", userId)
                                .eq("msg_id", msgId));
        if (Objects.isNull(unReadDO)) {
            return true;
        }
        SysMsgReadDO readDO = JsonUtils.castObjectForSetIdNull(unReadDO, SysMsgReadDO.class);
        if (sysMsgReadService.save(readDO)) {
            return sysMsgUnReadService.removeById(unReadDO.getId());
        }
        return false;
    }

    @Override
    public boolean updateUserAllNoReadMsg(String userId, String appId) {
        List<SysMessageDO> list = listNoRead(userId, appId);
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        List<String> msgIds = list.stream().map(SysMessageDO::getId).collect(Collectors.toList());
        List<SysMsgUnReadDO> unReads =
                sysMsgUnReadService.list(new QueryWrapper<SysMsgUnReadDO>().in("msg_id", msgIds));
        unReads.forEach(read -> {
            SysMsgReadDO readDO = JsonUtils.castObjectForSetIdNull(read, SysMsgReadDO.class);
            if (sysMsgReadService.save(readDO)) {
                sysMsgUnReadService.removeById(read.getId());
            }
        });
        return true;
    }

    @Override
    public boolean deleteMsg(String msgId, String userId) {
        SysMsgReadDO sysMsgReadDO = sysMsgReadService.getOne(new QueryWrapper<SysMsgReadDO>()
                .eq("user_id", userId).eq("msg_id", msgId));
        if (sysMsgReadDO != null) {
            if (!sysMsgReadService.removeById(sysMsgReadDO.getId())) {
                return false;
            }
        }
        SysMsgUnReadDO sysMsgUnReadDO = sysMsgUnReadService.getOne(new
                QueryWrapper<SysMsgUnReadDO>()
                .eq("user_id", userId).eq("msg_id", msgId));
        if (sysMsgUnReadDO != null) {
            if (!sysMsgUnReadService.removeById(sysMsgUnReadDO.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteMsgs(List<String> msgIds, String userId) {
        if (CollectionUtils.isNotEmpty(msgIds)) {
            return this.deleteMsgUserRelation(msgIds, userId);
        }
        return true;
    }

    @Override
    public boolean deleteAllMsgs(String userId, String appId) {
        List<String> msgIds = listAllMsg(userId, appId).stream().map(SysMessageDO::getId).collect(Collectors
                .toList());
        if (CollectionUtils.isNotEmpty(msgIds)) {
            return this.deleteMsgUserRelation(msgIds, userId);
        }
        return true;
    }

    @Override
    public boolean deleteAllReadMsgs(String userId, String appId) {
        List<String> msgIds = listRead(userId, appId).stream().map(SysMessageDO::getId).collect(Collectors.toList
                ());
        if (CollectionUtils.isNotEmpty(msgIds)) {
            return this.deleteMsgUserRelation(msgIds, userId);
        }
        return true;
    }

    private boolean deleteMsgUserRelation(List<String> msgIds, String userId) {
        List<SysMsgReadDO> sysMsgReadDOS = sysMsgReadService.list(new QueryWrapper<SysMsgReadDO>()
                .eq("user_id", userId).in("msg_id", msgIds));
        if (CollectionUtils.isNotEmpty(sysMsgReadDOS)) {
            if (!sysMsgReadService.removeByIds(sysMsgReadDOS.stream().map(SysMsgReadDO::getId).collect(Collectors
                    .toList()))) {
                return false;
            }
        }
        List<SysMsgUnReadDO> sysMsgUnReadDOS = sysMsgUnReadService.list(new QueryWrapper<SysMsgUnReadDO>()
                .eq("user_id", userId).in("msg_id", msgIds));
        if (CollectionUtils.isNotEmpty(sysMsgUnReadDOS)) {
            if (!sysMsgUnReadService.removeByIds(sysMsgUnReadDOS.stream().map(SysMsgUnReadDO::getId).collect(Collectors
                    .toList()))) {
                return false;
            }
        }
        return true;
    }


    private void sendExtendMsg(List<SysMsgTypeExtendDO> msgTypeExtends, SysMessageDO sysMessageDO) {
        try {
            if (CollectionUtils.isEmpty(msgTypeExtends)) {
                return;
            }
            msgTypeExtends.forEach(extend -> {
                String appId = extend.getAppId();
                String url = extend.getUrl();
                String appAddress;
                if (Constants.isZuulApp) {
                    appAddress = "http://127.0.0.1:" + port + "/" + appId;
                } else {
                    appAddress = client.getAppAddr(appId);
                }
                url = appAddress + url + "?token=" + securityUtils.getToken();
                SysMessageDO result = AbstractMessengerImpl.replaceTempplateByData(sysMessageDO, extend.getName());
                client.postReturnString(url, result);
            });
        } catch (Exception e) {
            log.error("发送消息拓展失败，请检查消息拓展配置！");
            e.printStackTrace();
        }
    }
}
