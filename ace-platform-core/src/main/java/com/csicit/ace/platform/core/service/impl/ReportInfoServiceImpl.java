package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.ReportInfoMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.ReportInfoService;
import com.csicit.ace.platform.core.service.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 报表信息 实例对象访问接口实现
 *
 * @author generator
 * @date 2019-08-07 08:54:46
 * @version V1.0
 */
@Service("reportInfoService")
public class ReportInfoServiceImpl extends BaseServiceImpl<ReportInfoMapper, ReportInfoDO>
        implements ReportInfoService {

    @Autowired
    ReportTypeService reportTypeService;

    @Override
    public TreeVO getReportTree(String appId,int type) {
        List<ReportTypeDO> typeList = reportTypeService.list(
                new QueryWrapper<ReportTypeDO>()
                        .eq("app_id", appId)
                        .eq("type",type)
                        .eq("parent_id","-1")
                        .orderByAsc("sort"));
        List<TreeVO> list = new ArrayList<>(16);
        typeList.stream().forEach(rt->{
            TreeVO tree = new TreeVO();
            tree.setId(rt.getId());
            tree.setLabel(rt.getName());
            tree.setType("parentNode");
            List<TreeVO> childrens = new ArrayList<>(16);
            setChildrens(childrens,rt);
            tree.setChildren(childrens);
            list.add(tree);
        });
        TreeVO tr = new TreeVO();
        tr.setId("0");
        if(type==1){
            tr.setLabel("所有报表");
        }else{
            tr.setLabel("所有仪表盘");
        }
        tr.setType("parentNode");
        tr.setChildren(list);
        return tr;
    }

    @Override
    public boolean saveReport(ReportInfoDO report) {
        if(save(report)){
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success,"新增"),"新增报表",
                    securityUtils.getCurrentUser().getRealName()+" 新增报表 "+report.getName(),
                    null,report.getAppId());
        }
        return false;
    }

    @Override
    public boolean updateReport(ReportInfoDO report) {
        if(updateById(report)){
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning,"更新"),"更新报表",
                    securityUtils.getCurrentUser().getRealName()+" 更新报表 "+report.getName(),
                    null,report.getAppId());
        }
        return false;
    }

    @Override
    public boolean importMrt(String id, String mrtStr) {
        ReportInfoDO reportInfoDO = getById(id);
        reportInfoDO.setMrtStr(mrtStr);
        if(updateById(reportInfoDO)){
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success,"新增"),"导入报表",
                    securityUtils.getCurrentUser().getRealName()+" 导入报表 "+reportInfoDO.getName(),
                    null,reportInfoDO.getAppId());
        }
        return false;
    }

    @Override
    public boolean deleteReports(List<String> ids) {
        StringBuffer sb = new StringBuffer(16);
        ids.parallelStream().forEach(id->{
            sb.append(getById(id).getName());
            sb.append(",");
        });
        if (removeByIds(ids)){
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger,"删除"),"删除报表",
                    securityUtils.getCurrentUser().getRealName()+" 删除报表 "+sb.toString(),
                    null,getById(ids.get(0)).getAppId());
        }
        return false;
    }

    private void setChildrens(List<TreeVO> childrens,ReportTypeDO rt){
        List<ReportTypeDO> types =
                reportTypeService.list(
                        new QueryWrapper<ReportTypeDO>()
                                .eq("parent_id",rt.getId())
                                .orderByAsc("sort"));
        if(types!=null&&types.size()>0){
            types.stream().forEach(info->{
                TreeVO treeVO = new TreeVO();
                treeVO.setId(info.getId());
                treeVO.setLabel(info.getName());
                treeVO.setType("parentNode");
                List<TreeVO> trs = new ArrayList<>(16);
                setChildrens(trs,info);
                treeVO.setChildren(trs);
                childrens.add(treeVO);
            });
        }
        List<ReportInfoDO> infos = list(new QueryWrapper<ReportInfoDO>().eq("type_id", rt.getId()));
        if (infos!=null&&infos.size()>0){
            infos.stream().forEach(info->{
                TreeVO treeVO = new TreeVO();
                treeVO.setId(info.getId());
                treeVO.setLabel(info.getName());
                treeVO.setType("childNode");
                childrens.add(treeVO);
            });
        }
    }
}
