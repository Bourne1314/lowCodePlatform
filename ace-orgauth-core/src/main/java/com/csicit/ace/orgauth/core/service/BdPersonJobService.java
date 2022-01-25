package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础数据-人员工作信息 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 17:27:00
 * @version V1.0
 */
@Transactional
public interface BdPersonJobService extends IBaseService<BdPersonJobDO> {
    /**
     * 保存用户职务信息
     * @param job
     * @return
     * @author xulei
     * @date 2020/6/30 10:09
     */
    boolean savePersonJob(BdPersonJobDO job);
}
