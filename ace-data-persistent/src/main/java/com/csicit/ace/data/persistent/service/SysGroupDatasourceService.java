package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.GroupDatasourceDetail;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用绑定数据源 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Transactional
public interface SysGroupDatasourceService extends IBaseService<SysGroupDatasourceDO> {

    /**
     * 保存数据源
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(SysGroupDatasourceDO instance);

    /**
     * 更新数据源
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(SysGroupDatasourceDO instance);

    /**
     * 删除数据源
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(String[] ids);

    /**
     * 获取数据源请求url
     *
     * @param id
     * @return java.lang.String
     * @author shanwj
     * @date 2020/5/29 10:06
     */
    String getDsUrl(String id);

    /**
     * 获取数据源请求url
     *
     * @param instance
     * @return java.lang.String
     * @author shanwj
     * @date 2020/5/29 10:06
     */
    String getDsUrl(SysGroupDatasourceDO instance);

    /**
     * 应用升级更新 数据源
     *
     * @param groupDatasources
     * @return
     * @author zuogang
     * @date 2020/8/6 17:49
     */
    boolean groupDatasourceUpdate(List<GroupDatasourceDetail> groupDatasources, String appId);

}
