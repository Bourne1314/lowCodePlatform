package com.csicit.ace.license.core.domain;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/14 14:49
 */
@Data
public class LicenseAppDO {
    private String appId;

    private String appName;

    private Integer count;

    private LocalDate endDate;
}
