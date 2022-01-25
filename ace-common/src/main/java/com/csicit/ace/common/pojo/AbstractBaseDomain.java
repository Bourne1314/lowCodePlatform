package com.csicit.ace.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * 通用的领域模型
 *
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
@Data
public abstract class AbstractBaseDomain implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 数据版本
     */
    @Version
    private Integer dataVersion;
}
