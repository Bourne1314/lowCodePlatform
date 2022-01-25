package com.csicit.ace.dev.service;

import com.csicit.ace.common.form.LoginForm;
import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户管理 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface DevUserService extends IBaseService<DevUserDO> {
    /**
     * 获取用户信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    DevUserDO getUserInfo(String id);
    /**
     * 保存用户
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean saveUser(DevUserDO sysUserDO);
    /**
     * 更新用户
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean updateUser(DevUserDO sysUserDO);
    /**
     * 解锁用户
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    boolean unlockUser(String id);

    /**
     * 登录
     * @param loginForm
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:50
     */
    R login(LoginForm loginForm);
    /**
     * 验证用户名密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:51
     */
    boolean authenticate(String userName, String password);
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



    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(String userId);


}
