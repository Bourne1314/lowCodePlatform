package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author yansiyang

 * @version V1.0
 * @date 2019/10/11 18:28
 */
@Configuration
@ConfigurationProperties("ace.objects")
public class PersonIdTypeConfig {

    private List<BdPersonIdTypeDO> personIdTypes;

    public List<BdPersonIdTypeDO> getPersonIdTypes() {
        return personIdTypes;
    }

    public void setPersonIdTypes(List<BdPersonIdTypeDO> personIdTypes) {
        this.personIdTypes = personIdTypes;
    }
}
