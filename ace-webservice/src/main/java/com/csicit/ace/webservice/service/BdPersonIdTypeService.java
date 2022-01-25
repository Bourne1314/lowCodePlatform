package com.csicit.ace.webservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.server.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础数据-人员证件类型 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:06
 */
@Transactional
public interface BdPersonIdTypeService extends IService<BdPersonIdTypeDO> {

    /**
     * 保存人员证件类型
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(BdPersonIdTypeDO instance);

    /**
     * 更新人员证件类型
     *
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(BdPersonIdTypeDO instance);

    /**
     * 删除人员证件类型
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(List<String> ids);
}
