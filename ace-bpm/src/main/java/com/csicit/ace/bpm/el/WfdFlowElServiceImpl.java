package com.csicit.ace.bpm.el;

import com.csicit.ace.bpm.*;
import com.csicit.ace.bpm.enums.DataType;
import com.csicit.ace.bpm.exception.ElBusinessKeyNotFoundException;
import com.csicit.ace.bpm.exception.ElFlowInstanceIdNotFoundException;
import com.csicit.ace.bpm.exception.ElFlowNotFoundException;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.FormField;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.dbplus.mapper.DBHelperMapper;
import com.csicit.ace.interfaces.service.ISecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取表达字段值
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/27 8:17
 */
@Service("elService")
public class WfdFlowElServiceImpl implements WfdFlowElService {
    @Autowired
    HttpSession session;

    @Autowired
    DBHelperMapper dbHelperMapper;

    @Autowired
    WfdFlowService wfdFlowService;

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private BpmManager bpmManager;
    @Autowired
    private BpmAdapter bpmAdapter;
    @Autowired
    ISecurity iSecurity;

    /**
     * 获取流程实例ID
     *
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2019/10/23 17:44
     */

    private String getFlowInstanceId() {
        String flowInstanceId = (String) session.getAttribute(SessionAttribute.WFD_FLOW_EL_FLOW_ID);
        if (StringUtils.isEmpty(flowInstanceId)) {
            throw new ElFlowInstanceIdNotFoundException();
        }
        return flowInstanceId;
    }

    private String getBusinessKey() {
        String businessKey = (String) session.getAttribute(SessionAttribute.WFD_FLOW_EL_BUSINESS_KEY);
        if (StringUtils.isEmpty(businessKey)) {
            throw new ElBusinessKeyNotFoundException();
        }
        return businessKey;
    }

    private Flow getFlow() {
        Flow flow = JsonUtils.castObject(session.getAttribute(SessionAttribute.WFD_FLOW_EL_FLOW), Flow.class);
        if (flow == null) {
            throw new ElFlowNotFoundException();
        }
        return flow;
    }

    @Override
    public String getPostName(String userId) {
        BdPostDO postDO = iSecurity.getMainPostByUserId(userId);
        if (postDO != null) {
            return postDO.getName();
        }
        return null;
    }

    @Override
    public String getPostNameByUserIdFromFormValue(String columnName) {
        String userId = getFormValue(columnName);
        return getPostName(userId);
    }

    @Override
    public String getFormValue(String columnName) {
        Flow flow = getFlow();
        Object value = bpmAdapter.getFormValue(flow.getFormDataTable(), columnName, flow.getFormIdName(), getBusinessKey());
        String result = "";
        if (value != null) {
            FormField formField = flow.getFormFieldByName(columnName);
            switch (formField.getDataType()) {
                case DataType.Boolean:
                    result = String.valueOf(IntegerUtils.isTrue(Integer.parseInt(String.valueOf(value))) ? IntegerUtils.TRUE_VALUE : IntegerUtils.FALSE_VALUE);
                    break;
                case DataType.DateTime:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WfdFlowElService.DATE_FORMAT_PATTERN);
                    result = simpleDateFormat.format(value);
                    break;
                case DataType.Integer:
                    result = String.valueOf(value);
                    break;
                case DataType.String:
                    result = String.valueOf(value);
                    break;
                default:
                    result = String.valueOf(value);
                    break;
            }
        }
        return result;
    }

    @Override
    public String getNodeNames() {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(getFlowInstanceId());
        if (taskInstances.size() > 0) {
            // 如果有正在办理的任务
            return StringUtils.join(taskInstances.stream().map(TaskInstance::getNodeName).distinct().collect(Collectors.toList()), ",");
        } else {
            // 如果没有正在办理的任务，判断是否已办结
            return getNodeNames(getFlowInstanceId());
        }
    }

    private String getNodeNames(String flowInstanceId) {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(flowInstanceId);
        if (flowInstance.getEndTime() == null) {
            return "";
        } else {
            return LocaleUtils.getFlowStateEnded();
        }
    }

    @Override
    public String getNodeCodes() {
        List<TaskInstance> taskInstances = bpmAdapter.listTasksByFlowInstanceId(getFlowInstanceId());
        if (taskInstances.size() > 0) {
            // 如果有正在办理的任务
            return StringUtils.join(taskInstances.stream().map(TaskInstance::getNodeCode).distinct().collect(Collectors.toList()), ",");
        } else {
            // 如果没有正在办理的任务，需要向下级查询
            return getNodeCodes(getFlowInstanceId());
        }
    }

    private String getNodeCodes(String flowInstanceId) {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(flowInstanceId);
        if (flowInstance.getEndTime() == null) {
            return "";
        } else {
            return "";
        }
    }

    @Override
    public String getNodeSns() {
        return "";
    }

    @Override
    public String getCompleted() {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(getFlowInstanceId());
        return String.valueOf(flowInstance.getEndTime() == null ? IntegerUtils.FALSE_VALUE : IntegerUtils.TRUE_VALUE);
    }

    @Override
    public String getFlowState() {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(getFlowInstanceId());
        return flowInstance.getEndTime() == null ? LocaleUtils.getFlowStateRunning() : LocaleUtils.getFlowStateEnded();
    }

    @Override
    public String getNodeHostId(String nodeCode) {
        return bpmAdapter.getNodeHostId(getFlowInstanceId(), nodeCode);
    }

    @Override
    public String getNodeHostRealName(String nodeCode) {
        SysUserDO host = bpmAdapter.getNodeHost(getFlowInstanceId(), nodeCode);
        if (host == null) {
            return "";
        }
        return host.getRealName();
    }

    @Override
    public String getNodeHostOpinion(String nodeCode) {
        return bpmAdapter.getNodeHostOpinion(getFlowInstanceId(), nodeCode);
    }

    @Override
    public String getStartTime() {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(getFlowInstanceId());
        if (flowInstance.getStartTime() == null) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            return simpleDateFormat.format(flowInstance.getStartTime());
        }
    }

    @Override
    public String getEndTime() {
        FlowInstance flowInstance = bpmAdapter.getFlowInstance(getFlowInstanceId());
        if (flowInstance.getEndTime() == null) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            return simpleDateFormat.format(flowInstance.getEndTime());
        }
    }

    private String parseToString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            return simpleDateFormat.format(date);
        }
    }

    @Override
    public String getNodeStartTime(String nodeCode) {
        Date startTime = bpmAdapter.getNodeStartTime(getFlowInstanceId(), nodeCode);
        return parseToString(startTime);
    }

    @Override
    public String getNodeEndTime(String nodeCode) {
        Date endTime = bpmAdapter.getNodeEndTime(getFlowInstanceId(), nodeCode);
        return parseToString(endTime);
    }

    @Override
    public String getHostDeptId(String nodeCode) {
        SysUserDO host = bpmAdapter.getNodeHost(getFlowInstanceId(), nodeCode);
        if (host != null) {
            return host.getDepartmentId();
        }
        return null;
    }

    @Override
    public String getHostDeptName(String nodeCode) {
        SysUserDO host = bpmAdapter.getNodeHost(getFlowInstanceId(), nodeCode);
        if (host != null) {
            return host.getDepartmentName();
        }
        return null;
    }

    @Override
    public String getCurrentUserId() {
        return securityUtils.getCurrentUserId();
    }

    @Override
    public String getCurrentUserRealName() {
        return securityUtils.getCurrentUser().getRealName();
    }

    @Override
    public String getRejectFromNodeCode() {
        List<Node> nodes = bpmAdapter.getRejectFromNode(getFlowInstanceId());
        return nodes.size() == 0 ? "" : StringUtils.join(nodes.stream().map(Node::getCode), ",");
    }

    @Override
    public String getRejectFromNodeName() {
        List<Node> nodes = bpmAdapter.getRejectFromNode(getFlowInstanceId());
        return nodes.size() == 0 ? "" : StringUtils.join(nodes.stream().map(Node::getName), ",");
    }
}