package com.csicit.ace.demo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * @author shanwj
 * @date 2019/5/27 20:13
 */
@TableName("BORROW")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BorrowDO extends AbstractBaseDomain {
    private String borrowPerson;
    private String bookName;
    private int quantity;
}
