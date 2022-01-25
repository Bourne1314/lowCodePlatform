package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础数据-人员档案 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Transactional
public interface BdPersonDocService extends IService<BdPersonDocDO> {

    /**
     * 保存人员档案
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(BdPersonDocDO instance);

    /**
     * 更新人员档案
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(BdPersonDocDO instance);

    /**
     * 删除人员档案
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(List<String> ids);

    /**
     * 通过ID获取人员档案详情
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019/5/27 14:57
     */
    BdPersonDocDO getPersonById(String id);

}
