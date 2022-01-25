package com.csicit.ace.demo.service;

import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.demo.domain.BookTypeDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/10/16 15:16
 */
@Transactional
public interface BookTypeService extends IBaseService<BookTypeDO> {
    boolean saveBookType(BookTypeDO intance);

    TreeVO getBookTypeTree(String appId);
}
