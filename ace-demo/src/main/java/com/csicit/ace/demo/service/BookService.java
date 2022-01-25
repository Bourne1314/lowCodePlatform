package com.csicit.ace.demo.service;

import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.demo.domain.BookDO;

/**
 * @author shanwj
 * @date 2019/5/27 17:24
 */
public interface BookService extends IBaseService<BookDO> {

    boolean saveBook(BookDO bookDO);
}
