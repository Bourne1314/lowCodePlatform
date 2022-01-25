
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Define a Trigger
 * 
 * <p>triggerType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="triggerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="simple" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}simpleTriggerType"/>
 *         &lt;element name="cron" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}cronTriggerType"/>
 *         &lt;element name="calendar-interval" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}calendarIntervalTriggerType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "triggerType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "simple",
    "cron",
    "calendarInterval"
})
public class TriggerType {

    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected SimpleTriggerType simple;
    @XmlElement(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected CronTriggerType cron;
    @XmlElement(name = "calendar-interval", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected CalendarIntervalTriggerType calendarInterval;

    /**
     * ��ȡsimple���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link SimpleTriggerType }
     *
     */
    public SimpleTriggerType getSimple() {
        return simple;
    }

    /**
     * ����simple���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link SimpleTriggerType }
     *
     */
    public void setSimple(SimpleTriggerType value) {
        this.simple = value;
    }

    /**
     * ��ȡcron���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link CronTriggerType }
     *
     */
    public CronTriggerType getCron() {
        return cron;
    }

    /**
     * ����cron���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link CronTriggerType }
     *
     */
    public void setCron(CronTriggerType value) {
        this.cron = value;
    }

    /**
     * ��ȡcalendarInterval���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link CalendarIntervalTriggerType }
     *
     */
    public CalendarIntervalTriggerType getCalendarInterval() {
        return calendarInterval;
    }

    /**
     * ����calendarInterval���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link CalendarIntervalTriggerType }
     *     
     */
    public void setCalendarInterval(CalendarIntervalTriggerType value) {
        this.calendarInterval = value;
    }

}
