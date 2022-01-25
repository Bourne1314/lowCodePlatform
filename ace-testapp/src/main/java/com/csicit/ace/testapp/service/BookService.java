package com.csicit.ace.testapp.service;

import com.csicit.ace.dbplus.service.IBaseService;
import com.csicit.ace.testapp.pojo.Book;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookService extends IBaseService<Book> {
}
