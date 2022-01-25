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
 * 发起人（无参数）
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionInitiator implements BaseCollection {

    private final String name = "发起人";

    private final String code = "initiator";

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
        List<SysUserDO> users = new ArrayList<>();
        if (variables.containsKey(FLOW_STARTER_ID)) {
            String starterId = (String) variables.get(FLOW_STARTER_ID);
            SysUserDO sysUser = iUser.getUserById(starterId);
            if (sysUser == null) {
                throw new BpmException(LocaleUtils.getFlowStarterNotFound(starterId));
            } else {
                users.add(sysUser);
            }
        }
        return users;
    }
}
