package com.csicit.ace.data.persistent.messenger.impl;


import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateConfigDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.messenger.AbstractMessengerImpl;
import com.csicit.ace.data.persistent.messenger.IMessenger;
import com.csicit.ace.data.persistent.service.SysUserThirdPartyService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/** 
 * 微信小程序信使实现
 *
 * @author shanwj
 * @date 2020/4/13 18:37
 */
@Service
public class WxMicroAppMessengerImpl extends AbstractMessengerImpl implements IMessenger {

    private static final String ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";

    private static final String SEND_URL =
            "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    @Autowired
    SysUserThirdPartyService sysUserThirdPartyService;


    @Override
    public String getAnnotationValue() {
        return getAnnotationValue(this.getClass());
    }

    @Override
    public String getType() {
        return "微信小程序";
    }

    @Override
    public void sendMsg(SysMessageDO sysMessageDO) {
        Map<String, Object> data = sysMessageDO.getData();
        if(Objects.isNull(data)){
            return;
        }
        Map<String, Object> wxData = new HashMap<>(16);
        for (Map.Entry<String, Object> entry:data.entrySet()){
            wxData.put(entry.getKey(),new HashMap<String, Object>(16).put("value",entry.getValue()));
        }
        SysMsgTemplateDO sysMsgTemplate = sysMessageDO.getSysMsgTemplate();
        if(CollectionUtils.isNotEmpty(sysMsgTemplate.getTemplateConfigs())
                &&sysMsgTemplate.getTemplateConfigs().size()>0){
            List<SysMsgTemplateConfigDO> templateConfigs = sysMsgTemplate.getTemplateConfigs();
            templateConfigs.forEach(config->{
                //如果是微信消息模板并且模板配置是启用状态
                if(Objects.equals(config.getType(),Constants.WxMicroAppMessenger)&&Objects.equals(config.getOpen(),1)){
                    if(StringUtils.isNotEmpty(config.getMicroAppId())&&StringUtils.isNotEmpty(config.getMicroAppSecret())){
                        String[] userIds = sysMessageDO.getReceiveUsers().split(",");
                        List<String> openIds = sysUserThirdPartyService.getThirdAccounts(Arrays.asList(userIds),Constants.WxMicroAppMessenger);
                        String tokenStr = httpClient.client(ACCESS_TOKEN_URL
                                + config.getMicroAppId() + "&secret=" + config.getMicroAppSecret());
                        if (StringUtils.isNotEmpty(tokenStr)&&tokenStr.contains("access_token")){
                            JSONObject jsonObject = JSONObject.parseObject(tokenStr);
                            String token = jsonObject.getString("access_token");
                            if(CollectionUtils.isNotEmpty(openIds)){
                                openIds.forEach(openId->{
                                    JSONObject sendObj = new JSONObject(16);
                                    sendObj.put("touser",openId);
                                    sendObj.put("template_id",config.getTemplateId());
                                    sendObj.put("page","index");
                                    sendObj.put("data",wxData);
                                    httpClient.postReturnString(SEND_URL+token,sendObj);
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}
