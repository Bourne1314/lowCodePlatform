package com.csicit.ace.demo.service;

import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.demo.domain.BookInfoDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/10/16 15:54
 */
@Transactional
public interface BookInfoService extends IBaseService<BookInfoDO> {

    boolean saveBookInfo(BookInfoDO instance);
}
