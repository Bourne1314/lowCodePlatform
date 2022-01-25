package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.OrgCorporationDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 组织-公司组织 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-16 15:31:10
 * @version V1.0
 */
@Transactional
public interface OrgCorporationService extends IBaseService<OrgCorporationDO>, BaseOrgService {
    /**
     * 保存公司
     * @param corp
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean saveCorp(OrgCorporationDO corp);

    /**
     * 修改公司
     * @param corp
     * @return
     * @author yansiyang
     * @date 2019/4/15 16:08
     */
    boolean updateCorp(OrgCorporationDO corp);

    /**
     * 删除公司
     * @param map
     * @return 
     * @author yansiyang
     * @date 2019/5/15 8:44
     */
    R deleteCorp(Map<String, Object> map);

}
