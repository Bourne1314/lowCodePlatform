package com.csicit.ace.webservice.service;

import com.csicit.ace.common.pojo.domain.OrgDepartmentVDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织-部门版本 实例对象访问接口
 *
 * @author yansiyang
 * @date 2019-04-15 15:20:43
 * @version V1.0
 */
@Transactional
public interface OrgDepartmentVService extends IService<OrgDepartmentVDO> {

    /**
     * 保存部门
     * @param dep
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean saveDepV(OrgDepartmentVDO dep);

    /**
     * 修改部门
     * @param dep
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean updateDepV(OrgDepartmentVDO dep);
}
