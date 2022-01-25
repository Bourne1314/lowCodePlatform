package com.csicit.ace.quartz.core.utils;

//import com.csicit.ace.quartz.config.TriggerType;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="pre-processing-commands" type="{http://www.quartz-scheduler
 *         .org/xml/JobSchedulingData}pre-processing-commandsType" minOccurs="0"/>
 *         &lt;element name="processing-directives" type="{http://www.quartz-scheduler
 *         .org/xml/JobSchedulingData}processing-directivesType" minOccurs="0"/>
 *         &lt;element name="schedule" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="job" type="{http://www.quartz-scheduler
 *                   .org/xml/JobSchedulingData}job-detailType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="trigger" type="{http://www.quartz-scheduler
 *                   .org/xml/JobSchedulingData}triggerType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "preProcessingCommandsAndProcessingDirectivesAndSchedule"
})
@XmlRootElement(name = "job-scheduling-data", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
public class JobSchedulingData {

    @XmlElements({
            @XmlElement(name = "pre-processing-commands", namespace = "http://www.quartz-scheduler" +
                    ".org/xml/JobSchedulingData", type = PreProcessingCommandsType.class),
            @XmlElement(name = "processing-directives", namespace = "http://www.quartz-scheduler" +
                    ".org/xml/JobSchedulingData", type = ProcessingDirectivesType.class),
            @XmlElement(name = "schedule", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", type
                    = JobSchedulingData.Schedule.class)
    })
    protected List<Object> preProcessingCommandsAndProcessingDirectivesAndSchedule;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Gets the value of the preProcessingCommandsAndProcessingDirectivesAndSchedule property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the
     * preProcessingCommandsAndProcessingDirectivesAndSchedule property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreProcessingCommandsAndProcessingDirectivesAndSchedule().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreProcessingCommandsType }
     * {@link ProcessingDirectivesType }
     * {@link JobSchedulingData.Schedule }
     */
    public List<Object> getPreProcessingCommandsAndProcessingDirectivesAndSchedule() {
        if (preProcessingCommandsAndProcessingDirectivesAndSchedule == null) {
            preProcessingCommandsAndProcessingDirectivesAndSchedule = new ArrayList<Object>();
        }
        return this.preProcessingCommandsAndProcessingDirectivesAndSchedule;
    }

    /**
     * Sets the value of the preProcessingCommandsAndProcessingDirectivesAndSchedule property.
     *
     * @param preProcessingCommandsAndProcessingDirectivesAndSchedule
     * @return
     * @author zuogang
     * @date 2019/8/8 8:20
     */
    public void setPreProcessingCommandsAndProcessingDirectivesAndSchedule(List<Object>
                                                                                   preProcessingCommandsAndProcessingDirectivesAndSchedule) {
        this.preProcessingCommandsAndProcessingDirectivesAndSchedule =
                preProcessingCommandsAndProcessingDirectivesAndSchedule;
    }

    /**
     * ��ȡversion���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * ����version���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     *
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="job" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}job-detailType"
     *         maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="trigger" type="{http://www.quartz-scheduler.org/xml/JobSchedulingData}triggerType"
     *         maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(name = "", propOrder = {
//        "jobAndTrigger"
//    })
    @XmlType(name = "", propOrder = {
            "job", "trigger"
    })
    public static class Schedule {

//        @XmlElements({
//            @XmlElement(name = "job", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", type =
// JobDetailType.class),
//            @XmlElement(name = "trigger", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", type
// = TriggerType.class)
//        })
//        protected List<Object> jobAndTrigger;


        @XmlElements({
                @XmlElement(name = "job", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", type =
                        JobDetailType.class)
        })
        protected List<Object> job;


        @XmlElements({
                @XmlElement(name = "trigger", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData",
                        type = TriggerType.class)
        })
        protected List<Object> trigger;

        /**
         * Gets the value of the jobAndTrigger property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the jobAndTrigger property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getJobAndTrigger().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JobDetailType }
         * {@link TriggerType }
         */
//        public List<Object> getJobAndTrigger() {
//            if (jobAndTrigger == null) {
//                jobAndTrigger = new ArrayList<Object>();
//            }
//            return this.jobAndTrigger;
//        }
        public List<Object> getTrigger() {
            if (trigger == null) {
                trigger = new ArrayList<Object>();
            }
            return this.trigger;
        }

        public void setTrigger(List<Object> trigger) {
            this.trigger = trigger;
        }

        public List<Object> getJob() {
            if (job == null) {
                job = new ArrayList<Object>();
            }
            return this.job;
        }

        public void setJob(List<Object> job) {
            this.job = job;
        }

    }

}
