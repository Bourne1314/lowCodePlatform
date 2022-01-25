package com.csicit.ace.bpm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.exception.WfiCommentNotFoundByIdException;
import com.csicit.ace.bpm.pojo.domain.WfiCommentDO;
import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.domain.WfiFocusedWorkDO;
import com.csicit.ace.bpm.pojo.vo.WfdMessageVO;
import com.csicit.ace.bpm.service.WfiCommentService;
import com.csicit.ace.bpm.service.WfiFocusedWorkService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IMessage;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2020/5/25 19:15
 */
@RestController
@RequestMapping("/wfiComment")
@Api("流程评论意见")
public class WfiCommentController extends BaseController {
    @Autowired
    private WfiCommentService wfiCommentService;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    private WfiFocusedWorkService wfiFocusedWorkService;
    @Autowired
    private IMessage iMessage;

    /**
     * 获取评论列表
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/2 16:06
     */

    @CrossOrigin
    @RequestMapping("/listByFlowId")
    public R listByFlowId(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String flowId = (String) params.get("flowId");
        Page<WfiCommentDO> page = new Page<>(current, size);
        IPage<WfiCommentDO> list = wfiCommentService.page(page, new QueryWrapper<WfiCommentDO>().eq("APP_ID", securityUtils.getAppName()).eq("FLOW_ID", flowId).orderByAsc("COMMENT_TIME"));
        List<WfiCommentDO> replyToComments = list.getRecords().stream().filter(o -> StringUtils.isNotBlank(o.getReplyCommentId())).collect(Collectors.toList());
        List<String> replyCommentIds = replyToComments.stream().map(WfiCommentDO::getReplyCommentId).collect(Collectors.toList());
        Collection<WfiCommentDO> replyComments = wfiCommentService.listByIds(replyCommentIds);
        for (WfiCommentDO replyToComment : replyToComments) {
            List<WfiCommentDO> comments = replyComments.stream().filter(o -> o.getId().equals(replyToComment.getReplyCommentId())).collect(Collectors.toList());
            if (comments.size() > 0) {
                replyToComment.setReplyComment(comments.get(0));
            } else {
                // 如果找不到回复的评论，则返回新的评论给前端
                WfiCommentDO wfiComment = new WfiCommentDO();
                wfiComment.setCommentText(LocaleUtils.getWfiCommentIsDeleted());
                replyToComment.setReplyComment(wfiComment);
            }
        }
        bpmAdapter.appendNodeInfo(flowId, list.getRecords());
        return R.ok().put("page", list);
    }

    /**
     * 获取评论列表
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/2 16:06
     */

    @CrossOrigin
    @RequestMapping("/listByTaskId")
    public R listByTaskId(@RequestParam Map<String, Object> params) {
        String taskId = (String) params.get("taskId");
        TaskInstance taskInstance = bpmAdapter.getTaskInstanceById(taskId);
        params.put("flowId", taskInstance.getFlowInstanceId());
        return listByFlowId(params);
    }

    /**
     * 获取评论列表
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/2 16:06
     */

    @CrossOrigin
    @RequestMapping("/listByBusinessKey")
    public R listByBusinessKey(@RequestParam Map<String, Object> params) {
        String flowCode = (String) params.get("flowCode");
        String businessKey = (String) params.get("businessKey");
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowByBusinessKey(flowCode, businessKey);
        params.put("flowId", wfiFlow.getId());
        return listByFlowId(params);
    }

    /**
     * 新增评论
     *
     * @param wfiComment 评论
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/2 16:06
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody WfiCommentDO wfiComment) {
        // 校验流程实例是否存在
        WfiFlowDO wfiFlow = bpmAdapter.getWfiFlowById(wfiComment.getFlowId());
        if (StringUtils.isNotBlank(wfiComment.getReplyCommentId())) {
            WfiCommentDO replyComment = wfiCommentService.getById(wfiComment.getReplyCommentId());
            if (replyComment == null) {
                throw new WfiCommentNotFoundByIdException(wfiComment.getId());
            }
            wfiComment.setReplyUserId(replyComment.getCommentUserId());
            wfiComment.setReplyUser(replyComment.getCommentUser());
        }
        if (StringUtils.isNotBlank(wfiComment.getTaskId())) {
            // 前端不允许主动增加任务相关的评论
            wfiComment.setTaskId("");
        }
        SysUserDO user = securityUtils.getCurrentUser();
        wfiComment.setCommentUserId(user.getId());
        wfiComment.setCommentUser(user.getRealName());
        wfiComment.setAppId(securityUtils.getAppName());
        wfiComment.setCommentTime(LocalDateTime.now());
        if (wfiCommentService.save(wfiComment)) {
            // TODO: 2020/5/25 添加评论消息提醒
            List<WfiFocusedWorkDO> focusedWorks = wfiFocusedWorkService.list(new QueryWrapper<WfiFocusedWorkDO>()
                    .eq("flow_instance_id", wfiComment.getFlowId()).select("user_id"));
            if (CollectionUtils.isNotEmpty(focusedWorks)) {
                List<String> userIds = new ArrayList<>(focusedWorks.parallelStream().map(WfiFocusedWorkDO::getUserId).collect(Collectors.toSet()));
                Map<String, Object> data = new HashMap<>(16);
                data.put("userName", user.getRealName());
                data.put("flowNo", wfiFlow.getFlowNo());
                data.put("formId", wfiFlow.getBusinessKey());
                data.put("flowCode", wfiFlow.getFlowCode());
                iMessage.sendMessage(userIds, Constants.BpmCommentNoticeChannelName, Constants.BpmCommentNoticeTemplate, data);
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 获取评论
     *
     * @param id 评论id
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/6/2 16:07
     */

    @CrossOrigin
    @RequestMapping("/{id}")
    public R get(@PathVariable("id") String id) {
        return R.ok().put("instance", wfiCommentService.getById(id));
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        wfiCommentService.delete(ids);
        return R.ok();
    }
}