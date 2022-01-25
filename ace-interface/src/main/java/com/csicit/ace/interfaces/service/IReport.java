package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.vo.TreeVO;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/12/13 8:50
 */
public interface IReport {

    /**
     * 获取报表树
     *
     * @param parentId 父级id
     * @param type 报表类型 0报表1仪表盘
     * @return 报表树
     */
    List<TreeVO> getReportTree(String parentId, int type);
}
