package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AuthDetail;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthMapper;
import com.csicit.ace.data.persistent.service.SysAuthServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthServiceD")
public class SysAuthServiceDImpl extends BaseServiceImpl<SysAuthMapper, SysAuthDO> implements SysAuthServiceD {

    /**
     * 应用升级，权限更新
     *
     * @param authDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:21
     */
    @Override
    public boolean authUpdate(List<AuthDetail> authDetails, String appId) {
        List<SysAuthDO> add = new ArrayList<>(16);
        List<SysAuthDO> upd = new ArrayList<>(16);
        authDetails.stream().forEach(authDetail -> {
            SysAuthDO item = JsonUtils.castObject(authDetail, SysAuthDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysAuthDO sysAuthDO = getOne(new QueryWrapper<SysAuthDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysAuthDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysAuthDO, item)) {
                    item.setId(sysAuthDO.getId());
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
