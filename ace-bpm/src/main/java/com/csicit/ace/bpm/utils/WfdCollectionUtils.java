package com.csicit.ace.bpm.utils;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.SessionAttribute;
import com.csicit.ace.bpm.collection.BaseCollection;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/27 10:53
 */
@Component
public class WfdCollectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WfdCollectionUtils.class);

    @Autowired
    RuleUtils ruleUtils;

    BaseCollection baseCollection;

    @Autowired
    List<BaseCollection> allCollections;

    @Autowired
    HistoryService historyService;

    @Autowired
    private HttpSession session;

    /**
     * 根据规则获取用户列表
     *
     * @param node
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysUserDO>
     * @author JonnyJiang
     * @date 2019/10/9 9:31
     */

    public List<SysUserDO> getUsers(Node node) {
        return getUsers(node, new HashMap<>(16));
    }

    /**
     * 根据规则获取满足密级的用户列表
     *
     * @param node
     * @param secretLevel
     * @return 满足密级的用户列表
     * @author JonnyJiang
     * @date 2021/8/16 14:07
     */

    public List<SysUserDO> getUsers(Node node, Integer secretLevel) {
        List<SysUserDO> users = getUsers(node);
        if(secretLevel != null) {
            for (int i = 0; i < users.size(); ) {
                if (SecurityUtils.compareSecretLevel(secretLevel, users.get(i).getSecretLevel()) > 0) {
                    // 如果密级不满足，则移除
                    users.remove(i);
                } else {
                    i++;
                }
            }
        }
        return users;
    }

    /**
     * 根据规则获取用户列表
     *
     * @param node      流程定义
     * @param variables 变量
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysUserDO>
     * @author JonnyJiang
     * @date 2019/10/9 9:31
     */

    public List<SysUserDO> getUsers(Node node, Map<String, Object> variables) {
        if (StringUtils.isBlank(node.getParticipateRule())) {
            return new ArrayList<>();
        } else {
            if (!variables.containsKey(BaseCollection.FLOW_STARTER_ID)) {
                // 流程发起人
                String wfiFlowId = (String) session.getAttribute(SessionAttribute.WFD_FLOW_EL_FLOW_ID);
                if (StringUtils.isNotBlank(wfiFlowId)) {
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(wfiFlowId).singleResult();
                    if (historicProcessInstance != null) {
                        variables.put(BaseCollection.FLOW_STARTER_ID, historicProcessInstance.getStartUserId());
                        variables.put(BaseCollection.FLOW_INSTANCE_ID, historicProcessInstance.getId());
                    }
                }
            }
            return getUsers(node, JsonUtils.castObject(node.getParticipateRule(), WfdCollectionDO.class), variables);
        }
    }

    /**
     * 根据规则获取用户列表
     *
     * @param collection
     * @return
     * @author yansiyang
     * @date 2019/8/27 10:58
     */
    public List<SysUserDO> getUsers(Node node, WfdCollectionDO collection, Map<String, Object> variables) {
        variables.put(BaseCollection.NODE, node);
        try {
            String type = collection.getType();
            if (Objects.equals(type, "base")) {
                return getBaseUsers(collection, variables);
            } else if (Objects.equals(type, "intersect")) {
                //交集
                List<WfdCollectionDO> collections = collection.getCollections();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(collections)) {
                    if (collections.size() == 1) {
                        return getUsers(node, collections.get(0), variables);
                    } else {
                        List<SysUserDO> firstList = getUsers(node, collections.get(0), variables);
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(firstList)) {
                            for (int i = 1; i < collections.size(); i++) {
                                WfdCollectionDO collectionT = collections.get(i);
                                List<SysUserDO> listT = getUsers(node, collectionT, variables);
                                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(listT)) {
                                    firstList.retainAll(listT);
                                } else {
                                    // listT为null 直接返回null
                                    return new ArrayList<>();
                                }
                            }
                            return firstList;
                        } else {
                            //firstList为null 直接返回null
                            return new ArrayList<>();
                        }
                    }
                }
            } else if (Objects.equals(type, "union")) {
                //并集
                List<WfdCollectionDO> collections = collection.getCollections();
                Set<SysUserDO> users = new HashSet<>();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(collections)) {
                    collections.forEach(collectionDO -> {
                        users.addAll(getUsers(node, collectionDO, variables));
                    });
                }
                List<SysUserDO> list = new ArrayList<>();
                list.addAll(users);
                return list;
            } else if (Objects.equals(type, "except")) {
                //差集
                List<WfdCollectionDO> collections = collection.getCollections();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(collections)) {
                    if (collections.size() == 1) {
                        return getUsers(node, collections.get(0), variables);
                    } else {
                        List<SysUserDO> firstList = getUsers(node, collections.get(0), variables);
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(firstList)) {
                            for (int i = 1; i < collections.size(); i++) {
                                WfdCollectionDO collectionT = collections.get(i);
                                List<SysUserDO> listT = getUsers(node, collectionT, variables);
                                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(listT)) {
                                    firstList.removeAll(listT);
                                }
                            }
                            return firstList;
                        } else {
                            //firstList为null 直接返回null
                            return new ArrayList<>();
                        }
                    }
                }
            } else if (Objects.equals(type, "ifelse")) {
                String ruleId = collection.getRule();
                boolean satisfy = collection.isSatisfy();
                boolean result = false;
                if (StringUtils.isNotBlank(ruleId)) {
                    result = ruleUtils.calculateRule(node.getFlow(), ruleId, satisfy);
                } else {
                    result = true;
                }
                //ifTrue
                if (result) {
                    WfdCollectionDO collectionT = collection.getIfTrue();
                    return getUsers(node, collectionT, variables);
                } else {
                    //ifFalse
                    WfdCollectionDO collectionF = collection.getIfFalse();
                    return getUsers(node, collectionF, variables);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.toString());
        }
        return new ArrayList<>();
    }

    /**
     * 根据规则获取基础集合用户列表
     *
     * @param collection
     * @param variables
     * @return
     * @author yansiyang
     * @date 2019/8/27 10:58
     */
    public List<SysUserDO> getBaseUsers(WfdCollectionDO collection, Map<String, Object> variables) {
        String collectionType = collection.getCollectionType();
        if (StringUtils.isNotBlank(collectionType)) {
            for (int i = 0; i < allCollections.size(); i++) {
                baseCollection = allCollections.get(i);
                JSONObject json = JsonUtils.castObject(baseCollection, JSONObject.class);
                if (Objects.equals(json.getString("code"), collectionType)) {
                    break;
                }
            }
            return baseCollection.getCollection(collection.getParams(), variables);
        }
        return new ArrayList<>();
    }
}
