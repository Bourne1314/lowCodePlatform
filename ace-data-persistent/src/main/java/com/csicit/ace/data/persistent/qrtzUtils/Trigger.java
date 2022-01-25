package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Trigger {
    private SimpleTrigger simple;
    private CronTrigger cron;
    @XmlElement(name = "calendar-interval")
    private CalendarIntervalTrigger calendarInterval;

    public Trigger() {
        super();
    }

    public Trigger(SimpleTrigger simple, CronTrigger cron, CalendarIntervalTrigger calendarInterval) {
        this.simple = simple;
        this.cron = cron;
        this.calendarInterval = calendarInterval;
    }

    public SimpleTrigger getSimple() {
        return simple;
    }

    public void setSimple(SimpleTrigger simple) {
        this.simple = simple;
    }

    public CronTrigger getCron() {
        return cron;
    }

    public void setCron(CronTrigger cron) {
        this.cron = cron;
    }

    @XmlTransient
    public CalendarIntervalTrigger getCalendarInterval() {
        return calendarInterval;
    }

    public void setCalendarInterval(CalendarIntervalTrigger calendarInterval) {
        this.calendarInterval = calendarInterval;
    }

}
