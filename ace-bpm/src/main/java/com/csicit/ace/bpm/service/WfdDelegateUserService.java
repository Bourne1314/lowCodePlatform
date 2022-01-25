package com.csicit.ace.bpm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.bpm.pojo.domain.WfdDelegateUserDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/2/19 15:30
 */
@Transactional
public interface WfdDelegateUserService extends IService<WfdDelegateUserDO> {


    /**
     * 递归获取有效的受委托人主键
     * @param flowId 流程主键
     * @param userId 用户主键
     * @return
     * @author FourLeaves
     * @date 2020/2/21 16:28
     */
    String getEffectiveDelegateUserId(String flowId, String userId);



}
