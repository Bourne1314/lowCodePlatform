package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指定一或多个部门所辖的所有用户
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionDept implements BaseCollection {

    @Autowired
    IWfdFlow wfdFlow;

    private final String name = "部门人员";

    private final String code = "dept";

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
        List<String> ids = idNames.stream().map(IdName::getId).collect(Collectors.toList());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            return wfdFlow.getSomeUsers(code, ids, new HashMap<>());
        }
        return new ArrayList<>();
    }
}
