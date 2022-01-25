package com.csicit.ace.testapp.service;

import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.testapp.pojo.BorrowBook;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BorrowBookService extends IBaseService<BorrowBook> {
    /**
     * 新建借书信息
     *
     * @param borrowBook
     * @return
     * @date 2019/5/17 11:15
     */
    boolean addBorrowBook(BorrowBook borrowBook);
}
