package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础数据-人员工作信息 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:00
 */
@Transactional
public interface BdPersonJobService extends IService<BdPersonJobDO> {

    /**
     * 保存用户职务信息
     *
     * @param job
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean savePersonJob(BdPersonJobDO job);

    /**
     * 保存用户职务信息
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean deletePersonJob(List<String> ids);

    /**
     * 更新用户职务信息
     *
     * @param job
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    boolean updatePersonJob(BdPersonJobDO job);

    /**
     * 获取用户职务信息
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/7/15 17:50
     */
    List<BdPersonJobDO> getJobs(String userId);

    /**
     * 设置主职务
     *
     * @param personDocId
     * @param personJobId
     * @return
     * @author yansiyang
     * @date 2019/7/16 8:49
     */
    boolean setMainJob(String personDocId, String personJobId);
}
