
package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(propOrder={"name", "group", "description", "jobName", "jobGroup","startTime","endTime", "cronExpression", "misfireInstruction", "timeZone"})
public class CronTrigger {
    @XmlElement(name = "misfire-instruction")
    private String misfireInstruction;
    @XmlElement(name = "cron-expression", required = true)
    private String cronExpression;
    @XmlElement(name = "time-zone")
    private String timeZone;

    @XmlElement(required = true)
    private String name;
    private String group;
    private String description;
    @XmlElement(name = "job-name", required = true)
    private String jobName;
    @XmlElement(name = "job-group")
    private String jobGroup;
    @XmlElement(name = "start-time")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar startTime;
    @XmlElement(name = "end-time")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar endTime;


    public CronTrigger() {
        super();
    }

    public CronTrigger(String misfireInstruction, String cronExpression, String timeZone,String name, String group, String description, String jobName, String jobGroup,  XMLGregorianCalendar startTime,  XMLGregorianCalendar endTime) {
        this.misfireInstruction = misfireInstruction;
        this.cronExpression = cronExpression;
        this.timeZone = timeZone;
        this.name = name;
        this.group = group;
        this.description = description;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @XmlTransient
    public String getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(String misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }
    @XmlTransient
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    @XmlTransient
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    @XmlTransient
    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @XmlTransient
    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(XMLGregorianCalendar startTime) {
        this.startTime = startTime;
    }
    @XmlTransient
    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(XMLGregorianCalendar endTime) {
        this.endTime = endTime;
    }

}
