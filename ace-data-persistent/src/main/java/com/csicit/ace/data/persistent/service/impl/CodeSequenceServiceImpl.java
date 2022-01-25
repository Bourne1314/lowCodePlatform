package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.CodeSeqDetail;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import com.csicit.ace.common.pojo.domain.CodeTemplateDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.CodeSequenceMapper;
import com.csicit.ace.data.persistent.service.CodeSequenceService;
import com.csicit.ace.data.persistent.service.CodeTemplatePartService;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/5/22 15:19
 */
@Service
public class CodeSequenceServiceImpl extends BaseServiceImpl<CodeSequenceMapper,CodeSequenceDO>
        implements CodeSequenceService {
    @Autowired
    CodeTemplatePartService codeTemplatePartService;
    @Override
    public R saveCodeSequence(CodeSequenceDO instance) {
        if (StringUtils.isEmpty(instance.getPartValueTag())){
            instance.setPartValueTag(Constants.ACE_CODE_NULL_PART_VALUE_TAG);
        }
        CodeSequenceDO codeSequence =
                getCodeSequence(instance.getAppId(), instance.getBizTag(), instance.getPartValueTag());
        if(Objects.nonNull(codeSequence)){
            return R.error("已存在相同定义的数字序列!");
        }
        if (save(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R updateCodeSequence(CodeSequenceDO instance) {
        CodeSequenceDO oldTemplate = getById(instance.getId());
        if(!Objects.equals(oldTemplate.getBizTag()+oldTemplate.getPartValueTag(),
                instance.getBizTag()+instance.getPartValueTag())){
            if(Objects.nonNull(getCodeSequence(instance.getAppId(), instance.getBizTag(), instance.getPartValueTag()))){
                return R.error("已存在相同定义的数字序列!");
            }
        }
        if (updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public CodeSequenceDO getCodeSequence(String appId, String bigTag, String partValueTag) {
        return getOne(new QueryWrapper<CodeSequenceDO>()
                    .eq("app_id", appId)
                    .eq("biz_tag", bigTag)
                    .eq("part_value_tag", partValueTag));
    }

    @Override
    public CodeSequenceDO getRestCodeSequence(CodeSequenceDO sequence) {
        while (!update(new CodeSequenceDO(),new UpdateWrapper<CodeSequenceDO>()
                .eq("app_id", sequence.getAppId())
                .eq("biz_tag",sequence.getBizTag())
                .eq("part_value_tag", sequence.getPartValueTag())
                .eq("max_num",sequence.getMaxNum())
                .set("max_num",0)
                .set("last_reset_time",LocalDateTime.now()))){
            sequence = getById(sequence.getId());
        }
        return sequence;
    }

    @Override
    public CodeSequenceDO getNextMaxNum(String appId, String bigTag, String partValueTag) {
        CodeSequenceDO sequence = null;
        do{
            sequence = getCodeSequence(appId,bigTag,partValueTag);
            if(Objects.isNull(sequence)){
                return null;
            }
        }while (!update(new CodeSequenceDO(), new UpdateWrapper<CodeSequenceDO>()
                .eq("app_id", appId)
                .eq("biz_tag", bigTag)
                .eq("part_value_tag", partValueTag)
                .eq("max_num",sequence.getMaxNum())
                .set("max_num",sequence.getMaxNum()+sequence.getStep())));
        return sequence;
    }

    @Override
    public String getNextNum(String appId, String bigTag, String partValueTag) {
        String key = appId+"|"+bigTag+"|"+partValueTag;
        Object lock = lockMapCache.computeIfAbsent(key, k->new Object());
        String resultCode;
        synchronized (lock){
            CodeSequenceDO codeSequence = sequenceMapCache.get(key);
            boolean inFlag = true;
            boolean outFlag = true;
            do{
                if(inFlag&&Objects.nonNull(codeSequence)){
                    inFlag = false;
                    continue;
                }
                if(Objects.nonNull(codeSequence)){
                    getRestCodeSequence(codeSequence);
                }
                codeSequence = getNextMaxNum(appId, bigTag, partValueTag);
                if (Objects.isNull(codeSequence)){
                    throw new RException("数字序列未定义!");
                }
                sequenceMapCache.putIfAbsent(key,codeSequence);
                codeSequence.setCurrent(codeSequence.getMaxNum()+1);
            }while (!isOutResetTime(codeSequence.getResetMode(), codeSequence.getLastResetTime()));

            while (codeSequence.getCurrent()>codeSequence.getMaxNum()+codeSequence.getStep()){
                boolean firstFlag = true;
                do{
                    if(!firstFlag){
                        getRestCodeSequence(codeSequence);
                    }
                    firstFlag = false;
                    codeSequence = getNextMaxNum(appId, bigTag, partValueTag);
                    if (Objects.isNull(codeSequence)){
                        throw new RException("数字序列未定义!");
                    }
                    sequenceMapCache.putIfAbsent(key,codeSequence);
                    codeSequence.setCurrent(codeSequence.getMaxNum()+1);
                }while (!isOutResetTime(codeSequence.getResetMode(), codeSequence.getLastResetTime()));
            }
            if (codeSequence.getEnableFixNumLen()==1){
                resultCode = String.format("%0"+codeSequence.getNumLength()+"d",codeSequence.getCurrent());
            }else {
                resultCode = codeSequence.getCurrent()+"";
            }
            codeSequence.setCurrent(codeSequence.getCurrent()+1);
        }
        return resultCode;
    }


    /**
     * 应用升级时，数字序列更新
     *
     * @param codeSeqDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 10:18
     */
    @Override
    public boolean codeSeqUpdate(List<CodeSeqDetail> codeSeqDetails, String appId) {
        List<CodeSequenceDO> add = new ArrayList<>(16);
        List<CodeSequenceDO> upd = new ArrayList<>(16);

        codeSeqDetails.stream().forEach(codeSeqDetail -> {
            CodeSequenceDO item = JsonUtils.castObject(codeSeqDetail, CodeSequenceDO.class);
            // 判断当前业务数据库中是否存在该条数据
            CodeSequenceDO codeSequenceDO = getOne(new QueryWrapper<CodeSequenceDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (codeSequenceDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(codeSequenceDO, item)) {
                    item.setId(codeSequenceDO.getId());
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


    /**
     * 是否需要到重置 true不需要 false 需要重置
     * @param resetMode
     * @param lastResetTime
     * @return
     */
    private boolean isOutResetTime(int resetMode,LocalDateTime lastResetTime){
        switch (resetMode){
            case 1:
                return true;
            case 2:
                return lastResetTime.getYear()==LocalDateTime.now().getYear();
            case 3:
                return lastResetTime.getYear()==LocalDateTime.now().getYear()
                        &&lastResetTime.getMonth()==LocalDateTime.now().getMonth();
            case 4:
                return lastResetTime.getYear()==LocalDateTime.now().getYear()
                        &&lastResetTime.getMonth()==LocalDateTime.now().getMonth()
                        &&lastResetTime.getDayOfMonth()==LocalDateTime.now().getDayOfMonth();
            default:
                return true;
        }
    }

}
