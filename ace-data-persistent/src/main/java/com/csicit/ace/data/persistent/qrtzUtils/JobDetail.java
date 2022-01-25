package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.*;


@XmlType(propOrder={"name", "group", "description", "jobClass", "durability", "recover", "jobDataMap"})
public class JobDetail {
    @XmlElement
    private String name;

    private String group;

    private String description;
    @XmlElement(name = "job-class")
    private String jobClass;

    private Boolean durability;

    private Boolean recover;
    @XmlElement(name = "job-data-map")
    private JobDataMap jobDataMap;

    public JobDetail() {
        super();
    }

    public JobDetail(String name, String group, String description, String jobClass, Boolean durability, Boolean
            recover, JobDataMap jobDataMap) {
        this.name = name;
        this.group = group;
        this.description = description;
        this.jobClass = jobClass;
        this.durability = durability;
        this.recover = recover;
        this.jobDataMap = jobDataMap;
    }
    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Boolean getDurability() {
        return durability;
    }

    public void setDurability(Boolean durability) {
        this.durability = durability;
    }

    public Boolean getRecover() {
        return recover;
    }

    public void setRecover(Boolean recover) {
        this.recover = recover;
    }

    @XmlTransient
    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
}
