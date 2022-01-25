package com.csicit.ace.data.persistent.service.impl;

import com.aspose.slides.internal.mx.add;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import com.csicit.ace.common.pojo.domain.CodeTemplateDO;
import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.CodeTemplateMapper;
import com.csicit.ace.data.persistent.service.CodeSequenceService;
import com.csicit.ace.data.persistent.service.CodeTemplatePartService;
import com.csicit.ace.data.persistent.service.CodeTemplateService;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/5/22 15:19
 */
@Service
public class CodeTemplateServiceImpl extends BaseServiceImpl<CodeTemplateMapper, CodeTemplateDO>
        implements CodeTemplateService {

    @Autowired
    CodeTemplatePartService codeTemplatePartService;
    @Autowired
    CodeSequenceService codeSequenceService;

    @Override
    public R saveTemplate(CodeTemplateDO instance) {
        int count = count(
                new QueryWrapper<CodeTemplateDO>().eq("app_id", instance.getAppId())
                        .eq("template_key", instance.getTemplateKey()));
        if (count > 0) {
            return R.error("当前模板标识已存在");
        }
        if (Objects.isNull(getById(instance.getId()))) {
            if (save(instance)) {
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        if (updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R updateTemplate(CodeTemplateDO instance) {
        return null;
    }

    @Override
    public String getTemplateCode(Map<String, String> map) {
        CodeTemplateDO codeTemplate = getOne(new QueryWrapper<CodeTemplateDO>()
                .eq("app_id", map.get(Constants.ACE_CODE_PARAM_KEY_APPID))
                .eq("template_key", map.get(Constants.PACE_CODE_ARAM_KEY_TEMPLATEKEY)));
        if (Objects.isNull(codeTemplate)) {
            return null;
        }
        List<CodeTemplatePartDO> parts =
                codeTemplatePartService.list(new QueryWrapper<CodeTemplatePartDO>()
                        .eq("template_id", codeTemplate.getId()).orderByAsc("sort_index"));
        String result = null;
        for (CodeTemplatePartDO part : parts) {
            result += getPartStr(part, map, codeTemplate.getAppId());
        }
        return result;
    }


    /**
     * 应用升级时， 编码模板更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 9:40
     */
    @Override
    public boolean codeTemplateUpdate(AppUpgrade appUpgrade) {
        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeTemplate())) {
            List<CodeTemplateDO> add = new ArrayList<>(16);
            List<CodeTemplateDO> upd = new ArrayList<>(16);

            appUpgrade.getCodeTemplate().stream().forEach(codeTemplateDetail -> {
                CodeTemplateDO item = JsonUtils.castObject(codeTemplateDetail, CodeTemplateDO.class);
                // 判断当前业务数据库中是否存在该条数据
                CodeTemplateDO codeTemplateDO = getOne(new QueryWrapper<CodeTemplateDO>()
                        .eq("trace_id", item.getTraceId()).eq("app_id", appUpgrade.getAppId()));
                if (codeTemplateDO == null) {
                    add.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(codeTemplateDO, item)) {
                        item.setId(codeTemplateDO.getId());
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
        }

        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeTemplatePart())) {
            List<CodeTemplatePartDO> partAdd = new ArrayList<>(16);
            List<CodeTemplatePartDO> partUpd = new ArrayList<>(16);

            appUpgrade.getCodeTemplatePart().stream().forEach(codeTemplatePartDetail -> {
                CodeTemplatePartDO item = JsonUtils.castObject(codeTemplatePartDetail, CodeTemplatePartDO.class);
                // 判断当前业务数据库中是否存在该条数据
                CodeTemplatePartDO codeTemplatePartDO = codeTemplatePartService.getOne(new
                        QueryWrapper<CodeTemplatePartDO>()
                        .eq("trace_id", item.getTraceId()).eq("TEMPLATE_ID", "select id from CODE_TEMPLATE where " +
                                "app_id='" + appUpgrade.getAppId() + "'"));
                if (codeTemplatePartDO == null) {
                    partAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(codeTemplatePartDO, item)) {
                        item.setId(codeTemplatePartDO.getId());
                        partUpd.add(item);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(partAdd)) {
                if (!codeTemplatePartService.saveBatch(partAdd)) {
                    return false;
                }

            }
            if (CollectionUtils.isNotEmpty(partUpd)) {
                if (!codeTemplatePartService.updateBatchById(partUpd)) {
                    return false;
                }
            }
        }

        return true;
    }

    private String getPartStr(CodeTemplatePartDO part, Map<String, String> map, String appId) {
        switch (part.getCodeType()) {
            case 0:
                return part.getStaticText();
            case 1:
                ExpressionParser parser = new SpelExpressionParser();
                EvaluationContext context = new StandardEvaluationContext();
                return parser.parseExpression
                        (part.getElExpress()).getValue(context, String.class);
            case 2:
                return map.get(part.getParamKey()) == null ? "" : map.get(part.getParamKey());
            case 3:
                String partValue = Constants.ACE_CODE_NULL_PART_VALUE_TAG;
                if (StringUtils.isNotEmpty(part.getDynPart())) {
                    partValue = "";
                    String[] partIds = part.getDynPart().split(",");
                    for (String partId : partIds) {
                        CodeTemplatePartDO dynPart = codeTemplatePartService.getById(partId);
                        if (dynPart != null) {
                            partValue = partValue + getPartStr(dynPart, map, appId);
                        }
                    }
                }
                CodeSequenceDO codeSequence = codeSequenceService.getOne(new QueryWrapper<CodeSequenceDO>()
                        .eq("app_id", appId).eq("biz_tag", part.getSequenceBizTag())
                        .eq("part_value_tag", partValue));
                if (Objects.isNull(codeSequence)) {
                    if (part.getAutoCreateSeqDef() == 1) {
                        CodeSequenceDO cs = new CodeSequenceDO();
                        cs.setBizTag(part.getSequenceBizTag());
                        cs.setAppId(part.getAppId());
                        cs.setStep(part.getSeqStep());
                        cs.setResetMode(part.getSeqResetMode());
                        cs.setLastResetTime(LocalDateTime.now());
                        cs.setEnableFixNumLen(part.getEnableFixNumLen());
                        cs.setNumLength(part.getSeqNumLength());
                        cs.setPartValueTag(partValue);
                        codeSequenceService.save(cs);
                    } else {
                        return "";
                    }
                }
                return codeSequenceService.getNextNum(appId, part.getSequenceBizTag(), partValue);
            default:
                return "";
        }
    }


}
