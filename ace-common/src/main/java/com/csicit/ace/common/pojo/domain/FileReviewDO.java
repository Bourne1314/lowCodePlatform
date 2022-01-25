package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 附件管理-文件审查信息
 *
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@Data
@TableName("FILE_REVIEW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileReviewDO{
    //business_Key
    /**
     * 文件审查信息id
     */
    private String id;

    //[id1,id2]
    /**
     * 文件信息id
     */
    private String fileInfoId;

    /**
     * 审查人
     */
    private String reviewer;

    /**
     * 审查日期
     */
    private Date reviewDate;

    /**
     * 审查状态
     */
    private Integer reviewStatus;


}