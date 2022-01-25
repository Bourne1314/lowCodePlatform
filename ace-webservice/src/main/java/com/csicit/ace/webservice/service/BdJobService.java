package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 职务称谓表 实例对象访问接口
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 9:02
 */
@Transactional
public interface BdJobService extends IService<BdJobDO> {

    /**
     *  保存职务称谓
     * @param jobDO
     * @return boolean
     * @author wangzimin
     * @date 2019/9/24 11:27
     */
    boolean saveJob(BdJobDO jobDO);

    /**
     * 更新职务称谓
     * @param jobDO
     * @return
     * @author wangzimin
     * @date 2019/9/24 11:28
     */
    boolean updateJob(BdJobDO jobDO);

    /**
     * 删除职务称谓
     * @param ids
     * @return
     * @author wangzimin
     * @date 2019/9/24 11:29
     */
    boolean deleteJob(String[] ids);
}
