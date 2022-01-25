package com.csicit.ace.common.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 授权视图给到前端对象
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/17 17:31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrantAuthVO extends AbstractTreeVO {

    /**
     * 是否禁用 1禁用0 不禁用
     */
    private Integer revoker;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限标识
     */
    private String code;

    /**
     * 授予权限
     */
    private boolean grantFlag;

    /**
     * 禁用权限
     */
    private boolean revokeFlag;

    /**
     * 子权限列表
     */
    @TableField(exist = false)
    private List<GrantAuthVO> children;
}
