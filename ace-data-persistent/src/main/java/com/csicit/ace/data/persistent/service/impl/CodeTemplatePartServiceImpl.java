package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import com.csicit.ace.common.pojo.domain.CodeTemplateDO;
import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.CodeTemplatePartMapper;
import com.csicit.ace.data.persistent.service.CodeSequenceService;
import com.csicit.ace.data.persistent.service.CodeTemplatePartService;
import com.csicit.ace.data.persistent.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/5/22 15:19
 */
@Service
public class CodeTemplatePartServiceImpl extends BaseServiceImpl<CodeTemplatePartMapper,CodeTemplatePartDO>
        implements CodeTemplatePartService{

    @Autowired
    CodeTemplateService codeTemplateService;
    @Autowired
    CodeSequenceService codeSequenceService;
    @Override
    public R saveTemplatePart(CodeTemplatePartDO part) {
        CodeTemplateDO template = codeTemplateService.getById(part.getTemplateId());
        //如果是数字序列部件且自动创建数字序列
        if(Objects.isNull(template)){
            template = new CodeTemplateDO();
            template.setId(part.getTemplateId());
            if(codeTemplateService.save(template)&&save(part)){
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        // 判断模板部件名称是否已存在
        int count = count(new QueryWrapper<CodeTemplatePartDO>().eq("template_id",part.getTemplateId())
                                .eq("sort_index", part.getSortIndex()));
        if (count > 0) {
            return R.error("当前模板部件排序已存在!");
        }
        if (save(part)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R updateTemplatePart(CodeTemplatePartDO instance) {
        CodeTemplatePartDO oldTemplate = getById(instance.getId());
        if(!Objects.equals(oldTemplate.getSortIndex(),instance.getSortIndex())){
            int count = count(
                            new QueryWrapper<CodeTemplatePartDO>().eq("template_id",instance.getTemplateId())
                                    .eq("sort_index", instance.getSortIndex()));
            if (count > 0) {
                return R.error("当前模板排序已存在!");
            }
        }
        if (updateById(instance)){
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    private R createCodeSequence(CodeTemplatePartDO part){
        String partTagValue = Constants.ACE_CODE_NULL_PART_VALUE_TAG;
        if(StringUtils.isNotEmpty(part.getDynPart())){
            String[] parts = part.getDynPart().split(",");
            for (String partId:parts){

            }
        }
        CodeSequenceDO codeSequence = new CodeSequenceDO();
        codeSequence.setBizTag(part.getSequenceBizTag());
        codeSequence.setAppId(part.getAppId());
        codeSequence.setStep(part.getSeqStep());
        codeSequence.setResetMode(part.getSeqResetMode());
        codeSequence.setLastResetTime(LocalDateTime.now());
        codeSequence.setEnableFixNumLen(part.getEnableFixNumLen());
        codeSequence.setNumLength(part.getSeqNumLength());
        codeSequence.setPartValueTag(partTagValue);
        R r = codeSequenceService.saveCodeSequence(codeSequence);
        return r;
    }
}
