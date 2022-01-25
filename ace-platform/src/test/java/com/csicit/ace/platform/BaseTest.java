package com.csicit.ace.platform;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.cipher.SM4Util;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.redis.RedisUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.mock.TestUtil;
import com.csicit.ace.platform.core.service.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
public abstract class BaseTest {
    @Autowired
    public SysRoleService sysRoleService;
    @Autowired
    public SysUserAdminOrgService sysUserAdminOrgService;
    @Autowired
    public SysAuthScopeAppService sysAuthScopeAppService;
    @Autowired
    public SysAuthScopeOrgService sysAuthScopeOrgService;
    @Autowired
    public SysAuthScopeUserGroupService sysAuthScopeUserGroupService;
    @Autowired
    public OrgOrganizationTypeService orgOrganizationTypeService;
    @Autowired
    public OrgOrganizationVTypeService orgOrganizationVTypeService;
    @Autowired
    public BdPersonIdTypeService bdPersonIdTypeService;
    @Autowired
    public SysUserService sysUserService;
    @Autowired
    public RedisUtils redisUtils;
    @Autowired
    public BdPersonDocService bdPersonDocService;
    @Autowired
    public SysUserRoleLvService sysUserRoleLvService;
    @Autowired
    public BdPersonJobService bdPersonJobService;
    @Autowired
    public OrgDepartmentService orgDepartmentService;
    @Autowired
    public SysUserRoleVService sysUserRoleVService;
    @Autowired
    public OrgGroupService orgGroupService;
    @Autowired
    public SysApiMixService sysApiMixService;
    @Autowired
    public OrgOrganizationService orgOrganizationService;
    @Autowired
    public OrgOrganizationVService orgOrganizationVService;
    @Autowired
    public SysGroupAppService sysGroupAppService;
    @Autowired
    public SysAuthRoleLvService sysAuthRoleLvService;
    @Autowired
    public SysAuthMixService sysAuthMixService;
    @Autowired
    public SysAuthService sysAuthService;
    @Autowired
    public SysApiResourceService sysApiResourceService;
    @Autowired
    public SysAuthApiService sysAuthApiService;
    @Autowired
    public SysMenuService sysMenuService;
    @Autowired
    public SysAuthUserVService sysAuthUserVService;
    @Autowired
    public SysAuthUserLvService sysAuthUserLvService;
    @Autowired
    public SysAuthUserService sysAuthUserService;
    @Autowired
    public SysUserRoleService sysUserRoleService;
    @Autowired
    public SysAuthRoleService sysAuthRoleService;
    @Autowired
    public SysAuthRoleVService sysAuthRoleVService;
    @Autowired
    public SysWaitGrantAuthService sysWaitGrantAuthService;
    @Autowired
    public SysWaitGrantUserService sysWaitGrantUserService;
    @Autowired
    public SysDictService sysDictService;
    @Autowired
    public SysDictValueService sysDictValueService;
    @Autowired
    public SysUserGroupService sysUserGroupService;
    @Autowired
    public SysUserGroupUserService sysUserGroupUserService;
    @Autowired
    public SysUserAdminUserGroupService sysUserAdminUserGroupService;

    @Before
    public void setUp() throws Exception {
        System.out.println("测试开始");
        TestUtil.executeSql("test_add_data.sql");
        // 模拟登陆信息
        String token = login("admin");
        SecurityUtils.TEST_TOKEN = token;
    }

    @After
    public void down() {
        System.out.println("测试结束");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        TestUtil.executeSql("test_del_data.sql");
    }


    public String login(String userName) {
        SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>()
                .eq("user_name", userName).eq("is_delete", 0));


        String token = null;
        try {
            token = SM4Util.getToken(user.getId(), userName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_GET_TOKEN"));
        }


        /**
         * 加载用户信息、角色信息、权限列表到缓存数据库
         */
        cacheUserInfos(token, user.getId(), true, 0, null);

        return token;
    }

    /**
     * 缓存用户信息  角色信息等
     *
     * @param token
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/5/17 10:55
     */
    private void cacheUserInfos(String token, String userId, boolean ok, int code, String msg) {
        String defaultTokenExpireDay = redisUtils.get("defaultTokenExpireDay");
        int seconds = Integer.parseInt(defaultTokenExpireDay) * 60 * 60 * 24;
        SysUserDO user = sysUserService.getById(userId);
        redisUtils.hset(token, "user", JSONObject.toJSON(user), seconds);
        redisUtils.set(userId, token, RedisUtils.NOT_EXPIRE);
        redisUtils.set(token + "userid", userId, seconds);

        /**
         * 主职部门
         */
        if (StringUtils.isNotBlank(user.getPersonDocId())) {
            BdPersonDocDO person = bdPersonDocService.getById(user.getPersonDocId());
            if (person != null) {
                BdPersonJobDO mainJob = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                        person.getId()).eq("is_main_job", 1));
                if (mainJob != null) {
                    OrgDepartmentDO dep = orgDepartmentService.getById(mainJob.getDepartmentId());
                    redisUtils.hset(token, "department", JSONObject.toJSON(dep), seconds);
                }
            }
        }
        /**
         * 用户角色ID列表
         */
        List<String> roleIds = new ArrayList<>();
        List<SysUserRoleLvDO> sysUserRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("user_id", userId).eq("is_latest", 1));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysUserRoleLvDOS)) {
            List<String> lvIds = sysUserRoleLvDOS.stream().map(SysUserRoleLvDO::getId).collect(Collectors.toList());
            List<SysUserRoleVDO> sysUserRoleVDOS = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                    .in("lv_id", lvIds));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysUserRoleVDOS)) {
                roleIds.addAll(sysUserRoleVDOS.stream().map(SysUserRoleVDO::getRoleId).collect(Collectors.toList
                        ()));
            }
        }

        redisUtils.hset(token, "roleIds", JSONObject.toJSON(roleIds), seconds);

        // 判断用户是否是租户 集团 或 应用管理员
        String[] roleIdStrs = {"admin", "sec", "auditor", "groupadmin", "groupsec", "groupauditor", "appadmin",
                "appsec",
                "appauditor"};
        boolean isAdmin = roleIds.stream().anyMatch(roleId -> Arrays.asList(roleIdStrs).contains(roleId));

        /**
         * 统计用户可管理集团的数量
         */
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(user.getId());
        redisUtils.hset(token, "groups", JSONObject.toJSON(groups), seconds);
        redisUtils.hset(token, "isAdmin", isAdmin ? 1 : 0, seconds);
        /**
         * 用户权限标识列表  API
         */
        List<String> apis = sysApiMixService.list(new QueryWrapper<SysApiMixDO>().eq("user_id", userId)).stream().map
                (SysApiMixDO::getApiId).collect(Collectors.toList());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(apis)) {
            redisUtils.hset(token, "apis", JSONObject.toJSON(apis), seconds);
        }
    }

}
