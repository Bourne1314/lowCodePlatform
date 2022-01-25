package com.csicit.ace.webservice.service;

import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.utils.server.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 集团管理 实例对象访问接口
 *
 * @author yansiyang
 * @versiond V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface OrgGroupService extends IService<OrgGroupDO>, BaseOrgService {

    /**
     * 修改集团
     *
     * @param group 集团对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:53
     */
    boolean updateGroup(OrgGroupDO group);

    /**
     * 保存集团
     *
     * @param group 集团对象
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:51
     */
    boolean saveGroup(OrgGroupDO group);

    /**
     * 删除集团
     *
     * @param params 集团主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 14:56
     */
     R deleteGroup(Map<String, Object> params);


}
