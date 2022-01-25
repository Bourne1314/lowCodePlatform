package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.bpm.utils.ExpressionUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指定一或多个用户
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:46
 */
@Service
public class CollectionEl implements BaseCollection {

    @Autowired
    IWfdFlow wfdFlow;

    @Autowired
    ExpressionUtils expressionUtils;

    private final String name = "表达式";

    private final String code = "el";

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
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(idNames)) {
            String el = idNames.get(0).getName();
            if (StringUtils.isNotBlank(el)) {
                String str = expressionUtils.parseExpression(el);
                return JsonUtils.castObject(str, List.class);
            }
        }
        return new ArrayList<>();
    }
}
