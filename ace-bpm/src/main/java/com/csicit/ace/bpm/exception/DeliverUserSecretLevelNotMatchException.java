package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.domain.SysUserDO;

import java.util.Map;

/**
 * @author JonnyJiang
 * @date 2020/7/20 17:49
 */
public class DeliverUserSecretLevelNotMatchException extends BpmSystemException {
    private final Integer secretLevel;
    private final SysUserDO sysUser;

    public DeliverUserSecretLevelNotMatchException(Integer secretLevel, SysUserDO sysUser) {
        super(BpmErrorCode.S00060, LocaleUtils.getDeliverUserSecretLevelNotMatch(sysUser.getRealName()));
        this.secretLevel = secretLevel;
        this.sysUser = sysUser;
    }

    @Override
    protected void onLogging(Map<String, Object> args) {
        args.put("secretLevel", secretLevel);
        args.put("userId", sysUser.getId());
        args.put("realName", sysUser.getRealName());
    }
}
