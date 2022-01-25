package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysMenuMapper;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.data.persistent.service.SysMsgSendTypeService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.SysAuthService;
import com.csicit.ace.orgauth.core.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单实现
 *
 * @author shanwj
 * @date 2019/6/13 19:06
 */
@Service("sysMenuServiceO")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuService {

    @Resource(name = "sysAuthMixServiceO")
    SysAuthMixService sysAuthMixService;

    @Resource(name = "sysAuthServiceO")
    SysAuthService sysAuthService;


    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    @Override
    public List<SysMenuDO> listSideTree(String appId) {
        String userId = securityUtils.getCurrentUserId();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(appId)) {
            throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
        }
        /**
         * 获取当前用户权限列表
         */
        List<SysAuthMixDO> authList = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                .eq("user_id", userId).eq("app_id", appId));
        if (authList.size() == 0) {
            return new ArrayList<>();
        }
        List<String> authIds = authList.stream().map(SysAuthMixDO::getAuthId).collect(Collectors.toList());
        List<SysMenuDO> menuList = this.list(new QueryWrapper<SysMenuDO>()
                .and(authIds == null || authIds.size() == 0, i -> i.eq("1", "2"))
                .in("auth_id", authIds)
                .orderByAsc("sort_path"));
        List<SysMenuDO> list = new ArrayList<>(16);
        menuList.stream().forEach(menu -> {
            int count = count(new QueryWrapper<SysMenuDO>().eq("parent_id", menu.getId()));
            menu.setLeaf(0);
            if (count > 0) {
                menu.setLeaf(1);
            }
            list.add(menu);
        });
        return list;
    }

    @Override
    public List<SysMenuDO> listSideTreeByNoPermission(String appId) {
        List<SysMenuDO> menuList = this.list(new QueryWrapper<SysMenuDO>()
                .in("app_Id", appId)
                .orderByAsc("sort_path"));
        List<SysMenuDO> list = new ArrayList<>(16);
        menuList.stream().forEach(menu -> {
            int count = count(new QueryWrapper<SysMenuDO>().eq("parent_id", menu.getId()));
            menu.setLeaf(0);
            if (count > 0) {
                menu.setLeaf(1);
            }
            list.add(menu);
        });
        return list;
    }

    @Override
    public List<SysMenuDO> listByParentId(String appId, String parentId, String userId) {
        // 查询有效权限
        if (com.csicit.ace.common.utils.StringUtils.isNotBlank(userId)) {
            List<String> authIds = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                    .eq("user_id", userId).eq("app_id", appId)).stream().map(SysAuthMixDO::getAuthId)
                    .collect(Collectors.toList());
            if (authIds != null && authIds.size() > 0) {
                if (com.csicit.ace.common.utils.StringUtils.isNotBlank(appId) && com.csicit.ace.common.utils
                        .StringUtils.isNotBlank(parentId)) {
                    List<SysMenuDO> list = list(new QueryWrapper<SysMenuDO>().eq("APP_ID", appId)
                            .in("auth_id", authIds).eq("PARENT_ID", parentId)
                            .orderByAsc("SORT_PATH"));

                    list.stream().forEach(menu -> {
                        menu.setLeaf(count(new QueryWrapper<SysMenuDO>().eq("APP_ID", appId)
                                .eq("PARENT_ID", menu.getId())));
                        menu.setIsLeaf(menu.getLeaf() > 0 ? 0 : 1);
                    });

                    return list;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean setAppFLowMenu(String appId) {
        if (!Constants.AppNames.contains(appId) &&
                !sysMsgSendTypeService.createBpmChannelAndTemplate(appId)) {
            throw new RException("初始化创建工作流消息模板异常!");
        }


        SysMenuDO sysMenuDO = getOne(new QueryWrapper<SysMenuDO>()
                .eq("app_id", appId).eq("name", "工作流程"));
        if (sysMenuDO != null) {
            return true;
        }
        // 获取当前应用下菜单最大排序号
        Integer sortIndex = 10;
        SysMenuDO sysMenuDOForIndex = getOne(new QueryWrapper<SysMenuDO>()
                .eq("app_id", appId).eq("parent_id", "0").orderByDesc("sort_index"));
        if (sysMenuDOForIndex != null) {
            sortIndex = sysMenuDOForIndex.getSortIndex() + sortIndex;
        }

        List<SysMenuDO> sysMenuDOS = new ArrayList<>();
        List<SysAuthDO> sysAuthDOS = new ArrayList<>();
        // 添加工作流程菜单
        SysMenuDO workFlow = new SysMenuDO();
        workFlow.setId(UuidUtils.createUUID());
        workFlow.setName("工作流程");
        workFlow.setParentId("0");
        workFlow.setAppId(appId);
        workFlow.setParentName("一级菜单");
        workFlow.setUrl("");
        workFlow.setType(0);
        workFlow.setIcon("fa fa-cubes");
        workFlow.setSortIndex(sortIndex);
        workFlow.setSortPath(SortPathUtils.getSortPath("", sortIndex));
        sysMenuDOS.add(workFlow);
        // 添加新建工作菜单
        SysMenuDO newJob = new SysMenuDO();
        newJob.setId(UuidUtils.createUUID());
        newJob.setName("新建工作");
        newJob.setParentId(workFlow.getId());
        newJob.setAppId(appId);
        newJob.setParentName("工作流程");
        newJob.setUrl("platform/common/components/CreatWork");
        newJob.setType(1);
        newJob.setIcon("fa fa-cube");
        newJob.setSortIndex(10);
        newJob.setSortPath(SortPathUtils.getSortPath(workFlow
                .getSortPath(), 10));
        sysMenuDOS.add(newJob);
        // 添加我的工作菜单
        SysMenuDO myJob = new SysMenuDO();
        myJob.setId(UuidUtils.createUUID());
        myJob.setName("我的工作");
        myJob.setParentId(workFlow.getId());
        myJob.setAppId(appId);
        myJob.setParentName("工作流程");
        myJob.setUrl("platform/common/components/MyJob");
        myJob.setType(1);
        myJob.setIcon("fa fa-cube");
        myJob.setSortIndex(20);
        myJob.setSortPath(SortPathUtils.getSortPath(workFlow
                .getSortPath(), 20));
        sysMenuDOS.add(myJob);
        // 添加委托规则菜单
        SysMenuDO rule = new SysMenuDO();
        rule.setId(UuidUtils.createUUID());
        rule.setName("委托规则");
        rule.setParentId(workFlow.getId());
        rule.setAppId(appId);
        rule.setParentName("工作流程");
        rule.setUrl("platform/common/components/DelegateRule");
        rule.setType(1);
        rule.setIcon("fa fa-cube");
        rule.setSortIndex(30);
        rule.setSortPath(SortPathUtils.getSortPath(workFlow
                .getSortPath(), 30));
        sysMenuDOS.add(rule);

        // 添加工作查询权限
        Integer authSortIndex = 10;
        SysAuthDO sysAuthForIndex = sysAuthService.getOne(new QueryWrapper<SysAuthDO>()
                .eq("app_id", appId).eq("parent_id", "0").orderByDesc("sort_index"));
        if (sysAuthForIndex != null) {
            authSortIndex = authSortIndex + sysAuthForIndex.getSortIndex();
        }
        SysAuthDO queryAuth = new SysAuthDO();
        queryAuth.setId(UuidUtils.createUUID());
        queryAuth.setName("工作查询");
        queryAuth.setAppId(appId);
        queryAuth.setSortIndex(authSortIndex);
        queryAuth.setParentId("0");
        queryAuth.setSortPath(SortPathUtils.getSortPath("", authSortIndex));
        queryAuth.setCode("workFlow.jobQuery");
        sysAuthDOS.add(queryAuth);
        // 添加工作查询菜单
        SysMenuDO jobQuery = new SysMenuDO();
        jobQuery.setId(UuidUtils.createUUID());
        jobQuery.setName("工作查询");
        jobQuery.setParentId(workFlow.getId());
        jobQuery.setAppId(appId);
        jobQuery.setParentName("工作流程");
        jobQuery.setUrl("platform/common/components/JobQuery");
        jobQuery.setType(1);
        jobQuery.setIcon("fa fa-cube");
        jobQuery.setAuthId(queryAuth.getId());
        jobQuery.setSortIndex(40);
        jobQuery.setSortPath(SortPathUtils.getSortPath(workFlow
                .getSortPath(), 40));
        sysMenuDOS.add(jobQuery);

        // 添加工作监控权限
        SysAuthDO monitorAuth = new SysAuthDO();
        monitorAuth.setId(UuidUtils.createUUID());
        monitorAuth.setName("工作监控");
        monitorAuth.setAppId(appId);
        monitorAuth.setSortIndex(authSortIndex + 10);
        monitorAuth.setParentId("0");
        monitorAuth.setSortPath(SortPathUtils.getSortPath("", authSortIndex + 10));
        monitorAuth.setCode("workFlow.JobMonitor");
        sysAuthDOS.add(monitorAuth);
        // 添加工作监控菜单
        SysMenuDO jobMonitor = new SysMenuDO();
        jobMonitor.setId(UuidUtils.createUUID());
        jobMonitor.setName("工作监控");
        jobMonitor.setParentId(workFlow.getId());
        jobMonitor.setAppId(appId);
        jobMonitor.setParentName("工作流程");
        jobMonitor.setUrl("platform/common/components/JobMonitor");
        jobMonitor.setType(1);
        jobMonitor.setIcon("fa fa-cube");
        jobMonitor.setAuthId(monitorAuth.getId());
        jobMonitor.setSortIndex(50);
        jobMonitor.setSortPath(SortPathUtils.getSortPath(workFlow
                .getSortPath(), 50));
        sysMenuDOS.add(jobMonitor);

        // 添加历史信息菜单
        SysMenuDO historyMessage = new SysMenuDO();
        historyMessage.setId(UuidUtils.createUUID());
        historyMessage.setName("历史信息");
        historyMessage.setParentId("0");
        historyMessage.setAppId(appId);
        historyMessage.setParentName("一级菜单");
        historyMessage.setUrl("platform/common/components/ReadMessage");
        historyMessage.setType(0);
        historyMessage.setIcon("fa fa-dashboard");
        historyMessage.setSortIndex(sortIndex + 10);
        historyMessage.setSortPath(SortPathUtils.getSortPath("", sortIndex + 10));
        sysMenuDOS.add(historyMessage);
        if (sysAuthService.saveBatch(sysAuthDOS)) {
            if (saveBatch(sysMenuDOS)) {
                return true;
            }
        }
        throw new RException("工作流菜单创建失败");
    }

}
