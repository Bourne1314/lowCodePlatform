package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.ProChangelogHistoryDO;
import com.csicit.ace.data.persistent.mapper.ProChangelogHistoryMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProChangelogHistoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * changlog历史信息管理 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019/11/25 11:12
 */
@Service("proChangelogHistoryService")
public class ProChangelogHistoryServiceImpl extends BaseServiceImpl<ProChangelogHistoryMapper, ProChangelogHistoryDO>
        implements
        ProChangelogHistoryService {

    /**
     * 保存history
     *
     * @param logValue
     * @param serviceId
     * @return
     */
    @Override
    public boolean saveChangelogHistory(String id, String logValue, String serviceId, String dsId) {
        ProChangelogHistoryDO proChangelogHistoryDO = new ProChangelogHistoryDO();
        proChangelogHistoryDO.setId(id);
        proChangelogHistoryDO.setServiceId(serviceId);
        proChangelogHistoryDO.setLogValue(logValue);
        proChangelogHistoryDO.setCreateTime(LocalDateTime.now());
        proChangelogHistoryDO.setPublishTag(0);
        proChangelogHistoryDO.setUseLess(1);
        proChangelogHistoryDO.setDsId(dsId);
        if (!save(proChangelogHistoryDO)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean publishChangeLog(String serviceId) {
        List<ProChangelogHistoryDO> proChangelogHistoryDOS = list(new QueryWrapper<ProChangelogHistoryDO>()
                .eq("service_id", serviceId).eq("publish_tag", 0));
        Integer maxVersion = getOne(new QueryWrapper<ProChangelogHistoryDO>().eq("service_id", serviceId).orderByDesc
                ("publish_version")).getPublishVersion();
        proChangelogHistoryDOS.stream().forEach(proChangelogHistoryDO -> {
            proChangelogHistoryDO.setPublishTag(1);
            proChangelogHistoryDO.setPublishTime(LocalDateTime.now());
            proChangelogHistoryDO.setPublishVersion(maxVersion == null ? 0 : maxVersion + 1);
        });
        if (CollectionUtils.isNotEmpty(proChangelogHistoryDOS)) {
            if (!updateBatchById(proChangelogHistoryDOS)) {
                return false;
            }
        }

        return true;
    }
}
