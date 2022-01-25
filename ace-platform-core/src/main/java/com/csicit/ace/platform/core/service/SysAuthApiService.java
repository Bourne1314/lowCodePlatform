package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限api关系管理 实例对象访问接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysAuthApiService extends IService<SysAuthApiDO> {

    /**
     * 保存auth-api关系表
     *
     * @param authId 权限id
     * @param apis api资源集合
     * @author shanwj
     * @date 2019/4/18 16:58
     */
    boolean saveAuthApi(String authId, List<SysApiResourceDO> apis);

    /**
     * 把auth_api的数据缓存到 缓存数据库
     * @return 
     * @author FourLeaves
     * @date 2020/6/23 9:38
     */
    boolean initApis();

}
