package com.csicit.ace.testapp.service.impl;

import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.testapp.mapper.BorrowBookMapper;
import com.csicit.ace.testapp.pojo.BorrowBook;
import com.csicit.ace.testapp.service.BorrowBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("borrowBookService")
public class BorrowBookServiceImpl extends BaseServiceImpl<BorrowBookMapper, BorrowBook> implements
        BorrowBookService {

    @Autowired
    BpmManager bpmManager;

    /**
     * 新建借书信息
     *
     * @param borrowBook
     * @return String
     * @date 2019/5/17 11:31
     */
    @Override
    public boolean addBorrowBook(BorrowBook borrowBook) {

        borrowBook.setCreateTime(LocalDateTime.now());
        borrowBook.setCreateUser(securityUtils.getCurrentUserId());
        if (!save(borrowBook)) {
            return false;
        }
        // 创建流程实例
        bpmManager.createFlowInstanceByCode(borrowBook.getFlowCode(),borrowBook.getId());
        return true;
    }
}
