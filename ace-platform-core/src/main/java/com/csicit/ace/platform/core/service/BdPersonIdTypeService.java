package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础数据-人员证件类型 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 17:27:06
 * @version V1.0
 */
@Transactional
public interface BdPersonIdTypeService extends IBaseService<BdPersonIdTypeDO> {

    /**
     * 保存人员证件类型
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R insert(BdPersonIdTypeDO instance);

    /**
     * 更新人员证件类型
     * @param instance
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R update(BdPersonIdTypeDO instance);

    /**
     * 删除人员证件类型
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    R delete(List<String> ids);
}
