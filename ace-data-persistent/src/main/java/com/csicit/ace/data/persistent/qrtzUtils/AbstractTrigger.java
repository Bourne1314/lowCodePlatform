package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

public class AbstractTrigger {

    @XmlElement(required = true)
    private String name;
    private String group;
    private String description;
    @XmlElement(name = "job-name", required = true)
    private String jobName;
    @XmlElement(name = "job-group")
    private String jobGroup;
    @XmlSchemaType(name = "nonNegativeInteger")
    private BigInteger priority;
    @XmlElement(name = "calendar-name")
    private String calendarName;
    @XmlElement(name = "job-data-map")
    private JobDataMap jobDataMap;
    @XmlElement(name = "start-time")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar startTime;
    @XmlElement(name = "start-time-seconds-in-future")
    @XmlSchemaType(name = "nonNegativeInteger")
    private BigInteger startTimeSecondsInFuture;
    @XmlElement(name = "end-time")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar endTime;

    public AbstractTrigger() {
        super();
    }

    public AbstractTrigger(String name, String group, String description, String jobName, String jobGroup, BigInteger
            priority, String calendarName, JobDataMap jobDataMap, XMLGregorianCalendar startTime, BigInteger
                                   startTimeSecondsInFuture, XMLGregorianCalendar endTime) {
        this.name = name;
        this.group = group;
        this.description = description;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.priority = priority;
        this.calendarName = calendarName;
        this.jobDataMap = jobDataMap;
        this.startTime = startTime;
        this.startTimeSecondsInFuture = startTimeSecondsInFuture;
        this.endTime = endTime;
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
    public BigInteger getPriority() {
        return priority;
    }

    public void setPriority(BigInteger priority) {
        this.priority = priority;
    }
    @XmlTransient
    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }
    @XmlTransient
    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
    @XmlTransient
    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(XMLGregorianCalendar startTime) {
        this.startTime = startTime;
    }
    @XmlTransient
    public BigInteger getStartTimeSecondsInFuture() {
        return startTimeSecondsInFuture;
    }

    public void setStartTimeSecondsInFuture(BigInteger startTimeSecondsInFuture) {
        this.startTimeSecondsInFuture = startTimeSecondsInFuture;
    }
    @XmlTransient
    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(XMLGregorianCalendar endTime) {
        this.endTime = endTime;
    }
}
