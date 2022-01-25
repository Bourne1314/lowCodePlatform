package com.csicit.ace.data.persistent.messenger;

import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 消息信使
 *
 * @author shanwj
 * @date 2020/4/13 18:33
 */
public abstract class AbstractMessengerImpl {

    @Autowired
    protected HttpClient httpClient;

    protected String getAnnotationValue(Class cl){
        Service service = (Service)cl.getAnnotation(Service.class);
        String name = service.value();
        if(StringUtils.isEmpty(name)){
            name = org.apache.commons.lang.StringUtils.uncapitalize(this.getClass().getSimpleName());
        }
        return name;
    }

    /**
     * 将data中的数据替换消息模板中的参数
     * @param sysMessageDO
     * @return
     */
    public static SysMessageDO replaceTempplateByData(SysMessageDO sysMessageDO,String type){
        SysMsgTemplateDO sysMsgTemplate = sysMessageDO.getSysMsgTemplate();
        SysMessageDO result = new SysMessageDO();
        result.setReceiveUsers(sysMessageDO.getReceiveUsers());
        result.setTitle(sysMsgTemplate.getTemplateTitle());
        result.setContent(sysMsgTemplate.getTemplateContent());
        result.setUrl(sysMsgTemplate.getUrl());
        result.setAppId(sysMessageDO.getAppId());
        result.setId(sysMessageDO.getId());
        sysMsgTemplate.getTemplateConfigs().forEach(config->{
            if(Objects.equals(config.getType(),type)
                    &&Objects.equals(1,config.getOpen())){
                result.setTitle(config.getTitle());
                result.setContent(config.getContent());
                result.setUrl(config.getUrl());
                SysMsgTemplateDO template = sysMessageDO.getSysMsgTemplate();
                template.setTemplateConfig(config);
                result.setSysMsgTemplate(template);
                result.setData(sysMessageDO.getData());
            }
        });
        if (Objects.isNull(sysMessageDO.getData())){
            return result;
        }
        for(Map.Entry<String,Object> map:sysMessageDO.getData().entrySet()){
            result.setUrl(StringUtils.isNotEmpty(result.getUrl())?
                    result.getUrl().replace(
                            "${"+map.getKey()+"}",(map.getValue()!=null?map.getValue().toString():"")):"");
            result.setTitle(StringUtils.isNotEmpty(result.getTitle())?
                    result.getTitle().replace(
                            "${"+map.getKey()+"}",(map.getValue()!=null?map.getValue().toString():"")):"");
            result.setContent(StringUtils.isNotEmpty(result.getContent())?
                    result.getContent().replace(
                            "${"+map.getKey()+"}",(map.getValue()!=null?map.getValue().toString():"")):"");

        }
        return result;

    }
}
