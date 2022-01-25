package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.DictDetail;
import com.csicit.ace.common.AppUpgradeJaxb.DictValueDetail;
import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 字典类型 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:58
 */
@Transactional
public interface SysDictServiceD extends IBaseService<SysDictDO> {
    /**
     * 应用升级时，字典更新
     *
     * @param dictDetails
     * @param dictValueDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:49
     */
    boolean dictUpdate(List<DictDetail> dictDetails, List<DictValueDetail> dictValueDetails, String appId);

}
