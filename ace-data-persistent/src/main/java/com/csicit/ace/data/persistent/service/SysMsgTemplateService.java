package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.MsgTemplateConfigDetail;
import com.csicit.ace.common.AppUpgradeJaxb.MsgTemplateDetail;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2020/4/7 9:39
 */
@Transactional
public interface SysMsgTemplateService extends IBaseService<SysMsgTemplateDO> {
    /**
     * 获取平台所有小程序模板列表
     *
     * @return
     */
    List<SysMsgTemplateDO> getMicroAppTemplateList();

    /**
     * 保存导入选择的程序模板
     *
     * @param map
     * @return
     */
    R importSelectedTemplates(Map<String, Object> map);

    /**
     * 保存所有小程序模板
     *
     * @return
     */
    R importAllTemplates(Map<String, Object> map);

    /**
     * 保存平台消息模板
     *
     * @param instance
     * @return
     */
    boolean saveMsgTemplate(SysMsgTemplateDO instance);

    /**
     * 应用升级时，消息模板更新
     *
     * @param msgTemplateDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:27
     */
    boolean msgTemplateUpdate(List<MsgTemplateDetail> msgTemplateDetails, List<MsgTemplateConfigDetail>
            msgTemplateConfigDetails, String appId);
}
