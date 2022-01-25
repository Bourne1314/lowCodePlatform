package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.mapper.ReportMapper;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.ReportService;
import com.csicit.ace.orgauth.core.service.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/12/12 11:47
 */
@Service("reportServiceO")
public class ReportServiceImpl extends BaseServiceImpl<ReportMapper, ReportInfoDO> implements ReportService {

    @Resource(name = "reportTypeServiceO")
    private ReportTypeService reportTypeService;
    @Resource(name = "sysAuthMixServiceO")
    private SysAuthMixService sysAuthMixService;
    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public List<TreeVO> getReportTree(String parentId, int reportType, String appId) {
        System.out.println("appId：" + appId);
        String userId = securityUtils.getCurrentUserId();
        System.out.println("userId：" + userId);
        List<TreeVO> trees = new ArrayList<>(16);
        List<ReportTypeDO> types =
                reportTypeService.list(new QueryWrapper<ReportTypeDO>()
                        .eq("parent_id", parentId)
                        .eq("type", reportType)
                        .eq("app_id", appId)
                        .orderByAsc("sort"));
        List<ReportInfoDO> infos = list(new QueryWrapper<ReportInfoDO>()
                .eq("type_id", parentId)
                .orderByAsc("name"));
        types.stream().forEach(type -> {
            if (reportTypeService.count(new QueryWrapper<ReportTypeDO>()
                    .eq("parent_id", type.getId())
                    .eq("app_id", appId).eq("type", reportType)) > 0
                    || count(new QueryWrapper<ReportInfoDO>().eq("type_id", type.getId())) > 0) {
                TreeVO tree = new TreeVO();
                tree.setId(type.getId());
                tree.setLabel(type.getName());
                tree.setType("parentNode");
                trees.add(tree);
            }
        });
        infos.stream().forEach(info -> {
            if (StringUtils.isEmpty(info.getAuth()) || sysAuthMixService.count(
                    new QueryWrapper<SysAuthMixDO>()
                            .eq("auth_id", info.getAuth())
                            .eq("user_id", userId)) > 0) {
                TreeVO tree = new TreeVO();
                tree.setId(info.getId());
                tree.setLabel(info.getName());
                tree.setType("childNode");
                trees.add(tree);
            }
        });
        return trees;
    }
}
