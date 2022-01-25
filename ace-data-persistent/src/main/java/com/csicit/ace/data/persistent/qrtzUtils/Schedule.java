package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schedule")
public class Schedule {

    private List<JobDetail> job;

    private List<Trigger> trigger;
//
    public Schedule() {
        super();
    }

    public Schedule(List<JobDetail> job, List<Trigger> trigger) {
        this.job = job;
        this.trigger = trigger;
    }
    public List<JobDetail> getJob() {
        return job;
    }

    public void setJob(List<JobDetail> job) {
        this.job = job;
    }
    public List<Trigger> getTrigger() {
        return trigger;
    }

    public void setTrigger(List<Trigger> trigger) {
        this.trigger = trigger;
    }

}
