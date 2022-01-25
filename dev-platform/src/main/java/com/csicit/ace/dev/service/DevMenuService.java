package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.DevMenuDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevMenuService extends IBaseService<DevMenuDO> {
    /**
     * 删除菜单
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean deleteMenu(List<String> ids);

    /**
     * 获取树结构的菜单 左侧菜单
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:31
     */
    List<DevMenuDO> listSideTree(String userId);
}
