package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.TaskInstance;
import com.csicit.ace.bpm.enums.NodeType;
import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.bpm.pojo.vo.wfd.Link;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一或多个前序步骤中的参与用户（无参数）
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionPrev implements BaseCollection {

    private final String name = "前一步参与人员";

    private final String code = "prev";

    @Autowired
    private IUser iUser;

    @Autowired
    private BpmAdapter bpmAdapter;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public List<SysUserDO> getCollection(List<IdName> idNames, Map<String, Object> variables) {
        List<String> sysUserIds = new ArrayList<>();
        if (variables.containsKey(NODE)) {
            Node node = (Node) variables.get(NODE);
            if (variables.containsKey(FLOW_INSTANCE_ID)) {
                String flowInstanceId = (String) variables.get(FLOW_INSTANCE_ID);
                if (StringUtils.isNotEmpty(flowInstanceId)) {
                    for (Link link : node.getFlowInLinks()) {
                        Node fromNode = link.getFromNode();
                        if (NodeType.Manual.isEquals(fromNode.getNodeType()) || NodeType.Free.isEquals(fromNode.getNodeType())) {
//                            List<TaskInstance> taskInstances = bpmAdapter.getTasksByNodeId(flowInstanceId, node.getId());
                            List<TaskInstance> taskInstances = bpmAdapter.getTasksByNodeId(flowInstanceId, fromNode.getId());
                            for (TaskInstance taskInstance : taskInstances) {
                                sysUserIds.addAll(bpmAdapter.getUserIdsByNodeId(flowInstanceId, taskInstance.getNodeId()));
                            }
                        }
                    }
                }
            }
        }
        return iUser.getUsersByIds(sysUserIds);
    }
}
