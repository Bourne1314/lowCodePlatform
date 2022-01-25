package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(propOrder = {"schedule"})
@XmlRootElement(name = "job-scheduling-data", namespace = "http://www.quartz-scheduler.org/xml/JobSchedulingData")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class JobScheduling implements Serializable {
    private Schedule schedule;
    public JobScheduling() { }
    public JobScheduling(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
}
