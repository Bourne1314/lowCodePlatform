package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysMenuMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuService {


    @Autowired
    AceSqlUtils aceSqlUtils;
    /**
     * 是否开启MongoDB记录运行日志
     * 若未开启，前台不可以展示相关菜单
     */
    @Value("${ace.config.openMongoDB:false}")
    private boolean openMongoDB;

    /**
     * MongoDB运行日志菜单主键
     */
    private final static String[] mongoLogIds = {"99ea2ad7ea4a4e54acdaff4c84bb8371",
            "99ea2ad7ea4a4e54acdaff4c84bb8370"};
    private final static String[] nacosMenuIds = {"e3b15ac90286450fbe5a721c9f542344",
            "12315ac90286450fbe5a721c9f542344", "23315ac90286450fbe5a721c9f542344", "34415ac90286450fbe5a721c9f542344"};

    private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    SysAppMenuDisplayService sysAppMenuDisplayService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Override
    public R delete(Map<String, Object> params) {
        // 是否删除子节点
        boolean deleteSons = (boolean) params.get("deleteSons");
        List<String> ids = (ArrayList) params.get("ids");
        List<SysMenuDO> list = list(new QueryWrapper<SysMenuDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids)
                .select("id", "name", "sort_path", "app_id"));
        boolean result = false;
        if (!deleteSons) {
            int count = count(new QueryWrapper<SysMenuDO>()
                    .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                    .in("parent_id", ids));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("DELETE_ERROR_FOR_EXIST_SON"));
            } else {
                removeByIds(ids);
            }
        } else {
            // 通过sort_path删除子节点
            for (int i = 0; i < list.size(); i++) {
                SysMenuDO menu = list.get(i);
                String sortPath = menu.getSortPath();
                if (StringUtils.isNotBlank(sortPath)) {
                    remove(new QueryWrapper<SysMenuDO>().eq("app_id", menu.getAppId()).likeRight("sort_path",
                            sortPath));
                }
            }
        }
        // 判断删除是否成功
        int count = count(new QueryWrapper<SysMenuDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2")).in("id", ids));
        if (count == 0) {
            result = true;
        }
        if (result) {
            String groupId = sysGroupAppService.getById(list.get(0).getAppId()).getGroupId();
            String appId = null;
            String roleId = securityUtils.getRoleIds().get(0);
            if (Objects.equals(roleId, "appadmin")) {
                appId = list.get(0).getAppId();
                groupId = sysGroupAppService.getById(list.get(0).getAppId()).getGroupId();
            }
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除菜单","删除菜单："+ list.parallelStream()
                    .map
                            (SysMenuDO::getName)
                    .collect(Collectors.toList()), groupId, appId)) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        } else {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
    }

    /**
     * 保存菜单
     *
     * @param menu
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:40
     */
    @Override
    public boolean saveMenu(SysMenuDO menu) {
        String parentId = menu.getParentId();
        String rootId = "0";
        String sortPath;
        if (StringUtils.isBlank(parentId)) {
            return false;
        } else if (Objects.equals(rootId, parentId)) {
            menu.setParentName("一级菜单");
            sortPath = SortPathUtils.getSortPath("", menu.getSortIndex());
        } else {
            SysMenuDO parentMenu = this.getById(parentId);
            menu.setParentName(parentMenu.getName());
            sortPath = SortPathUtils.getSortPath(parentMenu
                    .getSortPath(), menu.getSortIndex());
        }

        //判断菜单类型,菜单的路由不可以为空
        if (menu.getType() == 0) {
            menu.setUrl("");
        } else if (menu.getType() == 1) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_MENU_URL"));
            }
        }

        aceSqlUtils.validateTreeTableWithUnique("sys_menu", parentId, menu.getSortIndex(), sortPath, "app_id", menu
                .getAppId());
        menu.setId(UuidUtils.createUUID());
        menu.setParentId(parentId);
        menu.setSortPath(sortPath);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        menu.setCreateUser(securityUtils.getCurrentUserId());

        if (Objects.equals(1, menu.getAddRelationFlag())) {
            // 创建自动关联权限
            menu.setAuthId(menu.getId());
            /**
             * 保存至 权限列表
             */
            saveMenuAuth(menu, true);

        }
        if (!this.save(menu)) {
            return false;
        }
        String groupId = sysGroupAppService.getById(menu.getAppId()).getGroupId();
        String appId = null;
        String roleId = securityUtils.getRoleIds().get(0);
        if (Objects.equals(roleId, "appadmin")) {
            appId = menu.getAppId();
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存菜单", "保存菜单："+menu.getName(), groupId,
                appId)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        return true;
    }

    /**
     * 解析URL获取权限标识
     *
     * @param url
     * @return
     * @author yansiyang
     * @date 2019/6/5 8:19
     */
    private String getAuthCode(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String[] strs = url.split("/");
        String authCode = "";
        StringJoiner stringJoiner = new StringJoiner(".");
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if (StringUtils.isNotBlank(str) && !Objects.equals("views", str)) {
                stringJoiner.add(str);
            }
        }
        authCode = authCode + "." + stringJoiner.toString();
        return StringUtils.isNotBlank(authCode) ? authCode.substring(1) : null;
    }


    /**
     * 将菜单保存至 权限列表
     *
     * @param menu
     * @param save 是否保存
     * @return
     * @author yansiyang
     * @date 2019/5/15 10:46
     */
    private void saveMenuAuth(SysMenuDO menu, boolean save) {
        SysAuthDO auth = JsonUtils.castObject(menu, SysAuthDO.class);
        if (!Objects.equals(menu.getParentId(), "0")) {
            if (StringUtils.isNotBlank(getById(menu.getParentId()).getAuthId())) {
                auth.setParentId(getById(menu.getParentId()).getAuthId());
            } else {
                if (sysAuthService.getById(menu.getParentId()) != null) {
                    auth.setParentId(menu.getParentId());
                } else {
                    auth.setParentId("0");
                }
            }
        } else {
            auth.setParentId("0");
        }
        // 创建权限标识
        auth.setCode(getAuthCode(menu.getUrl()));

        // 判断权限序号是否已存在
        List<Integer> sortIndexList = sysAuthService.list(new QueryWrapper<SysAuthDO>().eq("app_id", menu.getAppId())
                .eq("parent_id", auth.getParentId())).stream().map(SysAuthDO::getSortIndex)
                .collect(Collectors.toList());

        if (sortIndexList.contains(auth.getSortIndex())) {
            // 序号已存在，需重新赋予序号
            for (int i = 1; i < 10000; i++) {
                if (!sortIndexList.contains(i)) {
                    auth.setSortIndex(i);
                    break;
                }
            }
        }
        // 序号不存在，不需改变

        if (save) {
            if (!sysAuthService.saveAuth(auth)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
//        else {
//            if (!sysAuthService.updateAuth(auth)) {
//                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
//            }
//        }
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:40
     */
    @Override
    public boolean updateMenu(SysMenuDO menu) {

        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String parentId = menu.getParentId();
        SysMenuDO oldMenu = getById(menu.getId());
        String sortPath = "";
        String result = aceSqlUtils.verifyParentIdWithUnique("sys_menu", parentId, menu.getId(), menu.getSortIndex(),
                "app_id", menu.getAppId());
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            menu.setSortPath(SortPathUtils.getSortPath("", menu.getSortIndex()));
            menu.setParentName("一级菜单");
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            SysMenuDO parent = this.getById(parentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            menu.setParentName(parent.getName());
            sortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), menu.getSortIndex());
            menu.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            return false;
        }

        //index及父节点未发生变化 则不需要变更子节点
        if (!Objects.equals("noChange", result)) {
            aceSqlUtils.updateSonSortPathWithUnique("sys_menu", menu.getSortPath(), oldMenu.getSortPath().length(),
                    oldMenu
                            .getSortPath(), "app_id", menu.getAppId());
        }

        //判断菜单类型,菜单的路由不可以为空
        if (menu.getType() == 0) {
            menu.setUrl("");
        } else if (menu.getType() == 1) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_MENU_URL"));
            }
        }

        menu.setUpdateTime(LocalDateTime.now());

        if (Objects.equals(1, menu.getAddRelationFlag())) {
            SysMenuDO oldMenuDO = getById(menu.getId());
            boolean flag = false;

            if (StringUtils.isNotBlank(oldMenuDO.getAuthId())) {
                // 判断修改前的权限是否与自动创建的权限的位置一致
                SysAuthDO oldAuthDO = sysAuthService.getById(oldMenuDO.getAuthId());
                if (Objects.equals("0", menu.getParentId())) {
                    if (Objects.equals(oldAuthDO.getName(), menu.getName())) {
                        flag = true;
                    }
                } else {
                    if (StringUtils.isNotBlank(getById(menu.getParentId()).getAuthId())) {
                        if (Objects.equals(oldAuthDO.getParentId(), getById(menu.getParentId()).getAuthId()) &&
                                Objects.equals(oldAuthDO.getName(), menu.getName())) {
                            flag = true;
                        }
                    }
                }
            }
            if (flag) {
                // 一致
                menu.setAuthId(oldMenuDO.getAuthId());
                if (!this.updateById(menu)) {
                    return false;
                }
            } else {
                // 不一致
                menu.setAuthId(menu.getId());
                SysAuthDO sysAuthDO = sysAuthService.getById(menu.getAuthId());
                if (sysAuthDO == null) {
                    saveMenuAuth(menu, true);
                }
                if (!this.updateById(menu)) {
                    return false;
                }
            }
        } else {
            if (!this.updateById(menu)) {
                return false;
            }
        }


        if (StringUtils.isNotBlank(menu.getAuthId()) && Objects.equals(1, menu.getUpdRelationFlag())) {
            // 自动更新关联权限名称
            SysAuthDO sysAuthDO = sysAuthService.getById(menu.getAuthId());
            sysAuthDO.setName(menu.getName());
            sysAuthService.updateById(sysAuthDO);
        }
        String groupId = sysGroupAppService.getById(menu.getAppId()).getGroupId();
        String appId = null;
        String roleId = securityUtils.getRoleIds().get(0);
        if (Objects.equals(roleId, "appadmin")) {
            appId = menu.getAppId();
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新菜单", "更新菜单："+menu.getName(), groupId,
                appId)) {
            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }
        return true;
    }

    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysConfigService sysConfigService;

    /**
     * 左侧菜单树
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:00
     */
    @Override
    public List<SysMenuDO> listSideTree(String appId) {
        String userId = securityUtils.getCurrentUserId();

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(appId)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }


        // 获取当前用户的角色
        List<String> roleIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("user_id", userId)).stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
        // 平台管理员组成  "1"：集团应用三员；"2"：集团单一管理员、应用单一管理员；"3"：单一业务管理员
        String retainGroupAndAppAdmin;
        SysConfigDO config = sysConfigService.getOne(new QueryWrapper<SysConfigDO>().eq("name",
                "retainGroupAndAppAdmin"));
        if (config == null) {
            retainGroupAndAppAdmin = "1";
        } else {
            retainGroupAndAppAdmin = config.getValue();
        }

        /**
         * 获取当前用户权限列表
         */
//        List<SysAuthMixDO> authList = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
//                .eq("user_id", userId).eq("app_id", appId));
//        if (authList.size() == 0) {
//            return new ArrayList<>();
//        }
//        List<String> authIds = authList.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
        List<SysMenuDO> menuList = this.list(new QueryWrapper<SysMenuDO>()
                .ne("type", 2)
                .notIn(!openMongoDB, "id", mongoLogIds)
                .notIn(Constants.isMonomerApp || Constants.isZuulApp, "id", nacosMenuIds)
                .eq("app_id", appId)
//                .and(authIds == null || authIds.size() == 0, i -> i.eq("1", "2"))
//                .in("auth_id", authIds)
                .inSql("auth_id", "select auth_id from sys_auth_mix where app_id ='" + appId + "' and " + "user_id='" +
                        userId + "'")
                .orderByAsc("sort_path"));

        // 菜单对应的authId为空时，表明该菜单所有人都能看到
        menuList.addAll(this.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", appId).and(i -> i.isNull("auth_id").or().eq("auth_id", ""))));
        menuList = menuList.stream().sorted(Comparator.comparing(SysMenuDO::getSortPath)).collect(Collectors.toList());

        List<SysMenuDO> newMenus;
        // 平台管理员组成  "1"：集团应用三员；"2"：集团单一管理员、应用单一管理员；"3"：单一业务管理员
        if ("1".equals(retainGroupAndAppAdmin)) {
            //判断角色是否为admin,sec
            if (roleIds.contains("admin") || roleIds.contains("sec")) {
                // 去除【集团管理员维护】【业务管理员维护】菜单
                newMenus = menuList.stream().filter(menu -> !"jituan842fa04faea8f39beb866fd511".equals(menu.getId())
                        &&!"business2fa04faea8f39beb866fd511".equals(menu.getId())
                ).collect(Collectors.toList());
            } else {
                newMenus = menuList;
            }
        } else if("2".equals(retainGroupAndAppAdmin)) {
            //判断角色是否为admin,sec
            if (roleIds.contains("admin") || roleIds.contains("sec")) {
                // 去除【集团三员】【业务管理员维护】菜单
                newMenus = menuList.stream().filter(menu -> !"5fc1b8842fa04faea8f39beb866fd511".equals(menu.getId())
                        &&!"business2fa04faea8f39beb866fd511".equals(menu.getId())
                ).collect(Collectors.toList());
            } else {
                newMenus = menuList;
            }
        }else{
            //判断角色是否为admin,sec
            if (roleIds.contains("admin") || roleIds.contains("sec")) {
                // 去除【集团三员】【集团管理员维护】菜单
                newMenus = menuList.stream().filter(menu -> !"5fc1b8842fa04faea8f39beb866fd511".equals(menu.getId())
                        &&!"jituan842fa04faea8f39beb866fd511".equals(menu.getId())
                ).collect(Collectors.toList());
            } else {
                newMenus = menuList;
            }
        }

        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO menu : newMenus) {
            //目录
            if (Objects.equals(menu.getParentId(), "0")) {
                menu.setList(makeTree(newMenus.stream()
                        .filter(mu -> mu.getSortPath().startsWith(menu.getSortPath())
                                && mu.getSortPath().length() > menu.getSortPath().length())
                        .collect(Collectors.toList()), menu.getId()));
                menu.setChildren(menu.getList());
                subMenuList.add(menu);
            }
        }

        // 判断该应用管理员在应用管控域的应用范围内是否有应用的菜单需要展示在平台管控台上

        //当前用户的应用管控范围
        List<String> appIds =
                sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>().eq("is_activated", 1).eq("user_id",
                        userId))
                        .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());
        if (appIds != null && appIds.size() > 0) {
            List<SysAppMenuDisplayDO> sysAppMenuDisplayDOS = sysAppMenuDisplayService.list(new
                    QueryWrapper<SysAppMenuDisplayDO>()
                    .eq("user_id", userId).in("app_id", appIds));
            if (sysAppMenuDisplayDOS != null && sysAppMenuDisplayDOS.size() > 0) {
                SysMenuDO sysMenuDO = new SysMenuDO();
                sysMenuDO.setName("应用配置");
                sysMenuDO.setSortPath("999999");
                sysMenuDO.setType(0);
                sysMenuDO.setIcon("el-icon-setting");
                List<SysMenuDO> childMenuDOS = new ArrayList<>(16);
                sysAppMenuDisplayDOS.stream().forEach(sysAppMenuDisplayDO -> {
                    SysMenuDO childMenu = new SysMenuDO();
                    childMenu.setName(sysAppMenuDisplayDO.getMenuName());
                    childMenu.setIcon(sysAppMenuDisplayDO.getAppId());
                    childMenu.setUrl(sysAppMenuDisplayDO.getMenuUrl());
                    childMenu.setAppId(sysAppMenuDisplayDO.getAppId());
                    childMenu.setIframe(1);
                    childMenu.setType(1);
                    childMenu.setId(sysAppMenuDisplayDO.getId());
                    childMenu.setSortPath(SortPathUtils.getSortPath("999999", sysAppMenuDisplayDO.getMenuSortIndex()));
                    childMenuDOS.add(childMenu);
                });
                sysMenuDO.setList(childMenuDOS);
                subMenuList.add(sysMenuDO);
            }
        }

        return subMenuList;
    }


    /**
     * 递归把菜单列表转化为菜单树   左侧菜单
     *
     * @param menuList
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:54
     */
    public List<SysMenuDO> makeTree(List<SysMenuDO> menuList, String parentId) {
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO menu : menuList) {
            //目录
            if (Objects.equals(menu.getParentId(), parentId)) {
                if (menu.getType() == 0) {
                    menu.setList(makeTree(menuList.stream()
                            .filter(mu -> mu.getSortPath().startsWith(menu.getSortPath())
                                    && mu.getSortPath().length() > menu.getSortPath().length())
                            .collect(Collectors.toList()), menu.getId()));
                }
                subMenuList.add(menu);
            }
        }
        return subMenuList;
    }

    /**
     * 菜单管理树
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/5/15 9:00
     */
    @Override
    public List<SysMenuDO> listMenuTree(String appId) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(appId)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        /**
         * 获取当前用户权限列表
         */
        List<SysMenuDO> menuList = this.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", appId)
                .orderByAsc("sort_path"));
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO menu : menuList) {
            //目录
            if (Objects.equals(menu.getParentId(), "0")) {
                menu.setChildren(makeMenuTree(menuList.stream()
                        .filter(mu -> mu.getSortPath().startsWith(menu.getSortPath())
                                && mu.getSortPath().length() > menu.getSortPath().length())
                        .collect(Collectors.toList()), menu.getId()));
                subMenuList.add(menu);
            }
        }
        return subMenuList;
    }

    /**
     * 递归把菜单列表转化为菜单树   菜单管理
     *
     * @param menuList
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:54
     */
    public List<SysMenuDO> makeMenuTree(List<SysMenuDO> menuList, String parentId) {
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO menu : menuList) {
            //目录
            if (Objects.equals(menu.getParentId(), parentId)) {
                if (menu.getType() != 2) {
                    menu.setChildren(makeMenuTree(menuList.stream()
                            .filter(mu -> mu.getSortPath().startsWith(menu.getSortPath())
                                    && mu.getSortPath().length() > menu.getSortPath().length())
                            .collect(Collectors.toList()), menu.getId()));
                }
                subMenuList.add(menu);
            }
        }
        return subMenuList;
    }

    /**
     * 移动菜单
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:40
     */
    @Override
    public boolean moveMenu(Map<String, String> params) {
        String id = params.get("id");
        String aftParentId = params.get("aftParentId");
        String aftParentName = params.get("aftParentName");
        SysMenuDO sysMenuDO = getById(id);
        // 获取修改后的父菜单下的值菜单最大序号
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", "sys_menu");
        map.put("appId", sysMenuDO.getAppId());
        map.put("parentId", aftParentId);
        Integer maxSortIndex = aceSqlUtils.getMaxSort(map);
        sysMenuDO.setParentId(aftParentId);
        sysMenuDO.setParentName(aftParentName);
        sysMenuDO.setSortIndex(maxSortIndex + 10);
        String oldSortPath = sysMenuDO.getSortPath();
        String newSortPath = "";


        // 父节点发生改变 或 sortIndex 发生变化
        if (!Objects.equals("0", aftParentId)) {
            SysMenuDO parent = getById(aftParentId);
            if (parent == null) {
                throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_NOT_EXIST"));
            }
            newSortPath = SortPathUtils.getSortPath(parent
                    .getSortPath(), sysMenuDO.getSortIndex());
            sysMenuDO.setSortPath(newSortPath);

            if (StringUtils.isNotBlank(sysMenuDO.getAuthId())) {
                // 对应的权限同时需要移动
                if (StringUtils.isNotBlank(parent.getAuthId())) {
                    SysAuthDO sysAuthDO = sysAuthService.getById(sysMenuDO.getAuthId());
                    SysAuthDO parentAuth = sysAuthService.getById(parent.getAuthId());
                    // 获取修改后的父权限下的子权限最大序号
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("tableName", "sys_auth");
                    map2.put("appId", sysAuthDO.getAppId());
                    map2.put("parentId", parent.getAuthId());
                    Integer maxSortIndex2 = aceSqlUtils.getMaxSort(map2);
                    sysAuthDO.setParentId(parent.getAuthId());
                    sysAuthDO.setSortIndex(maxSortIndex2 + 10);
                    String oldSortPath2 = sysAuthDO.getSortPath();
                    String newSortPath2 = SortPathUtils.getSortPath(parentAuth.getSortPath(), sysAuthDO.getSortIndex
                            ());
                    sysAuthDO.setSortPath(newSortPath2);
                    if (!sysAuthService.updateById(sysAuthDO)) {
                        return false;
                    }
                    aceSqlUtils.updateSonSortPathWithUnique(
                            "sys_auth", newSortPath2, oldSortPath2.length(), oldSortPath2,
                            "app_id", sysAuthDO
                                    .getAppId());
                }
            }
        } else {
            newSortPath = SortPathUtils.getSortPath("", sysMenuDO.getSortIndex());
            sysMenuDO.setSortPath(newSortPath);

            if (StringUtils.isNotBlank(sysMenuDO.getAuthId())) {
                SysAuthDO sysAuthDO = sysAuthService.getById(sysMenuDO.getAuthId());
                // 获取修改后的父权限下的子权限最大序号
                Map<String, Object> map2 = new HashMap<>();
                map2.put("tableName", "sys_auth");
                map2.put("appId", sysAuthDO.getAppId());
                map2.put("parentId", "0");
                Integer maxSortIndex2 = aceSqlUtils.getMaxSort(map2);
                sysAuthDO.setParentId("0");
                sysAuthDO.setSortIndex(maxSortIndex2 + 10);
                String oldSortPath2 = sysAuthDO.getSortPath();
                String newSortPath2 = SortPathUtils.getSortPath("", sysAuthDO.getSortIndex());
                sysAuthDO.setSortPath(newSortPath2);
                if (!sysAuthService.updateById(sysAuthDO)) {
                    return false;
                }
                aceSqlUtils.updateSonSortPathWithUnique(
                        "sys_auth", newSortPath2, oldSortPath2.length(), oldSortPath2,
                        "app_id", sysAuthDO
                                .getAppId());
            }
        }


        if (!updateById(sysMenuDO)) {
            return false;
        }
        aceSqlUtils.updateSonSortPathWithUnique("sys_menu", newSortPath, oldSortPath.length(), oldSortPath,
                "app_id", sysMenuDO.getAppId());
        return true;
    }

    /**
     * 获取单个菜单信息
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/4/22 14:40
     */
    @Override
    public R getMenuInfo(String id) {
        SysMenuDO sysMenuDO = getById(id);
        if (sysMenuDO != null) {
            if (StringUtils.isNotBlank(sysMenuDO.getAuthId())) {
                sysMenuDO.setAuthName(sysAuthService.getById(sysMenuDO.getAuthId()).getName());
            }
            // 获取该菜单的所有子菜单ID列表
            List<String> cids = list(new QueryWrapper<SysMenuDO>()
                    .eq("app_id", sysMenuDO.getAppId()).likeRight("sort_path",
                            sysMenuDO.getSortPath())).stream().map(AbstractBaseDomain::getId)
                    .collect(Collectors.toList());
            return R.ok().put("menu", sysMenuDO).put("cids", cids);
        }
        return R.ok();
    }
}
