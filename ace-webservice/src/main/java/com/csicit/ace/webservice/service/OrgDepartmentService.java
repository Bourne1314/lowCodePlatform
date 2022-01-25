package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.webservice.service.BaseOrgService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 组织-部门 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */
@Transactional
public interface OrgDepartmentService extends IService<OrgDepartmentDO>, BaseOrgService {
    /**
     * 移动业务单元
     *
     * @param mvToTop     是否设置为顶级部门
     * @param id          要移动的部门主键
     * @param orgId       业务单元主键
     * @param targetDepId 目标部门主键
     * @return
     * @author yansiyang
     * @date 2019/11/21 13:59
     */
    boolean mvDep(boolean mvToTop, String id, String orgId, String targetDepId);

    /**
     * 保存部门
     *
     * @param dep
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean saveDep(OrgDepartmentDO dep);

    /**
     * 修改部门
     *
     * @param dep
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean updateDep(OrgDepartmentDO dep);

    /**
     * 删除部门
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    R deleteDep(Map<String, Object> map);
}
