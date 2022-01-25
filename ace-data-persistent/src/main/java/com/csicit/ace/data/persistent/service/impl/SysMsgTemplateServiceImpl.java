package com.csicit.ace.data.persistent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.MsgTemplateConfigDetail;
import com.csicit.ace.common.AppUpgradeJaxb.MsgTemplateDetail;
import com.csicit.ace.common.pojo.domain.SysMicroAppDO;
import com.csicit.ace.common.pojo.domain.SysMsgSendTypeDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateConfigDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysMsgTemplateMapper;
import com.csicit.ace.data.persistent.service.SysMicroAppService;
import com.csicit.ace.data.persistent.service.SysMsgTemplateConfigService;
import com.csicit.ace.data.persistent.service.SysMsgTemplateService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/4/7 10:10
 */
@Service
public class SysMsgTemplateServiceImpl extends BaseServiceImpl<SysMsgTemplateMapper, SysMsgTemplateDO>
        implements SysMsgTemplateService {
    @Autowired
    HttpClient client;
    @Autowired
    SysMicroAppService sysMicroAppService;
    @Autowired
    SysMsgTemplateConfigService sysMsgTemplateConfigService;

    @Override
    public List<SysMsgTemplateDO> getMicroAppTemplateList() {
        List<SysMicroAppDO> sysMicroApps = sysMicroAppService.list(null);
        List<SysMsgTemplateDO> list = new ArrayList<>(16);
        if (sysMicroApps.size() > 0) {
            sysMicroApps.forEach(app -> {
                if (StringUtils.isNotEmpty(app.getAppId()) && StringUtils.isNotEmpty(app.getAppSecret())) {
                    List<SysMsgTemplateDO> appTemplates = getAppTemplates(app);
                    list.addAll(appTemplates);
                }
            });
        }
        return list;
    }

    @Override
    public R importSelectedTemplates(Map<String, Object> map) {
        if (map.get("templates") == null) {
            return R.ok();
        }
        String templates = map.get("templates").toString();
        List<SysMsgTemplateDO> sysMsgTemplates = JSON.parseArray(templates, SysMsgTemplateDO.class);
        if (sysMsgTemplates.size() == 0) {
            return R.ok();
        }
        savePlatTemplates(sysMsgTemplates, null);
        return R.ok();
    }

    @Override
    public R importAllTemplates(Map<String, Object> map) {
        if (map.get("appId") == null) {
            return R.ok();
        }
        List<SysMsgTemplateDO> sysMsgTemplates = getMicroAppTemplateList();
        if (sysMsgTemplates.size() == 0) {
            return R.ok();
        }
        String appId = map.get("appId").toString();
        savePlatTemplates(sysMsgTemplates, appId);
        return R.ok();
    }

    @Override
    public boolean saveMsgTemplate(SysMsgTemplateDO instance) {
        if (Objects.isNull(getById(instance.getId()))) {
            instance.setCreateTime(LocalDateTime.now());
            return save(instance);
        }
        return updateById(instance);
    }

    /**
     * 应用升级时，消息模板更新
     *
     * @param msgTemplateDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:27
     */
    @Override
    public boolean msgTemplateUpdate(List<MsgTemplateDetail> msgTemplateDetails, List<MsgTemplateConfigDetail>
            msgTemplateConfigDetails, String appId) {
        List<SysMsgTemplateDO> add = new ArrayList<>(16);
        List<SysMsgTemplateDO> upd = new ArrayList<>(16);

        msgTemplateDetails.stream().forEach(msgSendTypeDetail -> {
            SysMsgTemplateDO item = JsonUtils.castObject(msgSendTypeDetail, SysMsgTemplateDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysMsgTemplateDO sysMsgTemplateDO = getOne(new QueryWrapper<SysMsgTemplateDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysMsgTemplateDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysMsgTemplateDO, item)) {
                    item.setId(sysMsgTemplateDO.getId());
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

        if (CollectionUtils.isNotEmpty(msgTemplateConfigDetails)) {
            List<SysMsgTemplateConfigDO> configAdd = new ArrayList<>(16);
            List<SysMsgTemplateConfigDO> configUpd = new ArrayList<>(16);
            msgTemplateConfigDetails.stream().forEach(msgTemplateConfigDetail -> {
                SysMsgTemplateConfigDO item = JsonUtils.castObject(msgTemplateConfigDetail, SysMsgTemplateConfigDO
                        .class);
                // 判断当前业务数据库中是否存在该条数据
                SysMsgTemplateConfigDO sysMsgTemplateConfigDO = sysMsgTemplateConfigService.getOne(new
                        QueryWrapper<SysMsgTemplateConfigDO>().eq("trace_id", item.getTraceId())
                        .inSql("tid", "select id from sys_msg_template where app_id='" + appId + "'"));
                if (sysMsgTemplateConfigDO == null) {
                    configAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(sysMsgTemplateConfigDO, item)) {
                        item.setId(sysMsgTemplateConfigDO.getId());
                        configUpd.add(item);
                    }
                }
            });


            if (CollectionUtils.isNotEmpty(configAdd)) {
                if (!sysMsgTemplateConfigService.saveBatch(configAdd)) {
                    return false;
                }

            }
            if (CollectionUtils.isNotEmpty(configUpd)) {
                if (!sysMsgTemplateConfigService.updateBatchById(configUpd)) {
                    return false;
                }
            }
        }

        return true;
    }


    private void savePlatTemplates(List<SysMsgTemplateDO> sysMsgTemplates, String appId) {
        sysMsgTemplates.forEach(template -> {
            //判断当前模板有没有导入过
            if (count(new QueryWrapper<SysMsgTemplateDO>().eq("template_id", template.getTemplateId())) == 0) {
                template.setCreateTime(LocalDateTime.now());
                String microAppId = template.getAuth();
                String microAppName = template.getAuthName();
                if (StringUtils.isNotEmpty(appId)) {
                    template.setAppId(appId);
                }
                template.setAuthName("");
                template.setAuth("");
                if (save(template)) {
                    SysMsgTemplateConfigDO templateConfig = new SysMsgTemplateConfigDO();
                    templateConfig.setOpen(1);
                    templateConfig.setTid(template.getId());
                    templateConfig.setTemplateId(template.getTemplateId());
                    templateConfig.setTitle(template.getTemplateTitle());
                    templateConfig.setContent(template.getTemplateContent());
                    templateConfig.setType(template.getType());
                    templateConfig.setMicroAppId(microAppId);
                    templateConfig.setMicroAppName(microAppName);
                    sysMsgTemplateConfigService.save(templateConfig);
                }
            }
        });
    }

    /**
     * 获取小程序请求凭证
     *
     * @param appId  应用id
     * @param secret 应用密钥
     * @return
     */
    private String getAppAccessToken(String appId, String secret) {
        String url =
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" +
                        secret;
        String requst = this.client.client(url);
        JSONObject jsonObject = JSONObject.parseObject(requst);
        return jsonObject.getString("access_token");
    }

    /**
     * 获取当前小程序所有订阅消息模板
     *
     * @param app 小程序对象
     * @return
     */
    private List<SysMsgTemplateDO> getAppTemplates(SysMicroAppDO app) {
        List<SysMsgTemplateDO> list = new ArrayList<>(16);
        String url =
                "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?access_token=" +
                        getAppAccessToken(app.getAppId(), app.getAppSecret());
        String requst = this.client.client(url);
        JSONObject jsonObject = JSONObject.parseObject(requst);
        if (jsonObject.getInteger("errcode") == 0) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject template = data.getJSONObject(i);
                SysMsgTemplateDO sysMsgTemplate = new SysMsgTemplateDO();
                sysMsgTemplate.setAuthName(app.getName());
                sysMsgTemplate.setAuth(app.getAppId());
                sysMsgTemplate.setType(app.getType());
                sysMsgTemplate.setTemplateId(template.getString("priTmplId"));
                sysMsgTemplate.setTemplateTitle(template.getString("title"));
                sysMsgTemplate.setTemplateContent(template.getString("content"));
                list.add(sysMsgTemplate);
            }
        }
        return list;
    }
}
