package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.ReportTypeDetail;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.ReportTypeMapper;
import com.csicit.ace.data.persistent.service.ReportTypeServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 报表类别 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-07 08:52:49
 */
@Service("reportTypeServiceD")
public class ReportTypeServiceDImpl extends BaseServiceImpl<ReportTypeMapper, ReportTypeDO> implements
        ReportTypeServiceD {
    /**
     * 应用升级时，业务类型更新
     *
     * @param reportTypeDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 16:15
     */
    @Override
    public boolean reportTypeUpdate(List<ReportTypeDetail> reportTypeDetails, String appId) {
        List<ReportTypeDO> add = new ArrayList<>(16);
        List<ReportTypeDO> upd = new ArrayList<>(16);

        reportTypeDetails.stream().forEach(reportTypeDetail -> {
            ReportTypeDO item = JsonUtils.castObject(reportTypeDetail, ReportTypeDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ReportTypeDO reportTypeDO = getOne(new QueryWrapper<ReportTypeDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (reportTypeDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(reportTypeDO, item)) {
                    item.setId(reportTypeDO.getId());
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
