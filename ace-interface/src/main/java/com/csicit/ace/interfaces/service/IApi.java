package com.csicit.ace.interfaces.service;

import com.csicit.ace.common.pojo.domain.SysApiResourceDO;

import java.util.List;

/**
 * api处理相关接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/5/22 10:15
 */
public interface IApi {

    /**
     * 保存api资源列表
     *
     * @param list api资源集合
     * @return 保存结果
     * @author shanwj
     * @date 2019/5/22 10:18
     */
    boolean save(List<SysApiResourceDO> list);

    /**
     * 删除应用对应的api资源列表
     *
     * @param appId 应用id
     * @return boolean 删除结果
     * @author shanwj
     * @date 2019/5/22 10:18
     */
    boolean delete(String appId);

}
