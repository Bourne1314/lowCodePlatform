
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>processing-directivesType complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="processing-directivesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="overwrite-existing-data" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ignore-duplicates" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processing-directivesType", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", propOrder = {
    "overwriteExistingData",
    "ignoreDuplicates"
})
public class ProcessingDirectivesType {

    @XmlElement(name = "overwrite-existing-data", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", defaultValue = "true")
    protected Boolean overwriteExistingData;
    @XmlElement(name = "ignore-duplicates", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", defaultValue = "false")
    protected Boolean ignoreDuplicates;

    /**
     * ��ȡoverwriteExistingData���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOverwriteExistingData() {
        return overwriteExistingData;
    }

    /**
     * ����overwriteExistingData���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverwriteExistingData(Boolean value) {
        this.overwriteExistingData = value;
    }

    /**
     * ��ȡignoreDuplicates���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIgnoreDuplicates() {
        return ignoreDuplicates;
    }

    /**
     * ����ignoreDuplicates���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreDuplicates(Boolean value) {
        this.ignoreDuplicates = value;
    }

}
