package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProInterfaceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 实体模型 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProInterfaceService extends IBaseService<ProInterfaceDO> {

    /**
     * 新增接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addInterface(ProInterfaceDO instance);

    /**
     * 修改接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updInterface(ProInterfaceDO instance);

    /**
     * 删除接口
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delInterface(List<String> ids);

    /**
     * sql代码测试检查
     *
     * @param instance
     * @return R
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    R sqlCodeCheck(ProInterfaceDO instance);

    /**
     * 获取单个
     *
     * @param sqlCode
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    R getSqlResult(String sqlCode , String queryString );
}
