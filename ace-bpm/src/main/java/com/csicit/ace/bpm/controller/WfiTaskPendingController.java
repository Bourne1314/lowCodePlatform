package com.csicit.ace.bpm.controller;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author JonnyJiang
 * @date 2020/6/29 11:49
 */
@RestController
@RequestMapping("/wfiTaskPending")
public class WfiTaskPendingController extends BaseController {
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private BpmManager bpmManager;

    /**
     * 刷新待办任务列表
     *
     * @param flowId 流程实例id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/29 11:52
     */

    @CrossOrigin
    @RequestMapping(value = "/refresh/{flowId}", method = RequestMethod.POST)
    public R refresh(@PathVariable("flowId") String flowId) {
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(flowId);
        bpmManager.refreshTaskPending(flowId, FlowUtils.getFlow(wfiFlow.getModel()));
        return R.ok();
    }
}
