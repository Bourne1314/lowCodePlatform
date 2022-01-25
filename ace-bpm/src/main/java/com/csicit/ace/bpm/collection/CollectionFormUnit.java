package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指定的一个表单字段存储业务单元ID集合，取该集合内所有业务单元所辖用户
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionFormUnit implements BaseCollection {

    private final String name = "表单字段指定单元人员";

    private final String code = "formUnit";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Autowired
    CollectionFormUtil collectionFormUtil;

    @Autowired
    IWfdFlow wfdFlow;

    @Override
    public List<SysUserDO> getCollection(List<IdName> idNames, Map<String, Object> variables) {
        String unitId = collectionFormUtil.getIdFromForm(idNames, variables);
        if (StringUtils.isNotBlank(unitId)) {
            List<String> ids = new ArrayList<>();
            ids.add(unitId);
            return wfdFlow.getSomeUsers("unit", ids, new HashMap<>());
        }
        return new ArrayList<>();
    }
}
