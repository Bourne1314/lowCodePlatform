package com.csicit.ace.platform.core.service;


import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 密码策略 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:09
 */
@Transactional
public interface SysPasswordPolicyService extends IBaseService<SysPasswordPolicyDO>{

    /**
     * @Author yansiyang
     * @Description 获取非密用户专属的密码策略
     * @Date 10:44 2019/4/9
     * @param userId 用户主键
     * @return
     */
    SysPasswordPolicyDO getPasswordPolicy(String userId);

    /**
     * @Author yansiyang
     * @Description 获取默认的密码策略
     * @Date 10:45 2019/4/9
     * @param
     * @return
     */
    SysPasswordPolicyDO getPasswordPolicy();

    /**
     * 保存密码策略
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(SysPasswordPolicyDO instance);

    /**
     * 更新密码策略
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(SysPasswordPolicyDO instance);

    /**
     * 删除密码策略
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(String[] ids);

}