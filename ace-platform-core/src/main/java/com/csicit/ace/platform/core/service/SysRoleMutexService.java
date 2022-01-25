package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色互斥关系管理 实例对象访问接口
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysRoleMutexService extends IService<SysRoleMutexDO> {

    /**
     * 保存角色互斥数据
     *
     * @param id
     * @param mids
     * @return
     * @author zuogang
     * @date 2019/4/22 15:23
     */
    boolean saveMutexRoles(String id, List<String> mids);
}
