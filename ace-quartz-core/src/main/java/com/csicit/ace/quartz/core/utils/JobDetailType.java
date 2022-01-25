
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Define a JobDetail
 * 
 * <p>job-detailType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="job-detailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="job-class" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="durability" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="recover" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;/sequence>
 *         &lt;element name="job-data-map" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}job-data-mapType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "job-detailType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "name",
    "group",
    "description",
    "jobClass",
    "durability",
    "recover",
    "jobDataMap"
})
public class JobDetailType {

    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String name;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String group;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String description;
    @XmlElement(name = "job-class", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String jobClass;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected Boolean durability;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected Boolean recover;
    @XmlElement(name = "job-data-map", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected JobDataMapType jobDataMap;

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
     * ��ȡjobClass���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * ����jobClass���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setJobClass(String value) {
        this.jobClass = value;
    }

    /**
     * ��ȡdurability���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDurability() {
        return durability;
    }

    /**
     * ����durability���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDurability(Boolean value) {
        this.durability = value;
    }

    /**
     * ��ȡrecover���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRecover() {
        return recover;
    }

    /**
     * ����recover���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRecover(Boolean value) {
        this.recover = value;
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

}
