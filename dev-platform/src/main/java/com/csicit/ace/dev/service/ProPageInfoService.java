package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProPageInfoDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 页面基本信息 实例对象访问接口
 *
 * @author shanwj
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProPageInfoService extends IBaseService<ProPageInfoDO> {
    /**
     * 获取项目信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:47
     */
    ProPageInfoDO getProPageInfoDO(String id);
}
