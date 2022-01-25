package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.OrgDepartmentVDO;
import com.csicit.ace.webservice.mapper.OrgDepartmentVMapper;
import com.csicit.ace.webservice.service.OrgDepartmentVService;
import org.springframework.stereotype.Service;


/**
 * 组织-部门版本 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:43
 */
@Service("orgDepartmentVService")
public class OrgDepartmentVServiceImpl extends ServiceImpl<OrgDepartmentVMapper, OrgDepartmentVDO> implements
        OrgDepartmentVService {

    /**
     * 保存部门
     *
     * @param dep
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:04
     */
    @Override
    public boolean saveDepV(OrgDepartmentVDO dep) {
        return false;
    }

    /**
     * 修改部门
     *
     * @param dep
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:04
     */
    @Override
    public boolean updateDepV(OrgDepartmentVDO dep) {
        return false;
    }
}
