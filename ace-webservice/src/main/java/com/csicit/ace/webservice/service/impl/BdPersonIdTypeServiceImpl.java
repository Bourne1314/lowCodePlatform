package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.webservice.mapper.BdPersonIdTypeMapper;
import com.csicit.ace.webservice.service.BdPersonIdTypeService;
import com.csicit.ace.webservice.service.OrgGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 基础数据-人员证件类型 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:06
 */
@Service
public class BdPersonIdTypeServiceImpl extends ServiceImpl<BdPersonIdTypeMapper, BdPersonIdTypeDO> implements
        BdPersonIdTypeService {

    @Autowired
    OrgGroupService orgGroupService;

    @Override
    public R insert(BdPersonIdTypeDO personDoc) {
        personDoc.setCreateTime(LocalDateTime.now());
        personDoc.setUpdateTime(LocalDateTime.now());
        personDoc.setCreateUser("webservice");
        if (save(personDoc)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(BdPersonIdTypeDO personDoc) {
        personDoc.setUpdateTime(LocalDateTime.now());
        if (updateById(personDoc)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }


    @Override
    public R delete(List<String> ids) {
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            List<BdPersonIdTypeDO> list = list(new QueryWrapper<BdPersonIdTypeDO>().select("name", "group_id").in
                    ("id", ids));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
//                String groupId = list.get(0).getGroupId();
                if (removeByIds(ids)) {
                    return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
