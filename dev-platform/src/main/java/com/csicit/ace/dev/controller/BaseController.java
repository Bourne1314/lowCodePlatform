package com.csicit.ace.dev.controller;

import com.csicit.ace.dev.service.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shanwj
 * @date 2019/11/4 14:28
 */
public abstract class BaseController {

    @Autowired
    protected MetaAssociationService metaAssociationService;

    @Autowired
    protected MetaTableService metaTableService;

    @Autowired
    protected MetaIndexService metaIndexService;

    @Autowired
    protected MetaDatasourceService metaDatasourceService;

    @Autowired
    protected MetaViewService metaViewService;

    @Autowired
    protected MetaTableColService metaTableColService;

    @Autowired
    protected MetaViewColService metaViewColService;

}
