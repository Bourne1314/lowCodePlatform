package com.csicit.ace.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 切片信息
 *
 * @author JonnyJiang
 * @date 2020/6/22 15:07
 */
@Data
public class ChunkVO implements Serializable {
    private Integer chunk;
    private String yfId;
    private Long size;
    private Integer chunks;
}
