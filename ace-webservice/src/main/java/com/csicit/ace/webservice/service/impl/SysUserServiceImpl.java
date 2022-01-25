package com.csicit.ace.webservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.PinyinUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.redis.RedisUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.webservice.mapper.SysUserMapper;
import com.csicit.ace.webservice.service.*;
import com.csicit.ace.webservice.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户管理 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 11:47 2019/3/29
 */
@Service("sysUserServiceW")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    SysConfigService sysConfigService;



    /**
     * 根据登录名生成排序号
     *
     * @param userName
     * @return
     * @author yansiyang
     * @date 2019/7/9 8:23
     */
    public Integer createSortIndexByUserName(String userName) {
        char[] chars = userName.toCharArray();
        Integer sortIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            int ascInt = (int) chars[i] - 48;
            int baseInt = (int) Math.pow(2, i + 5);
            sortIndex += ascInt * baseInt;
        }
        while (true) {
            int count = this.count(new QueryWrapper<SysUserDO>().eq
                    ("sort_index", sortIndex));
            if (count == 0) {
                break;
            } else {
                sortIndex += 1;
            }
        }
        return sortIndex;
    }


    /**
     * 校验用户名 只可以包含数字 大小写字符 下划线
     * 用户名的长度在3-16之间
     *
     * @param userName
     * @return
     * @author yansiyang
     * @date 2019/7/9 8:27
     */
    public void validateUserName(String userName) {
        if (userName == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_LENGTH_ERROR"));
        }
        if (userName.length() < 3 || userName.length() > 21) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_LENGTH_ERROR"));
        }
        String regex = "^[a-z0-9A-Z_]+$";
        if (!userName.matches(regex)) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NAME_VALID"));
        }
        SysUserDO user = getOne(new QueryWrapper<SysUserDO>().eq("user_name", userName).or().eq("staff_no", userName));
        if (user != null) {
            removeUser(user.getId());
        }
    }

    private void removeUser(String id ) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        removeUsers(ids);
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return com.csicit.ace.common.utils.server.R
     * @author zuogang
     * @date 2019/4/22 15:28
     */
    @Override
    public R saveUser(SysUserDO user) {

        validateUserName(user.getUserName());
        if (user.getSortIndex() == null || user.getSortIndex() < 1) {
            user.setSortIndex(createSortIndexByUserName(user.getUserName()));
        } else {
            int count = this.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).eq("sort_index", user
                    .getSortIndex()));
            if (count > 0) {
                user.setSortIndex(createSortIndexByUserName(user.getUserName()));
            }
        }
        /**
         * https 传输明文密码
         */
        String password = user.getPassword();
        /**
         * 转化汉语拼音
         */
        String realName = user.getRealName();
        if (StringUtils.isNotBlank(realName)) {
            user.setPinyin(PinyinUtils.toHanyuPinyin(realName));
        } else {
            user.setRealName(user.getUserName());
        }
        user.setCreateTime(LocalDateTime.now());
        user.setCreateUser("webservice");
        // 判断用户类型是否为空
        Integer userType = user.getUserType();
        if (userType == null || userType == 0 || Objects.equals(userType, new Integer(0))) {
            user.setUserType(3);
        }
        // 判断用户的人员ID是否为空格
        if (!this.save(user)) {
            return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        /**
         * 密码加密
         */
        if (StringUtils.isNotBlank(password) && password != null) {
            saveUserToUpdatePassword(user.getUserName(), password);
//            if (sysAuditLogService.saveLog("保存用户",user, "主数据推送")) {
//                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//            }
            return R.ok();
        } else {
            // 新建用户密码为空 采用默认密码
            password = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name", "defaultPassword"))
                    .getValue();
            if (StringUtils.isNotBlank(password) && password != null) {
                saveUserToUpdatePassword(user.getUserName(), decryptPassword(user.getUserName(), password, true));
//                if (sysAuditLogService.saveLog("保存用户",user, "主数据推送")) {
//                    return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//                }
                return R.ok();
            }

        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    private String decryptPassword(String userName, String cipherText, boolean save) {
        String password = "";
        try {
            password = GMBaseUtil.decryptString(cipherText);
            // 默认密码初始化时被加密 解密后第7位及之后为正确的默认密码
            password = password.substring(6);
            if (save) {
                return password;
            }
            password = GMBaseUtil.pwToCipherPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        return password;
    }

    /**
     * 逻辑删除用户
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/10/23 16:30
     */
    @Override
    public boolean removeUsers(List<String> ids) {
        if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            return false;
        }
        if (update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in("id", ids)
                .set("sort_index", -1)
                .setSql("person_doc_id=null")
                .setSql("staff_no=CONCAT(CONCAT('del-', SUBSTR(id,1,4)),staff_no)")
                .setSql("user_name=CONCAT(CONCAT('del-', SUBSTR(id,1,4)),user_name)")
                .set("is_delete", 1).isNotNull("staff_no")) || update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in
                ("id", ids)
                .set("sort_index", -1)
                .setSql("person_doc_id=null")
                .setSql("staff_no=CONCAT(CONCAT('del-', SUBSTR(id,1,8)),staff_no)")
                .setSql("user_name=CONCAT(CONCAT('del-', SUBSTR(id,1,4)),user_name)")
                .set("is_delete", 1).isNull("staff_no"))) {
            return true;

        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 保存用户的时候 调用的更新密码   避免首次登录相关问题
     *
     * @param userName
     * @param password
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/6/12 16:21
     */
    public R saveUserToUpdatePassword(String userName, String password) {
        return updatePassword(userName, password, true);
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

        SysUserDO user = this.getOne(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                .eq("user_name", userName));
        if (user == null) {
            throw new RException(InternationUtils.getInternationalMsg("USER_NOT_EXIST"));
        }
        String userId = user.getId();

//        SysPasswordPolicyDO passwordPolicy = sysPasswordPolicyService.getPasswordPolicy();

        /**
         * 校验密码长度
         * 非密、秘密至少8位
         * 机密至少10位
         */
//        int length = passwordPolicy.getLen() > 8 ? passwordPolicy.getLen() : 8;
//        /**
//         * 校验密码必须同时包含大写字母、小写字母、数字、特殊符号
//         */
//        String pwPattern = String.format("^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)" +
//                "[a-zA-Z0-9\\W]{%d,}$", length);
//        if (!password.matches(pwPattern)) {
//            throw new RException(String.format(InternationUtils.getInternationalMsg("ERROR_PASSWORD_LENGTH"), length));
//        }

        /**
         * 当前密码使用n天后才可以修改
         */
        // 判断用户是否时候首次登陆
        // 非首次登陆才判断密码修改期限
//        boolean firstLogin = user.getFirstLogin() == 1;
//        if (!firstLogin) {
//            int minTime = passwordPolicy.getUseMinTime();
//            LocalDateTime updateTime = user.getPasswordUpdateTime();
//            if (updateTime != null) {
//                long minutes = LocalDateTimeUtils.getInterval(updateTime, LocalDateTime.now());
//                if (minutes < minTime * 24 * 60) {
//                    throw new RException(String.format(InternationUtils.getInternationalMsg
//                            ("PASSOWRD_BEFORE_LAST_MODIFY_ERROR"), minTime));
//                }
//            }
//        }

        /**
         * 加密保存密码
         */
        try {
            password = GMBaseUtil.pwToCipherPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }

        /**
         * 不允许用户修改的密码与前几次相同
         */
//        int changeNum = passwordPolicy.getChangeNum();
//        /**
//         * 与上次比较
//         */
//        int count = this.count(new QueryWrapper<SysUserDO>().eq("id", userId).eq("password", password));
//        if (count > 0) {
//            throw new RException(String.format(InternationUtils.getInternationalMsg("NEW_PASSWORD_SAME_ERROR"),
//                    changeNum));
//        } else {
//            /**
//             * 与前几次比较
//             */
//            List<SysUserPasswordHistoryDO> list = sysUserPasswordHistoryService.list(new
//                    QueryWrapper<SysUserPasswordHistoryDO>()
//                    .eq("user_id", userId).orderByDesc("create_time"));
//            int size = (changeNum - 1) > list.size() ? list.size() : (changeNum - 1);
//            for (int i = 0; i < size; i++) {
//                if (Objects.equals(list.get(i).getPassword(), password)) {
//                    throw new RException(String.format(InternationUtils.getInternationalMsg
//                            ("NEW_PASSWORD_SAME_ERROR"), changeNum));
//                }
//            }
//        }
//
//        /**
//         * 保存老密码
//         */
//        if (baseMapper.saveOldPassword(UuidUtil.createUUID(), userId) != 1) {
//            throw new RException(InternationUtils.getInternationalMsg("SAVE_OLD_PASSWORD_ERROR"));
//        }
        boolean result = this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("password", password).set("password_update_time", LocalDateTime.now()));
        if (!result) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        return R.ok();
        // 首次登录修改密码
//        if (firstLogin) {
//            // 取消 首次登录 状态
//            // 0 代表 非首次登陆
//            if (!save) {
//                result = this.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
//                        .eq("id", userId).set("is_first_login", 0));
//                if (!result) {
//                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//                }
//            }
//            // 登陆页面修改密码 此时无token
//            SysAuditLogDO sysAuditLogDO = new SysAuditLogDO();
//            sysAuditLogDO.setOpUsername(user.getUserName());
//            sysAuditLogDO.setOpName(user.getRealName());
//            sysAuditLogDO.setOpTime(LocalDateTime.now());
//            sysAuditLogDO.setOpContent("更新密码:" + user.getUserName());
//            sysAuditLogDO.setIpAddress(IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
//            String id = UuidUtils.createUUID();
//            sysAuditLogDO.setId(id);
//            try {
//                sysAuditLogDO.setSign(GMBaseUtil.getSign(user.getUserName() + id + IpUtils
//                        .getIpAddr(HttpContextUtils.getHttpServletRequest())));
//            } catch (Exception e) {
//                throw new RException(InternationUtils.getInternationalMsg("GRANT_PRIVILEGE"));
//            }
////            if (sysAuditLogService.save(sysAuditLogDO)) {
////                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
////                        LocalDateTime.now());
////            }
//            return R.ok();
//        } else {
////            if (sysAuditLogService.saveLog("更新密码" , user.getUserName(), "主数据推送")) {
////                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("updatePasswordTime",
////                        LocalDateTime.now());
////            }
//            return R.ok();
//        }
        //throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }


}