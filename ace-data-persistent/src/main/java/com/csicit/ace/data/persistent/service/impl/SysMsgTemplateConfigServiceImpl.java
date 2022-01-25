package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateConfigDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysMsgTemplateConfigMapper;
import com.csicit.ace.data.persistent.service.SysMsgTemplateConfigService;
import com.csicit.ace.data.persistent.service.SysMsgTemplateService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/4/7 10:09
 */
@Service
public class SysMsgTemplateConfigServiceImpl extends BaseServiceImpl<SysMsgTemplateConfigMapper, SysMsgTemplateConfigDO>
        implements SysMsgTemplateConfigService {
    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    @Override
    public R saveTemplateConfig(SysMsgTemplateConfigDO instance) {
        SysMsgTemplateDO sysMsgTemplate = sysMsgTemplateService.getById(instance.getTid());
        if (Objects.isNull(sysMsgTemplate)) {
            sysMsgTemplate = new SysMsgTemplateDO();
            sysMsgTemplate.setId(instance.getTid());
            if (sysMsgTemplateService.save(sysMsgTemplate)) {
                if (save(instance)) {
                    return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
                }
            }
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        if (count(new QueryWrapper<SysMsgTemplateConfigDO>().eq("template_id", instance.getTemplateId())) > 0) {
            return R.error("当前消息信使模板id已存在!");
        }
        if (count(new QueryWrapper<SysMsgTemplateConfigDO>()
                .eq("type", instance.getType()).eq("tid", instance.getTid())) > 0) {
            return R.error("当前平台消息模板下已存在相同信使类别的信使配置项!");
        }

        if (save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R updateTemplateConfig(SysMsgTemplateConfigDO instance) {
        SysMsgTemplateConfigDO oldConfig = getById(instance.getId());
        if (!Objects.equals(oldConfig.getTemplateId(), instance.getTemplateId()) &&
                count(new QueryWrapper<SysMsgTemplateConfigDO>()
                        .eq("template_id", instance.getTemplateId())) > 0) {
            return R.error("当前消息信使模板id已存在!");
        }
        if (!Objects.equals(oldConfig.getType(), instance.getType()) &&
                count(new QueryWrapper<SysMsgTemplateConfigDO>()
                        .eq("type", instance.getType()).eq("tid", instance.getTid())) > 0) {
            return R.error("当前平台消息模板下已存在相同信使类别的信使配置项!");
        }
        if (updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

}
