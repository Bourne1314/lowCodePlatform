package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysPasswordPolicyDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.SysPasswordPolicyMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysPasswordPolicyService;
import com.csicit.ace.platform.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码策略 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/3 17:45
 */
@Service("systemPasswordPolicyService")
public class SysPasswordPolicyServiceImpl
        extends BaseServiceImpl<SysPasswordPolicyMapper, SysPasswordPolicyDO>
        implements SysPasswordPolicyService {

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    CacheUtil cacheUtil;
    @Autowired
    SysUserService sysUserService;

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 获取用户专属密码策略
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/4/19 10:36
     */
    @Override
    public SysPasswordPolicyDO getPasswordPolicy(String userId) {
        /**
         * 用户主键为空则获取默认密码策略
         */
        if (StringUtils.isBlank(userId)) {
            return getPasswordPolicy();
        } else {
            String token = securityUtils.getToken();
            /**
             * 先从Redis中获取
             */
            SysPasswordPolicyDO passwordPolicy = (SysPasswordPolicyDO) cacheUtil.hget(token, "passwordPolicy");
            if (passwordPolicy == null) {
                /**
                 * redis是空的，则从数据库获取
                 *
                 * 先获取当前用户策略ID
                 */
                String pswPolicyId = securityUtils.getCurrentUser().getPswPolicyId();
                /**
                 * 当前用户策略ID为空，则从数据库获取
                 */
                if (StringUtils.isBlank(pswPolicyId)) {
                    SysUserDO user = sysUserService.getById(userId);
                    pswPolicyId = user.getPswPolicyId();
                    /**
                     * 数据库中密码策略为空，则获取默认密码策略
                     */
                    if (StringUtils.isBlank(pswPolicyId)) {
                        return getPasswordPolicy();
                    } else {
                        passwordPolicy = this.getById(pswPolicyId);
                        if (passwordPolicy == null) {
                            return getPasswordPolicy();
                        } else {
                            cacheUtil.set(token+"passwordPolicy",  passwordPolicy, CacheUtil.NOT_EXPIRE);
                            return passwordPolicy;
                        }
                    }
                } else {
                    passwordPolicy = this.getById(pswPolicyId);
                    if (passwordPolicy == null) {
                        return getPasswordPolicy();
                    } else {
                        cacheUtil.set(token+"passwordPolicy" , passwordPolicy, CacheUtil.NOT_EXPIRE);
                        return passwordPolicy;
                    }
                }
            }
            return passwordPolicy;
        }
    }

    /**
     * 获取默认密码策略
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/19 10:36
     */
    @Override
    public SysPasswordPolicyDO getPasswordPolicy() {
        SysPasswordPolicyDO pswPolicy = this.getOne(new QueryWrapper<SysPasswordPolicyDO>()
                .eq("id", "bdpolicy"));
        return pswPolicy;
    }

    @Override
    public R insert(SysPasswordPolicyDO instance) {
        /**
         * 只允许非密单位维护密码策略
         */
        if (save(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存密码策略", "保存密码策略："+instance.getRemark())) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(SysPasswordPolicyDO instance) {
        /**
         * 只允许非密单位维护密码策略
         */
        if (updateById(instance)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新密码策略","更新密码策略："+ instance
                    .getRemark())) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R delete(String[] ids) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        List<SysPasswordPolicyDO> list = list(new QueryWrapper<SysPasswordPolicyDO>()
                .and(ids == null || ids.length == 0, i -> i.eq("1", "2"))
                .in("ids", ids).select("remark"));
        if (removeByIds(Arrays.asList(ids))) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除密码策略", "删除密码策略："+list.stream().map
                    (SysPasswordPolicyDO::getRemark)
                    .collect(Collectors.toList()))) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
