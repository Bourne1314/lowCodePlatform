package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.MsgTypeExtendDetail;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/9/5 11:15
 */
@Transactional
public interface SysMsgTypeExtendService extends IBaseService<SysMsgTypeExtendDO> {

    boolean deleteByIds(Collection<? extends Serializable> idList);

    boolean saveMsgExtends(List<SysMsgTypeExtendDO> msgExtends, String appId);

    /**
     * 应用升级时，消息拓展更新
     *
     * @param msgTypeExtendDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:39
     */
    boolean msgTypeExtendUpdate(List<MsgTypeExtendDetail> msgTypeExtendDetails, String appId);
}
