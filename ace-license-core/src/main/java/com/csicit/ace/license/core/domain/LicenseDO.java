package com.csicit.ace.license.core.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/14 14:21
 */
@Data
public class LicenseDO {
    private String appId;

    private String uuId;

    private LocalDateTime registerTime;
}
