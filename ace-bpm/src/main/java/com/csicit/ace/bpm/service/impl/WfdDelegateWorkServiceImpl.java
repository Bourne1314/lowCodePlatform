package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.mapper.WfdDelegateWorkMapper;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateWorkDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdDelegateWorkService;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/25 10:00
 */
@Service("wfdDelegateWorkService")
public class WfdDelegateWorkServiceImpl extends BaseServiceImpl<WfdDelegateWorkMapper, WfdDelegateWorkDO> implements WfdDelegateWorkService {

    @Autowired
    WfiFlowService wfiFlowService;

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public List<WfdDelegateWorkDO> getDelegateWorkByUserId(String userId) {
        return list(new QueryWrapper<WfdDelegateWorkDO>()
                .eq("app_id", appName).eq("user_id", securityUtils.getCurrentUserId())
                .orderByDesc("create_time"));
    }

    @Override
    public List<WfdDelegateWorkDO> getDelegateWorkByUserId(int current, int size, String userId) {
        Page<WfdDelegateWorkDO> page = new Page<>(current, size);
        IPage<WfdDelegateWorkDO> list = page(page, new QueryWrapper<WfdDelegateWorkDO>()
                .eq("app_id", appName).eq("user_id", securityUtils.getCurrentUserId())
                .orderByDesc("create_time"));
        return list.getRecords();
    }

    @Override
    public void fillDelegateWork() {
        List<WfdDelegateWorkDO> wfdDelegateWorkDOS = list(null);
        wfdDelegateWorkDOS.stream().forEach(work -> {
            if (StringUtils.isBlank(work.getNodeName())) {
                WfiFlowDO wfiFlowDO = wfiFlowService.getById(work.getProcessId());
                Flow flow = FlowUtils.getFlow(wfiFlowDO.getModel());
                Node node = flow.getNodeById(work.getTaskName());
                work.setNodeName(node.getName());
                work.setFlowNo(wfiFlowDO.getFlowNo());
                work.setWfdId(flow.getId());
                work.setWfdCategoryId(flow.getCategoryId());
            }
        });
        updateBatchById(wfdDelegateWorkDOS);
    }
}
