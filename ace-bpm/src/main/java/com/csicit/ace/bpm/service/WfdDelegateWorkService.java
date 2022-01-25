package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateWorkDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/25 10:00
 */
@Transactional
public interface WfdDelegateWorkService extends IService<WfdDelegateWorkDO> {
    /**
     * 获取委托的任务列表
     * @param userId
     * @return 
     * @author FourLeaves
     * @date 2020/2/25 10:18
     */
    List<WfdDelegateWorkDO> getDelegateWorkByUserId(String userId);

    /**
     * 获取委托的任务列表分页
     * @param current
     * @param size
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/2/25 10:18
     */
    List getDelegateWorkByUserId(int current, int size, String userId);

    /**
     * 填充委托工作
     *
     * @return 
     * @author FourLeaves
     * @date 2020/5/25 17:13
     */
    void fillDelegateWork();
}
