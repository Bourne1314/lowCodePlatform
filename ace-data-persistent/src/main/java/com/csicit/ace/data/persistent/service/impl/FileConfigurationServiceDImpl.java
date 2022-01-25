package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.FileConfigDetail;
import com.csicit.ace.common.pojo.domain.FileConfigurationDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.FileConfigurationMapper;
import com.csicit.ace.data.persistent.service.FileConfigurationServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Service("fileConfigurationServiceD")
public class FileConfigurationServiceDImpl extends BaseServiceImpl<FileConfigurationMapper, FileConfigurationDO>
        implements FileConfigurationServiceD {
    /**
     * 应用升级时，附件配置更新
     *
     * @param fileConfigDetailList
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 9:38
     */
    @Override
    public boolean fileConfigurationUpdate(List<FileConfigDetail> fileConfigDetailList, String appId) {
        List<FileConfigurationDO> add = new ArrayList<>(16);
        List<FileConfigurationDO> upd = new ArrayList<>(16);
        fileConfigDetailList.stream().forEach(fileConfigDetail -> {
            FileConfigurationDO item = JsonUtils.castObject(fileConfigDetail, FileConfigurationDO.class);
            FileConfigurationDO fileConfigurationDO = getOne(new QueryWrapper<FileConfigurationDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (fileConfigurationDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(fileConfigurationDO, item)) {
                    item.setId(fileConfigurationDO.getId());
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