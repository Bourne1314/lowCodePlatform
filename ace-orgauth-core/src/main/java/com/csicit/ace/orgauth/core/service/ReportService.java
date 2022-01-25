package com.csicit.ace.orgauth.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.vo.TreeVO;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/12/12 11:46
 */
public interface ReportService extends IService<ReportInfoDO> {

    List<TreeVO> getReportTree(String parentId, int type, String appId);
}
