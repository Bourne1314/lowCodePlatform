
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;


/**
 * Common Trigger definitions
 * 
 * <p>abstractTriggerType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="abstractTriggerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="job-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="job-group" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="calendar-name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="job-data-map" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}job-data-mapType" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;choice>
 *             &lt;element name="start-time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *             &lt;element name="start-time-seconds-in-future" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *           &lt;/choice>
 *           &lt;element name="end-time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractTriggerType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "name",
    "group",
    "description",
    "jobName",
    "jobGroup",
    "priority",
    "calendarName",
    "jobDataMap",
    "startTime",
    "startTimeSecondsInFuture",
    "endTime"
})
@XmlSeeAlso({
    SimpleTriggerType.class,
    CalendarIntervalTriggerType.class,
    CronTriggerType.class
})
public abstract class AbstractTriggerType {

    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String name;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String group;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String description;
    @XmlElement(name = "job-name", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String jobName;
    @XmlElement(name = "job-group", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String jobGroup;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger priority;
    @XmlElement(name = "calendar-name", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String calendarName;
    @XmlElement(name = "job-data-map", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected JobDataMapType jobDataMap;
    @XmlElement(name = "start-time", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startTime;
    @XmlElement(name = "start-time-seconds-in-future", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger startTimeSecondsInFuture;
    @XmlElement(name = "end-time", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endTime;

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * ��ȡgroup���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * ����group���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * ��ȡdescription���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * ����description���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * ��ȡjobName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * ����jobName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobName(String value) {
        this.jobName = value;
    }

    /**
     * ��ȡjobGroup���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * ����jobGroup���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobGroup(String value) {
        this.jobGroup = value;
    }

    /**
     * ��ȡpriority���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * ����priority���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
    }

    /**
     * ��ȡcalendarName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * ����calendarName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalendarName(String value) {
        this.calendarName = value;
    }

    /**
     * ��ȡjobDataMap���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JobDataMapType }
     *     
     */
    public JobDataMapType getJobDataMap() {
        return jobDataMap;
    }

    /**
     * ����jobDataMap���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JobDataMapType }
     *     
     */
    public void setJobDataMap(JobDataMapType value) {
        this.jobDataMap = value;
    }

    /**
     * ��ȡstartTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    /**
     * ����startTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartTime(XMLGregorianCalendar value) {
        this.startTime = value;
    }

    /**
     * ��ȡstartTimeSecondsInFuture���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStartTimeSecondsInFuture() {
        return startTimeSecondsInFuture;
    }

    /**
     * ����startTimeSecondsInFuture���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStartTimeSecondsInFuture(BigInteger value) {
        this.startTimeSecondsInFuture = value;
    }

    /**
     * ��ȡendTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    /**
     * ����endTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndTime(XMLGregorianCalendar value) {
        this.endTime = value;
    }

}
