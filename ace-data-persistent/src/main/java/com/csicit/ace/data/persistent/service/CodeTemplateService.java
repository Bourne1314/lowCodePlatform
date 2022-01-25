package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.AppUpgradeJaxb.CodeTemplateDetail;
import com.csicit.ace.common.pojo.domain.CodeTemplateDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;

import java.util.Map;

/**
 * @author shanwj
 * @date 2020/5/22 15:17
 */
public interface CodeTemplateService extends IBaseService<CodeTemplateDO> {

    R saveTemplate(CodeTemplateDO instance);

    R updateTemplate(CodeTemplateDO instance);

    String getTemplateCode(Map<String, String> map);

    /**
     * 应用升级时， 编码模板更新
     * @param appUpgrade	
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 9:40
     */
    boolean codeTemplateUpdate(AppUpgrade appUpgrade);
}
