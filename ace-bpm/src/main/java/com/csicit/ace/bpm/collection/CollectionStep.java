package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.interfaces.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指定的一个或多个步骤中的参与用户，取流程中已经办结的指定步骤实例的参与人员
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionStep implements BaseCollection {


    private final String name = "指定步骤参与人员";

    private final String code = "step";
    @Autowired
    private IUser iUser;
    @Autowired
    private BpmManager bpmManager;

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
        List<SysUserDO> sysUsers = new ArrayList<>();
        // 待闫四洋告知如何获取指定步骤
//        if (variables.containsKey(NODE)) {
//            Node node = (Node) variables.get(NODE);
//            if (variables.containsKey(FLOW_INSTANCE_ID)) {
//                String flowInstanceId = (String) variables.get(FLOW_INSTANCE_ID);
//                if (StringUtils.isNotEmpty(flowInstanceId)) {
//                    List<TaskInstance> taskInstances = bpmManager.getTasksByNodeId(flowInstanceId, node.getId());
//                    for (TaskInstance taskInstance : taskInstances) {
//                        sysUsers.addAll(bpmManager.getUsersByNodeId(flowInstanceId, taskInstance.getName()));
//                    }
//                }
//            }
//        }
        return sysUsers;
    }
}
