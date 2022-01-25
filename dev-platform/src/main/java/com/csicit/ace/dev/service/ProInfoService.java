package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProInfoDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用管理 实例对象访问接口
 *
 * @author shanwj
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProInfoService extends IBaseService<ProInfoDO> {

    /**
     * 新增项目
     *
     * @param proInfoDO
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean saveInfo(ProInfoDO proInfoDO);

    /**
     * 删除项目
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean deleteByIds(List<String> ids);

    /**
     * 获取项目信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    ProInfoDO getProInfoDO(String id);
}
