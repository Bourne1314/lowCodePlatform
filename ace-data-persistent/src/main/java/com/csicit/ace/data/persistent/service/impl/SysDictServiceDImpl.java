package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.DictDetail;
import com.csicit.ace.common.AppUpgradeJaxb.DictValueDetail;
import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysDictMapper;
import com.csicit.ace.data.persistent.service.SysDictServiceD;
import com.csicit.ace.data.persistent.service.SysDictValueServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 字典类型 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:58
 */
@Service("sysDictServiceD")
public class SysDictServiceDImpl extends BaseServiceImpl<SysDictMapper, SysDictDO> implements SysDictServiceD {
    @Autowired
    SysDictValueServiceD sysDictValueServiceD;

    /**
     * 应用升级时，字典更新
     *
     * @param dictDetails
     * @param dictValueDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:49
     */
    @Override
    public boolean dictUpdate(List<DictDetail> dictDetails, List<DictValueDetail> dictValueDetails, String appId) {
        List<SysDictDO> add = new ArrayList<>(16);
        List<SysDictDO> upd = new ArrayList<>(16);

        dictDetails.stream().forEach(dictDetail -> {
            SysDictDO item = JsonUtils.castObject(dictDetail, SysDictDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysDictDO sysDictDO = getOne(new QueryWrapper<SysDictDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysDictDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysDictDO, item)) {
                    item.setId(sysDictDO.getId());
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

        if (CollectionUtils.isNotEmpty(dictValueDetails)) {
            List<SysDictValueDO> valueAdd = new ArrayList<>(16);
            List<SysDictValueDO> valueUpd = new ArrayList<>(16);
            dictValueDetails.stream().forEach(dictValueDetail -> {
                SysDictValueDO item = JsonUtils.castObject(dictValueDetail, SysDictValueDO.class);
                // 判断当前业务数据库中是否存在该条数据
                SysDictValueDO sysDictValueDO = sysDictValueServiceD.getOne(new QueryWrapper<SysDictValueDO>()
                        .eq("trace_id", item.getTraceId()).eq("app_id", appId));
                if (sysDictValueDO == null) {
                    valueAdd.add(item);
                } else {
                    // 判断业务数据库数据与应用更新配置文件数据是否一致
                    if (CommonUtils.compareFields(sysDictValueDO, item)) {
                        item.setId(sysDictValueDO.getId());
                        valueUpd.add(item);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(valueAdd)) {
                if (!sysDictValueServiceD.saveBatch(valueAdd)) {
                    return false;
                }
            }
            if (CollectionUtils.isNotEmpty(valueUpd)) {
                if (!sysDictValueServiceD.updateBatchById(valueUpd)) {
                    return false;
                }
            }
        }

        return true;
    }
}
