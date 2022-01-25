package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean savePersonJob(BdPersonJobDO job);

    /**
     * 保存用户职务信息
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean deletePersonJob(List<String> ids);

    /**
     * 更新用户职务信息
     * @param job
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean updatePersonJob(BdPersonJobDO job);

    /**
     * 获取用户职务信息
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    List<BdPersonJobDO> getJobs(String userId);

    /**
     * 设置主职务
     * @param personDocId
     * @param personJobId
     * @return 
     * @author yansiyang
     * @date 2019/7/16 8:49
     */
    boolean setMainJob(String personDocId, String personJobId);
}
