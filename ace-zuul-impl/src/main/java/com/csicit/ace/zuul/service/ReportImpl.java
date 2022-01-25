package com.csicit.ace.zuul.service;

import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.interfaces.service.IReport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/12/13 8:52
 */
@Service("report")
public class ReportImpl extends BaseImpl implements IReport {

    @Override
    public List<TreeVO> getReportTree(String parentId, int type) {
        return clientService.getReportTree(parentId, type, securityUtils.getAppName());
    }

}
