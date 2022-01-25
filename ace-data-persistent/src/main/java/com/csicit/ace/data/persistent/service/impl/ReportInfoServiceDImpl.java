package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.ReportInfoDetail;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.ReportInfoMapper;
import com.csicit.ace.data.persistent.service.ReportInfoServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 报表信息 实例对象访问接口实现
 *
 * @author generator
 * @date 2019-08-07 08:54:46
 * @version V1.0
 */
@Service("reportInfoServiceD")
public class ReportInfoServiceDImpl extends BaseServiceImpl<ReportInfoMapper, ReportInfoDO>
        implements ReportInfoServiceD {

    /**
     * 应用升级时，报表/仪表盘信息更新
     *
     * @param reportInfoDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 16:47
     */
    @Override
    public boolean reportInfoUpdate(List<ReportInfoDetail> reportInfoDetails, String appId) {
        List<ReportInfoDO> add = new ArrayList<>(16);
        List<ReportInfoDO> upd = new ArrayList<>(16);

        reportInfoDetails.stream().forEach(reportInfoDetail -> {
            ReportInfoDO item = JsonUtils.castObject(reportInfoDetail, ReportInfoDO.class);
            // 判断当前业务数据库中是否存在该条数据
            ReportInfoDO reportInfoDO = getOne(new QueryWrapper<ReportInfoDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (reportInfoDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(reportInfoDO, item)) {
                    item.setId(reportInfoDO.getId());
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
