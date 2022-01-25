package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.QrtzConfigDetail;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.QrtzConfigMapper;
import com.csicit.ace.data.persistent.service.QrtzConfigServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 批处理任务配置 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@Service("qrtzConfigServiceD")
public class QrtzConfigServiceDImpl extends BaseServiceImpl<QrtzConfigMapper, QrtzConfigDO> implements
        QrtzConfigServiceD {
    /**
     * 应用升级时，定时任务更新
     *
     * @param qrtzConfigDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:10
     */
    @Override
    public boolean qrtzCongfigUpdate(List<QrtzConfigDetail> qrtzConfigDetails, String appId) {
        List<QrtzConfigDO> add = new ArrayList<>(16);
        List<QrtzConfigDO> upd = new ArrayList<>(16);

        qrtzConfigDetails.stream().forEach(qrtzConfigDetail -> {
            QrtzConfigDO item = JsonUtils.castObject(qrtzConfigDetail, QrtzConfigDO.class);
            // 判断当前业务数据库中是否存在该条数据
            QrtzConfigDO qrtzConfigDO = getOne(new QueryWrapper<QrtzConfigDO>()
                    .eq("trace_id", item.getTraceId()).eq("id", appId));
            if (qrtzConfigDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(qrtzConfigDO, item)) {
                    item.setId(qrtzConfigDO.getId());
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
