package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysDictValueDO;

import java.util.List;

/**
 * 获取字典数据
 *
 * @author shanwj
 * @date 2019/5/28 11:05
 * @version V1.0
 */
public interface IDict {

    /**
     * 通过字典类型获取字典项
     *
     * @param type 字典类型
     * @return 字典项列表
     * @author shanwj
     * @date 2019/6/18 18:06
     */
    List<SysDictValueDO> getValue(String type);
}
