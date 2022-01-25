
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Define a CronTrigger
 * 
 * <p>cronTriggerType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="cronTriggerType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.quartz-scheduler.org/xml/JobSchedulingData}abstractTriggerType">
 *       &lt;sequence>
 *         &lt;element name="misfire-instruction" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}cron-trigger-misfire-instructionType" minOccurs="0"/>
 *         &lt;element name="cron-expression" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}cron-expressionType"/>
 *         &lt;element name="time-zone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cronTriggerType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "misfireInstruction",
    "cronExpression",
    "timeZone"
})
public class CronTriggerType
    extends AbstractTriggerType
{

    @XmlElement(name = "misfire-instruction", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String misfireInstruction;
    @XmlElement(name = "cron-expression", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String cronExpression;
    @XmlElement(name = "time-zone", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String timeZone;

    /**
     * ��ȡmisfireInstruction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMisfireInstruction() {
        return misfireInstruction;
    }

    /**
     * ����misfireInstruction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMisfireInstruction(String value) {
        this.misfireInstruction = value;
    }

    /**
     * ��ȡcronExpression���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * ����cronExpression���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCronExpression(String value) {
        this.cronExpression = value;
    }

    /**
     * ��ȡtimeZone���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * ����timeZone���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }

}
