package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysComponentRegisterDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组件注册 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Transactional
public interface SysComponentRegisterService extends IBaseService<SysComponentRegisterDO> {

    /**
     * 新增组件注册信息
     */
    boolean addComponent(SysComponentRegisterDO sysComponentRegisterDO);

    /**
     * 修改组件注册信息
     */
    boolean editComponent(SysComponentRegisterDO sysComponentRegisterDO);

    /**
     * 删除组件注册信息
     */
    boolean delComponent(List<String> ids);
}
