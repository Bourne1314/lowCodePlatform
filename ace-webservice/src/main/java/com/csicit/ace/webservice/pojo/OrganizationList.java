package com.csicit.ace.webservice.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 14:32
 */
@XmlRootElement(name = "LIST")
public class OrganizationList {
    private List<Organization> organizations;

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @XmlElement(name = "ORGANIZATION")
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
