package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.MsgSendTypeDetail;
import com.csicit.ace.common.pojo.domain.SysMsgSendTypeDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/8/26 10:09
 */
@Transactional
public interface SysMsgSendTypeService extends IBaseService<SysMsgSendTypeDO> {

    /**
     * 应用启动时创建工作流默认模板、消息频道
     *
     * @param appId
     * @return
     */
    boolean createBpmChannelAndTemplate(String appId);

    /**
     * 查询当前应用信使类别
     *
     * @param appId
     * @return
     */
    List<KeyValueVO> listSendTypes(String appId);

    /**
     * 应用升级时，消息通道更新
     *
     * @param msgSendTypeDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:17
     */
    boolean msgSendTypeUpdate(List<MsgSendTypeDetail> msgSendTypeDetails, String appId);
}
