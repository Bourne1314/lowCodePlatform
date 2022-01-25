package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IApi;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * api处理相关接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/5/22 10:17
 */
@Service("api")
public class ApiImpl extends BaseImpl implements IApi {

    /**
     * 保存api资源列表
     *
     * @param list api资源列表
     * @return boolean
     * @author shanwj
     * @date 2019/5/22 10:18
     */
    @Override
    public boolean save(List<SysApiResourceDO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            return gatewayService.saveSysApis(list);
        }
        return true;
    }

    @Override
    public boolean delete(String appId) {
        if (StringUtils.isNotBlank(appId)) {
            return gatewayService.delete(appId);
        }
        return false;
    }

}
