package com.csicit.ace.testapp.service.impl;

import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.testapp.mapper.BookMapper;
import com.csicit.ace.testapp.pojo.Book;
import com.csicit.ace.testapp.service.BookService;
import org.springframework.stereotype.Service;

@Service("bookService")
public class BookServiceImpl extends BaseServiceImpl<BookMapper, Book> implements
        BookService {
}
