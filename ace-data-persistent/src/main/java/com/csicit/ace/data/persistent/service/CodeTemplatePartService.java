package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.CodeTemplatePartDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;

/**
 * @author shanwj
 * @date 2020/5/22 15:17
 */
public interface CodeTemplatePartService extends IBaseService<CodeTemplatePartDO> {

    R saveTemplatePart(CodeTemplatePartDO part);

    R updateTemplatePart(CodeTemplatePartDO part);

}
