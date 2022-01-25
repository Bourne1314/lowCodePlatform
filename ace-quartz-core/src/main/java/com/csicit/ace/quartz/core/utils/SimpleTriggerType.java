
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * Define a SimpleTrigger
 * 
 * <p>simpleTriggerType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="simpleTriggerType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.quartz-scheduler.org/xml/JobSchedulingData}abstractTriggerType">
 *       &lt;sequence>
 *         &lt;element name="misfire-instruction" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}simple-trigger-misfire-instructionType" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="repeat-count" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}repeat-countType"/>
 *           &lt;element name="repeat-interval" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleTriggerType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "misfireInstruction",
    "repeatCount",
    "repeatInterval"
})
public class SimpleTriggerType
    extends AbstractTriggerType
{

    @XmlElement(name = "misfire-instruction", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected String misfireInstruction;
    @XmlElement(name = "repeat-count", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    protected BigInteger repeatCount;
    @XmlElement(name = "repeat-interval", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger repeatInterval;

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
     * ��ȡrepeatCount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRepeatCount() {
        return repeatCount;
    }

    /**
     * ����repeatCount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRepeatCount(BigInteger value) {
        this.repeatCount = value;
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

}
