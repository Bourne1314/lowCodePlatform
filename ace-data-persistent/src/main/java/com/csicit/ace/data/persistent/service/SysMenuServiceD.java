package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.MenuDetail;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单 实例对象访问接口
 *
 * @author yansiyang
 * @version v1.0
 * @date 2019-04-11 14:18:16
 */
@Transactional
public interface SysMenuServiceD extends IBaseService<SysMenuDO> {

    /**
     * 应用升级时，菜单更新
     *
     * @param menuDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 15:18
     */
    boolean menuUpdate(List<MenuDetail> menuDetails, String appId);

}
