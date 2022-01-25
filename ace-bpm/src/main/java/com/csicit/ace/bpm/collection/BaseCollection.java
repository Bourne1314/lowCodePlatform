package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.List;
import java.util.Map;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:41
 */
public interface BaseCollection {
    /**
     * 流程定义
     */
    String FLOW_INSTANCE_ID = "FLOW_INSTANCE_ID";
    /**
     * 流程发起人ID
     */
    String FLOW_STARTER_ID = "FLOW_STARTER_ID";
    /**
     * 当前节点
     */
    String NODE = "NODE";

    String getName();

    String getCode();

    /**
     * 获取相关人员
     * @param idNames
     * @return
     * @author yansiyang
     * @date 2019/11/12 18:05
     */
    List<SysUserDO> getCollection(List<IdName> idNames, Map<String, Object> variables);
}
