package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.interfaces.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程发起人所属集团用户（无参数）
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionInitGroupComp implements BaseCollection {

    private final String name = "发起人集团人员";

    private final String code = "initGroupComp";
    @Autowired
    private IUser iUser;

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
        if (variables.containsKey(FLOW_STARTER_ID)) {
            String starterId = (String) variables.get(FLOW_STARTER_ID);
            SysUserDO starter = iUser.getUserById(starterId);
            if (starter == null) {
                throw new BpmException(LocaleUtils.getFlowStarterNotFound(starterId));
            } else {
                return iUser.getUsersByGroupId(starter.getGroupId());
            }
        }
        return new ArrayList<>();
    }
}
