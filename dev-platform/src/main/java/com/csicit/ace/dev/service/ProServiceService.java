package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务管理 实例对象访问接口
 *
 * @author shanwj
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProServiceService extends IBaseService<ProServiceDO> {
    /**
     * 保存服务
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/1/3 15:20
     */
    boolean saveProService(ProServiceDO instance);
    /**
     * 删除服务
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean deleteByIds(List<String> ids);

    /**
     * 判断当前应用服务是否启动中
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:20
     */
    String serverRunJudge(String id);

    /**
     * 关闭服务
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:20
     */
    boolean closeServer(String id);

    /**
     * 关闭服务
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:20
     */
    boolean runServer(String id);
}
