package com.csicit.ace.demo.service.impl;

import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.demo.domain.BorrowDO;
import com.csicit.ace.demo.mapper.BorrowMapper;
import com.csicit.ace.demo.service.BorrowService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/5/28 8:15
 */
@Service
public class BorrowServiceImpl extends BaseServiceImpl<BorrowMapper,BorrowDO> implements BorrowService {
}
