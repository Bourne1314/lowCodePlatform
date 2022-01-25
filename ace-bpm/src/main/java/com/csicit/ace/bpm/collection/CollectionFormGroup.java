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
 * 指定的一个表达那字段存储用户组ID集合，去该集合内用户组中的所有用户
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionFormGroup implements BaseCollection {


    private final String name = "表单字段指定用户组";

    private final String code = "formGroup";

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
        String groupId = collectionFormUtil.getIdFromForm(idNames, variables);
        if (StringUtils.isNotBlank(groupId)) {
            List<String> ids = new ArrayList<>();
            ids.add(groupId);
            return wfdFlow.getSomeUsers("group", ids, new HashMap<>());
        }
        return new ArrayList<>();
    }
}
