package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.MsgSendTypeDetail;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysMsgSendTypeMapper;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.SysMsgSendTypeService;
import com.csicit.ace.data.persistent.service.SysMsgTemplateService;
import com.csicit.ace.data.persistent.service.SysMsgTypeExtendService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shanwj
 * @date 2019/8/26 10:12
 */
@Service("sysMsgSendTypeService")
public class SysMsgSendTypeServiceImpl extends BaseServiceImpl<SysMsgSendTypeMapper, SysMsgSendTypeDO>
        implements SysMsgSendTypeService {

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    @Autowired
    Map<String, IMessenger> messengerMap;

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    @Resource(name = "sysGroupAppServiceD")
    SysGroupAppServiceD sysGroupAppService;

    @Override
    public boolean createBpmChannelAndTemplate(String appId) {
        // 应用不存在 不进行此操作
        if (sysGroupAppService.count(new QueryWrapper<SysGroupAppDO>()
                .eq("id", appId)) == 0) {
            return true;
        }
        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmTransferNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmTransferNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmTransferNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmTransferNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }

        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmCommentNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmCommentNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmCommentNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmCommentNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }

        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmDelegateNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmDelegateNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmDelegateNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmDelegateNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }

        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmDrawBackNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmDrawBackNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmDrawBackNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmDrawBackNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }

        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmFinshNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmFinshNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmFinshNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmFinshNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }

        if (count(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId).eq("channel_name", Constants.BpmRejectNoticeChannelName)) == 0) {
            SysMsgSendTypeDO sysMsgSendType = createBpmChannel(Constants.BpmRejectNoticeChannelName, appId);
            if (!save(sysMsgSendType)) {
                return false;
            }
        }
        if (sysMsgTemplateService.count(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId).eq("template_id", Constants.BpmRejectNoticeTemplate)) == 0) {
            SysMsgTemplateDO sysMsgTemplate = createBpmTemplate(Constants.BpmRejectNoticeTemplate, appId);
            if (!sysMsgTemplateService.save(sysMsgTemplate)) {
                return false;
            }
        }
        return true;
    }


    private SysMsgSendTypeDO createBpmChannel(String name, String appId) {
        SysMsgSendTypeDO sysMsgSendType = new SysMsgSendTypeDO();
        sysMsgSendType.setAppId(appId);
        sysMsgSendType.setChannelName(name);
        sysMsgSendType.setSendMode("socketMessengerImpl");
        return sysMsgSendType;
    }

    private SysMsgTemplateDO createBpmTemplate(String templateId, String appId) {
        SysMsgTemplateDO sysMsgTemplate = new SysMsgTemplateDO();
        Map<String, String> map = Constants.BpmTemplateDefaultData.get(templateId);
        sysMsgTemplate.setTemplateId(templateId);
        sysMsgTemplate.setAppId(appId);
        sysMsgTemplate.setTemplateTitle(map.get("title"));
        sysMsgTemplate.setTemplateContent(map.get("content"));
        sysMsgTemplate.setUrl(map.get("url"));
        return sysMsgTemplate;
    }


    @Override
    public List<KeyValueVO> listSendTypes(String appId) {
        List<KeyValueVO> list = new ArrayList<>(16);
        for (Map.Entry<String, IMessenger> entry : messengerMap.entrySet()) {
            KeyValueVO keyValueVO = new KeyValueVO();
            keyValueVO.setKey(entry.getValue().getType());
            keyValueVO.setValue(entry.getValue().getAnnotationValue());
            list.add(keyValueVO);
        }
        SysGroupAppDO groupApp = sysGroupAppService.getById(appId);
        //查询当前集团下所有应用
        if (Objects.isNull(groupApp) || StringUtils.isEmpty(groupApp.getGroupId())) {
            return list;
        }
        List<SysGroupAppDO> apps = sysGroupAppService.list(
                new QueryWrapper<SysGroupAppDO>()
                        .eq("group_id", groupApp.getGroupId()));
        if (CollectionUtils.isEmpty(apps)) {
            return list;
        }
        List<String> appIds = apps.stream().map(SysGroupAppDO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(appIds)) {
            return list;
        }
        List<SysMsgTypeExtendDO> extendTypes =
                sysMsgTypeExtendService.list(
                        new QueryWrapper<SysMsgTypeExtendDO>().in("app_id", appIds));
        extendTypes.stream().forEach(extendType -> {
            KeyValueVO keyValueVO = new KeyValueVO();
            keyValueVO.setKey(extendType.getName());
            keyValueVO.setValue(extendType.getName());
            list.add(keyValueVO);
        });
        return list;
    }

    /**
     * 应用升级时，消息通道更新
     *
     * @param msgSendTypeDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:17
     */
    @Override
    public boolean msgSendTypeUpdate(List<MsgSendTypeDetail> msgSendTypeDetails, String appId) {
        List<SysMsgSendTypeDO> add = new ArrayList<>(16);
        List<SysMsgSendTypeDO> upd = new ArrayList<>(16);

        msgSendTypeDetails.stream().forEach(msgSendTypeDetail -> {
            SysMsgSendTypeDO item = JsonUtils.castObject(msgSendTypeDetail, SysMsgSendTypeDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysMsgSendTypeDO sysMsgSendTypeDO = getOne(new QueryWrapper<SysMsgSendTypeDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysMsgSendTypeDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysMsgSendTypeDO, item)) {
                    item.setId(sysMsgSendTypeDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }
}
