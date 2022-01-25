package com.csicit.ace.bpm.service.impl;

import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.bpm.mapper.WfdFlowCategoryMapper;
import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.csicit.ace.bpm.service.WfdFlowCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 流程类别 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Service("wfdFlowCategoryService")
public class WfdFlowCategoryServiceImpl extends BaseServiceImpl<WfdFlowCategoryMapper, WfdFlowCategoryDO> implements
        WfdFlowCategoryService {
    /**
     * 存在判断
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public String existCheck(WfdFlowCategoryDO instance) {
        // 判断流程类别是否已存在
        int count = count(new QueryWrapper<WfdFlowCategoryDO>().eq("app_id", instance.getAppId
                ()).eq("name",
                instance.getName()));
        if (count > 0) {
            return InternationUtils.getInternationalMsg("SAME_MODEL",
                    new Object[]{"FLOW_CATEGORY", instance.getName()}
            );
        }
        // 判断序号是否存在
        int count2 = count(new QueryWrapper<WfdFlowCategoryDO>().eq("app_id", instance.getAppId
                ()).eq("sort_no",
                instance.getSortNo()));
        if (count2 > 0) {
            return InternationUtils.getInternationalMsg("SAME_MODEL",
                    new Object[]{InternationUtils.getInternationalMsg("SORT_NO"), instance.getSortNo().toString()}
            );
        }
        return "";
    }
}
