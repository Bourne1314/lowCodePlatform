package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户管理 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Transactional
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 删除用户
     * @param ids
     * @return
     */
    boolean removeUsers(List<String> ids);
    /**
     * 保存用户
     *
     * @param user
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R saveUser(SysUserDO user);

    /**
     * 更新密码
     *
     * @param userName 用户名
     * @param password 密码
     * @param save     是否为保存用户时调用的方法
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:51
     */
    R updatePassword(String userName, String password, boolean save);



}
