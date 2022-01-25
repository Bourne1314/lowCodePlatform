package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.form.LoginForm;
import com.csicit.ace.common.pojo.domain.dev.DevMenuDO;
import com.csicit.ace.common.pojo.domain.dev.DevMenuRoleDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.common.utils.LocalDateTimeUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.DevUserMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.*;
import com.csicit.ace.dev.util.DevConstants;
import com.csicit.ace.dev.util.MD5Util;
import com.csicit.ace.dev.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devUserService")
public class DevUserServiceImpl extends BaseServiceImpl<DevUserMapper, DevUserDO> implements
        DevUserService {

    @Autowired
    DevUserRoleService devUserRoleService;

    @Autowired
    DevMenuRoleService devMenuRoleService;

    @Autowired
    DevMenuService devMenuService;

    @Autowired
    DevUserTokenService devUserTokenService;

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public DevUserDO getUserInfo(String id) {
        DevUserDO sysDevUserDO = getById(id);
        sysDevUserDO.setRoleIds(devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", id)).stream().map(DevUserRoleDO::getRoleId).collect(Collectors.toList()));
        return sysDevUserDO;
    }

    /**
     * 保存用户
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean saveUser(DevUserDO sysUserDO) {
        sysUserDO.setPassword(MD5Util.createMD5Str(DevConstants.DEFAULT_PASSWORD, sysUserDO.getUserName()));
//        sysUserDO.setPassword(DevConstants.DEFAULT_PASSWORD);
        sysUserDO.setCreateTime(LocalDateTime.now());
        sysUserDO.setCreateUserId(ShiroUtils.getUserinfo().getId());
        if (!save(sysUserDO)) {
            return false;
        }

        return saveUserRole(sysUserDO);
    }

    /**
     * 修改用户角色关系
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/11/27 18:03
     */
    private boolean saveUserRole(DevUserDO sysUserDO) {
        devUserRoleService.remove(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", sysUserDO.getId()));
        if (sysUserDO.getRoleIds() != null && sysUserDO.getRoleIds().size() > 0) {
            List<DevUserRoleDO> sysUserRoleDOS = new ArrayList<>(16);
            sysUserDO.getRoleIds().stream().forEach(roleId -> {
                DevUserRoleDO sysUserRoleDO = new DevUserRoleDO();
                sysUserRoleDO.setId(UuidUtils.createUUID());
                sysUserRoleDO.setRoleId(roleId);
                sysUserRoleDO.setUserId(sysUserDO.getId());
                sysUserRoleDOS.add(sysUserRoleDO);
            });
            if (!devUserRoleService.saveBatch(sysUserRoleDOS)) {
                return false;
            }
        }
        ShiroUtils.deleteCache(sysUserDO.getUserName(), false);
        return true;
    }

    /**
     * 更新用户
     *
     * @param sysUserDO
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean updateUser(DevUserDO sysUserDO) {
        sysUserDO.setUpdateTime(LocalDateTime.now());
        if (!updateById(sysUserDO)) {
            return false;
        }
        return saveUserRole(sysUserDO);
    }

    /**
     * 解锁用户
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public boolean unlockUser(String id) {
        if (this.update(new DevUserDO(), new UpdateWrapper<DevUserDO>()
                .eq("id", id).set("FAIL_TIMES", 0).set("LAST_FAIL_TIME", null).set("LOCK_STATUS", 0)
        )) {
            return true;
        }
        return false;
    }

    /**
     * 登录
     *
     * @param loginForm LoginForm
     * @return
     */
    @Override
    public R login(LoginForm loginForm) {
        String username = loginForm.getUserName();
        String password = loginForm.getPassword();
        String pwd = MD5Util.createMD5Str(password, username);
        DevUserDO sysDevUserDO = getOne(new QueryWrapper<DevUserDO>().eq("user_name", username));
        if (sysDevUserDO == null || !Objects.equals(pwd, sysDevUserDO.getPassword())) {
            if (sysDevUserDO != null) {
                /**
                 * 用户实际登录失败次数
                 */
                Integer failLoginTimes = sysDevUserDO.getFailTimes();
                failLoginTimes = failLoginTimes == null ? 1 : failLoginTimes + 1;
                /**
                 * 更新密码失败次数和时间
                 */
                sysDevUserDO.setFailTimes(failLoginTimes);
                sysDevUserDO.setLastFailTime(LocalDateTime.now());

                if (failLoginTimes >= DevConstants.MAX_FAILURE_TIMES) {
                    /**
                     * 锁定账户并登录失败次数和时间
                     */
                    sysDevUserDO.setLockStatus(1);
                    sysDevUserDO.setFailTimes(0);
                }
                updateById(sysDevUserDO);
            }
            return R.error("用户名密码错误");
        }
        // 判断用户是否启用
        if (Objects.equals(0, sysDevUserDO.getApplyStatus())) {
            return R.error("帐号被停用");
        }
        // 锁定
        if (Objects.equals(1, sysDevUserDO.getLockStatus())) {
            /**
             * 判断锁定时间
             */
            int maxResetFailureTimesTime = DevConstants.LOCK_MINTUES;
            /**
             * 获取最新一次登录失败登录时间
             */
            if (sysDevUserDO.getLastFailTime() != null) {
                long interval = LocalDateTimeUtils.getInterval(sysDevUserDO.getLastFailTime(), LocalDateTime.now());
                // 登录失败计数应在n分钟后重置为0
                if (interval >= maxResetFailureTimesTime) {
                    sysDevUserDO.setFailTimes(0);
                    // 解锁账号
                    sysDevUserDO.setLockStatus(0);
                } else {
                    return R.error("帐号已被锁定");
                }
            }
        }

        sysDevUserDO.setFailTimes(0);
        sysDevUserDO.setLoginTime(LocalDateTime.now());

        updateById(sysDevUserDO);

        //生成token，并保存到数据库
        return devUserTokenService.createToken(sysDevUserDO.getId());
    }

    /**
     * 验证用户名密码
     *
     * @param userName
     * @param password
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:29
     */
    @Override
    public boolean authenticate(String userName, String password) {
        password = MD5Util.createMD5Str(password, userName);
        int count = count(new QueryWrapper<DevUserDO>().eq("user_name", userName).eq
                ("password",
                        password));
        return count == 1;
    }

    /**
     * 更新密码
     *
     * @param userName
     * @param password
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    @Override
    public R updatePassword(String userName, String password, boolean save) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_USERNAME_OR_PASSWORD"));
        }
        DevUserDO user = this.getOne(new QueryWrapper<DevUserDO>()
                .eq("user_name", userName));
        if (user == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NOT_EXIST"));
        }
        user.setPassword(MD5Util.createMD5Str(password, userName));
        if (!updateById(user)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
    }

    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/4/22 15:30
     */
    @Override
    public List<String> queryAllPerms(String userId) {
        List<String> permissions = new ArrayList<>(16);
        devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", userId)).stream().map(DevUserRoleDO::getRoleId)
                .forEach(roleId -> {
                    devMenuRoleService.list(new QueryWrapper<DevMenuRoleDO>()
                            .eq("role_id", roleId)).stream().map(DevMenuRoleDO::getMenuId)
                            .forEach(menuId -> {
                                DevMenuDO sysMenuDO = devMenuService.getById(menuId);
                                permissions.add(sysMenuDO.getPerms());
                            });
                });
        return permissions;
    }
}
