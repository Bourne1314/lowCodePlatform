package com.csicit.ace.demo.service.impl;

import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.demo.domain.BookInfoDO;
import com.csicit.ace.demo.mapper.BookInfoMapper;
import com.csicit.ace.demo.service.BookInfoService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/10/16 15:55
 */
@Service
public class BookInfoServiceImpl extends BaseServiceImpl<BookInfoMapper,BookInfoDO> implements BookInfoService {
    @Override
    public boolean saveBookInfo(BookInfoDO instance) {
        if(save(instance)){
            return true;
        }
        return false;
    }
}
