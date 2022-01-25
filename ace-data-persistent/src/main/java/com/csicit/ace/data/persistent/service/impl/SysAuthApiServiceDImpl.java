package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.AuthApiDetail;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysAuthApiMapper;
import com.csicit.ace.data.persistent.service.SysAuthApiServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限api关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysAuthApiServiceD")
public class SysAuthApiServiceDImpl extends BaseServiceImpl<SysAuthApiMapper, SysAuthApiDO> implements
        SysAuthApiServiceD {

    /**
     * 应用升级时，AuthAPI更新
     *
     * @param authApiDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:59
     */
    @Override
    public boolean authApiUpdate(List<AuthApiDetail> authApiDetails, String appId) {
        List<SysAuthApiDO> add = new ArrayList<>(16);
        List<SysAuthApiDO> upd = new ArrayList<>(16);

        authApiDetails.stream().forEach(authApiDetail -> {
            SysAuthApiDO item = JsonUtils.castObject(authApiDetail, SysAuthApiDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysAuthApiDO sysAuthApiDO = getOne(new QueryWrapper<SysAuthApiDO>()
                    .eq("trace_id", item.getTraceId()).inSql("auth_id", "select id from sys_auth where app_id='" +
                            appId + "'"));
            if (sysAuthApiDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysAuthApiDO, item)) {
                    item.setId(sysAuthApiDO.getId());
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
        return false;
    }
}
