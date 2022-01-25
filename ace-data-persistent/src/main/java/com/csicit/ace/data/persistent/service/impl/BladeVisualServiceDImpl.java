package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.AppUpgradeJaxb.BladeVisualMsgDetail;
import com.csicit.ace.common.AppUpgradeJaxb.BladeVisualShowDetail;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.BladeVisualMapper;
import com.csicit.ace.data.persistent.service.BladeVisualMsgServiceD;
import com.csicit.ace.data.persistent.service.BladeVisualServiceD;
import com.csicit.ace.data.persistent.service.BladeVisualShowServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 大屏信息数据表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */
@Service("bladeVisualServiceD")
public class BladeVisualServiceDImpl extends BaseServiceImpl<BladeVisualMapper, BladeVisualDO> implements
        BladeVisualServiceD {

    @Autowired
    BladeVisualMsgServiceD bladeVisualMsgServiceD;

    @Autowired
    BladeVisualShowServiceD bladeVisualShowServiceD;

    /**
     * 应用升级时，大屏更新
     *
     * @param appUpgrade
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 8:25
     */
    @Override
    public boolean bladeVisualUpdate(AppUpgrade appUpgrade) {
        // 大屏
        List<BladeVisualDO> add = new ArrayList<>(16);
        List<BladeVisualDO> upd = new ArrayList<>(16);

        appUpgrade.getBladeVisual().stream().forEach(bladeVisualDetail -> {
            BladeVisualDO item = JsonUtils.castObject(bladeVisualDetail, BladeVisualDO.class);
            // 判断当前业务数据库中是否存在该条数据
            BladeVisualDO bladeVisualDO = getOne(new QueryWrapper<BladeVisualDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appUpgrade.getAppId()));
            if (bladeVisualDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(bladeVisualDO, item)) {
                    item.setId(bladeVisualDO.getId());
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

        // 大屏展示
        if (CollectionUtils.isNotEmpty(appUpgrade.getBladeVisualShow())) {
            if (!bladeVisualShowUpdate(appUpgrade.getBladeVisualShow(), appUpgrade.getAppId())) {
                return false;
            }
        }

        // 大屏通知
        if (CollectionUtils.isNotEmpty(appUpgrade.getBladeVisualMsg())) {
            if (!bladeVisualMsgUpdate(appUpgrade.getBladeVisualMsg(), appUpgrade.getAppId())) {
                return false;
            }
        }
        return true;
    }

    private boolean bladeVisualMsgUpdate(List<BladeVisualMsgDetail> bladeVisualMsgDetails, String appId) {
        List<BladeVisualMsgDO> add = new ArrayList<>(16);
        List<BladeVisualMsgDO> upd = new ArrayList<>(16);

        bladeVisualMsgDetails.stream().forEach(bladeVisualMsgDetail -> {
            BladeVisualMsgDO item = JsonUtils.castObject(bladeVisualMsgDetail, BladeVisualMsgDO.class);
            // 判断当前业务数据库中是否存在该条数据
            BladeVisualMsgDO bladeVisualMsgDO = bladeVisualMsgServiceD.getOne(new QueryWrapper<BladeVisualMsgDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (bladeVisualMsgDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(bladeVisualMsgDO, item)) {
                    item.setId(bladeVisualMsgDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!bladeVisualMsgServiceD.saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!bladeVisualMsgServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

    private boolean bladeVisualShowUpdate(List<BladeVisualShowDetail> bladeVisualShowDetails, String appId) {

        List<BladeVisualShowDO> add = new ArrayList<>(16);
        List<BladeVisualShowDO> upd = new ArrayList<>(16);

        bladeVisualShowDetails.stream().forEach(bladeVisualShowDetail -> {
            BladeVisualShowDO item = JsonUtils.castObject(bladeVisualShowDetail, BladeVisualShowDO.class);
            // 判断当前业务数据库中是否存在该条数据
            BladeVisualShowDO bladeVisualShowDO1 = bladeVisualShowServiceD.getOne(new QueryWrapper<BladeVisualShowDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (bladeVisualShowDO1 == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(bladeVisualShowDO1, item)) {
                    item.setId(bladeVisualShowDO1.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!bladeVisualShowServiceD.saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!bladeVisualShowServiceD.updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }
}
