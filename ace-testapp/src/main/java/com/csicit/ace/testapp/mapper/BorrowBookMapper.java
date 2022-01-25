package com.csicit.ace.testapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.testapp.pojo.BorrowBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowBookMapper extends BaseMapper<BorrowBook> {
}
