
package com.csicit.ace.quartz.core.utils;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.csicit.ace.quartz.config package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PreProcessingCommandsTypeDeleteJob_QNAME = new QName("http://www.quartz-scheduler.org/xml/JobSchedulingData", "delete-job");
    private final static QName _PreProcessingCommandsTypeDeleteTrigger_QNAME = new QName("http://www.quartz-scheduler.org/xml/JobSchedulingData", "delete-trigger");
    private final static QName _PreProcessingCommandsTypeDeleteJobsInGroup_QNAME = new QName("http://www.quartz-scheduler.org/xml/JobSchedulingData", "delete-jobs-in-group");
    private final static QName _PreProcessingCommandsTypeDeleteTriggersInGroup_QNAME = new QName("http://www.quartz-scheduler.org/xml/JobSchedulingData", "delete-triggers-in-group");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.csicit.ace.quartz.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JobSchedulingData }
     *
     */
    public JobSchedulingData createJobSchedulingData() {
        return new JobSchedulingData();
    }

    /**
     * Create an instance of {@link PreProcessingCommandsType }
     *
     */
    public PreProcessingCommandsType createPreProcessingCommandsType() {
        return new PreProcessingCommandsType();
    }

    /**
     * Create an instance of {@link ProcessingDirectivesType }
     *
     */
    public ProcessingDirectivesType createProcessingDirectivesType() {
        return new ProcessingDirectivesType();
    }

    /**
     * Create an instance of {@link JobSchedulingData.Schedule }
     *
     */
    public JobSchedulingData.Schedule createJobSchedulingDataSchedule() {
        return new JobSchedulingData.Schedule();
    }

    /**
     * Create an instance of {@link EntryType }
     *
     */
    public EntryType createEntryType() {
        return new EntryType();
    }

    /**
     * Create an instance of {@link SimpleTriggerType }
     *
     */
    public SimpleTriggerType createSimpleTriggerType() {
        return new SimpleTriggerType();
    }

    /**
     * Create an instance of {@link CalendarIntervalTriggerType }
     *
     */
    public CalendarIntervalTriggerType createCalendarIntervalTriggerType() {
        return new CalendarIntervalTriggerType();
    }

    /**
     * Create an instance of {@link JobDataMapType }
     *
     */
    public JobDataMapType createJobDataMapType() {
        return new JobDataMapType();
    }

    /**
     * Create an instance of {@link JobDetailType }
     * 
     */
    public JobDetailType createJobDetailType() {
        return new JobDetailType();
    }

    /**
     * Create an instance of {@link CronTriggerType }
     * 
     */
    public CronTriggerType createCronTriggerType() {
        return new CronTriggerType();
    }

    /**
     * Create an instance of {@link TriggerType }
     * 
     */
    public TriggerType createTriggerType() {
        return new TriggerType();
    }

    /**
     * Create an instance of {@link PreProcessingCommandsType.DeleteJob }
     * 
     */
    public PreProcessingCommandsType.DeleteJob createPreProcessingCommandsTypeDeleteJob() {
        return new PreProcessingCommandsType.DeleteJob();
    }

    /**
     * Create an instance of {@link PreProcessingCommandsType.DeleteTrigger }
     * 
     */
    public PreProcessingCommandsType.DeleteTrigger createPreProcessingCommandsTypeDeleteTrigger() {
        return new PreProcessingCommandsType.DeleteTrigger();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PreProcessingCommandsType.DeleteJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", name = "delete-job", scope = PreProcessingCommandsType.class)
    public JAXBElement<PreProcessingCommandsType.DeleteJob> createPreProcessingCommandsTypeDeleteJob(PreProcessingCommandsType.DeleteJob value) {
        return new JAXBElement<PreProcessingCommandsType.DeleteJob>(_PreProcessingCommandsTypeDeleteJob_QNAME, PreProcessingCommandsType.DeleteJob.class, PreProcessingCommandsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PreProcessingCommandsType.DeleteTrigger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", name = "delete-trigger", scope = PreProcessingCommandsType.class)
    public JAXBElement<PreProcessingCommandsType.DeleteTrigger> createPreProcessingCommandsTypeDeleteTrigger(PreProcessingCommandsType.DeleteTrigger value) {
        return new JAXBElement<PreProcessingCommandsType.DeleteTrigger>(_PreProcessingCommandsTypeDeleteTrigger_QNAME, PreProcessingCommandsType.DeleteTrigger.class, PreProcessingCommandsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", name = "delete-jobs-in-group", scope = PreProcessingCommandsType.class)
    public JAXBElement<String> createPreProcessingCommandsTypeDeleteJobsInGroup(String value) {
        return new JAXBElement<String>(_PreProcessingCommandsTypeDeleteJobsInGroup_QNAME, String.class, PreProcessingCommandsType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData", name = "delete-triggers-in-group", scope = PreProcessingCommandsType.class)
    public JAXBElement<String> createPreProcessingCommandsTypeDeleteTriggersInGroup(String value) {
        return new JAXBElement<String>(_PreProcessingCommandsTypeDeleteTriggersInGroup_QNAME, String.class, PreProcessingCommandsType.class, value);
    }

}
