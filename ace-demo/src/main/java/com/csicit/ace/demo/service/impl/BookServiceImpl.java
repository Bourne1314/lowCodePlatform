package com.csicit.ace.demo.service.impl;

import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.demo.domain.BookDO;
import com.csicit.ace.demo.mapper.BookMapper;
import com.csicit.ace.demo.service.BookService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/5/27 17:25
 */
@Service
public class BookServiceImpl extends BaseServiceImpl<BookMapper,BookDO> implements BookService {
    @Override
    public boolean saveBook(BookDO bookDO) {
        if(save(bookDO)){
            return true;
        }
        return false;
    }
}
