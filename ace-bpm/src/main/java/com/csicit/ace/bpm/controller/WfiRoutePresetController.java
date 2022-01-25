package com.csicit.ace.bpm.controller;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.exception.WfiRoutePresetNotFoundByTaskIdException;
import com.csicit.ace.bpm.pojo.domain.WfiRoutePresetDO;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.service.WfiRoutePresetService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/8/28 17:00
 */
@RestController
@RequestMapping("/wfiRoutePreset")
public class WfiRoutePresetController {
    @Autowired
    private WfiRoutePresetService wfiRoutePresetService;
    @Autowired
    private BpmAdapter bpmAdapter;

    /**
     * 获取预设信息
     *
     * @param taskId 任务id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/9/23 23:09
     */

    @CrossOrigin
    @RequestMapping("/getByTaskId/{taskId}")
    public R getByTaskId(@PathVariable("taskId") String taskId) {
        WfiRoutePresetDO wfiRoutePreset = wfiRoutePresetService.getByTaskId(taskId);
//        if (wfiRoutePreset == null) {
//            TaskInstance task = bpmAdapter.getTaskInstanceById(taskId);
//            List<WfiRoutePresetDO> wfiRoutePresets = wfiRoutePresetService.list(new QueryWrapper<WfiRoutePresetDO>().eq("flow_id", task.getFlowInstanceId()).orderByDesc("preset_time"));
//            if (wfiRoutePresets.size() > 0) {
//                for (WfiRoutePresetDO routePreset : wfiRoutePresets) {
//                    PresetInfo presetInfo = JSONObject.parseObject(routePreset.getPresetInfo(), PresetInfo.class);
//                    if (presetInfo.getPresetRoutes().stream().anyMatch(o -> StringUtils.equals(task.getNodeId(), o.getNodeId()))) {
//                        wfiRoutePreset = routePreset;
//                        break;
//                    }
//                }
//            }
//        }
        return R.ok().put("instance", wfiRoutePreset);
    }

    /**
     * 获取用户提交的预设信息
     *
     * @param taskId 任务ID
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/9/23 23:08
     */

    @CrossOrigin
    @RequestMapping("/getPresetInfoByTaskId/{taskId}")
    public R getPresetInfoByTaskId(@PathVariable("taskId") String taskId) {
        WfiRoutePresetDO wfiRoutePreset = wfiRoutePresetService.getByTaskId(taskId);
        if (wfiRoutePreset == null) {
            throw new WfiRoutePresetNotFoundByTaskIdException(taskId);
        }
        PresetInfo presetInfo = FlowUtils.getPresetInfo(wfiRoutePreset.getPresetInfo());
        return R.ok().put("presetInfo", presetInfo);
    }

    /**
     * 获取可预设的路径列表
     *
     * @param taskId 任务ID
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/9/23 23:08
     */

    @CrossOrigin
    @RequestMapping("/listAvailablePresetInfos")
    public R listAvailablePresetInfosByTaskId(@RequestParam("taskId") String taskId) {
        List<PresetInfo> presetInfos = bpmAdapter.listAvailablePresetInfosByTaskId(taskId);
        return R.ok().put("presetInfos", presetInfos);
    }


    /**
     * @Author zhangzhaojun
     * @Description //根据部门获取预设流程人员
     * @Date 8:34 2021/9/14
     * @Param departmentId,flowId,nodeId
     * @return userList
     **/

    @CrossOrigin
    @RequestMapping("/getPresetUserListByDepartment")
    public R getPresetUserListByDepartment(@RequestParam("departmentId") String departmentId,@RequestParam("nodeId") String nodeId
    ,@RequestParam("flowId")String flowId) {
        List<SysUserDO> sysUserDOS = bpmAdapter.getUsersByDepartmentId(departmentId);
        List<String> userIds = bpmAdapter.getUserIdsByNodeId(flowId,nodeId);
        List<SysUserDO> sysUserDOS1 = bpmAdapter.getUsersByUserIds(userIds);
        sysUserDOS.retainAll(sysUserDOS1);
        return R.ok().put("userList", sysUserDOS);
    }

}
