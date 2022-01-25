package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfdFlowInitiateAuthDO;
import org.springframework.transaction.annotation.Transactional;
import com.csicit.ace.dbplus.service.IBaseService;

/**
 * 有权发起流程的用户 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 11:46:06
 */
@Transactional
public interface WfdFlowInitiateAuthService extends IBaseService<WfdFlowInitiateAuthDO> {
}
