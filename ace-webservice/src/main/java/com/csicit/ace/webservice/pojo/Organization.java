package com.csicit.ace.webservice.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 14:10
 */
public class Organization {

    public String getDeptcode() {
        return deptcode;
    }

    @XmlElement(name = "DEPTCODE")
    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getDeptfullname() {
        return deptfullname;
    }

    @XmlElement(name = "DEPTFULLNAME")
    public void setDeptfullname(String deptfullname) {
        this.deptfullname = deptfullname;
    }

    public String getParentdept() {
        return parentdept;
    }

    @XmlElement(name = "PARENTDEPT")
    public void setParentdept(String parentdept) {
        this.parentdept = parentdept;
    }

    public String getParentdeptDeptfullname() {
        return parentdeptDeptfullname;
    }
    @XmlElement(name = "PARENTDEPT_DEPTFULLNAME")
    public void setParentdeptDeptfullname(String parentdeptDeptfullname) {
        this.parentdeptDeptfullname = parentdeptDeptfullname;
    }

    public String getEstablishdate() {
        return establishdate;
    }

    @XmlElement(name = "ESTABLISHDATE")
    public void setEstablishdate(String establishdate) {
        this.establishdate = establishdate;
    }

    public String getRevocationdate() {
        return revocationdate;
    }

    @XmlElement(name = "REVOCATIONDATE")
    public void setRevocationdate(String revocationdate) {
        this.revocationdate = revocationdate;
    }

    public String getOrgdescription() {
        return orgdescription;
    }

    @XmlElement(name = "ORGDESCRIPTION")
    public void setOrgdescription(String orgdescription) {
        this.orgdescription = orgdescription;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    @XmlElement(name = "DATASTATUS")
    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getStopReason() {
        return stopReason;
    }

    @XmlElement(name = "STOPREASON")
    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    /**
     * ????????????
     */
    private String deptcode;

    /**
     * ??????????????????
     */
    private String deptfullname;

    /**
     * ??????????????????
     */
    private String parentdept;

    /**
     * ??????????????????
     */
    private String parentdeptDeptfullname;

    /**
     * ????????????
     */
    private String establishdate;


    /**
     * ????????????
     */
    private String revocationdate;

    /**
     * ??????
     */
    private String orgdescription;

    /**
     * ????????????
     */
    private String dataStatus;

    /**
     * ????????????
     */
    private String stopReason;

}
