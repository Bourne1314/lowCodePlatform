package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaAssociationDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据关联定义 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-07 10:31:39
 * @version V1.0
 */
@Transactional
public interface MetaAssociationService extends IBaseService<MetaAssociationDO> {

    boolean saveAssociation(MetaAssociationDO ass);

    boolean updateAssociation(MetaAssociationDO ass);

    boolean deleteAssociation(String id);
}
