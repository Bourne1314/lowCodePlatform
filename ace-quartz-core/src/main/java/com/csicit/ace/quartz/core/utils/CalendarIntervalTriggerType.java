
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * Define a DateIntervalTrigger
 * 
 * <p>calendarIntervalTriggerType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="calendarIntervalTriggerType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.quartz-scheduler.org/xml/JobSchedulingData}abstractTriggerType">
 *       &lt;sequence>
 *         &lt;element name="misfire-instruction" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}date-interval-trigger-misfire-instructionType" minOccurs="0"/>
 *         &lt;element name="repeat-interval" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="repeat-interval-unit" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}interval-unitType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calendarIntervalTriggerType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "misfireInstruction",
    "repeatInterval",
    "repeatIntervalUnit"
})
public class CalendarIntervalTriggerType
    extends AbstractTriggerType
{

    @XmlElement(name = "misfire-instruction", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String misfireInstruction;
    @XmlElement(name = "repeat-interval", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger repeatInterval;
    @XmlElement(name = "repeat-interval-unit", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", required = true)
    protected String repeatIntervalUnit;

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
     * ��ȡrepeatInterval���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRepeatInterval() {
        return repeatInterval;
    }

    /**
     * ����repeatInterval���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRepeatInterval(BigInteger value) {
        this.repeatInterval = value;
    }

    /**
     * ��ȡrepeatIntervalUnit���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepeatIntervalUnit() {
        return repeatIntervalUnit;
    }

    /**
     * ����repeatIntervalUnit���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepeatIntervalUnit(String value) {
        this.repeatIntervalUnit = value;
    }

}
