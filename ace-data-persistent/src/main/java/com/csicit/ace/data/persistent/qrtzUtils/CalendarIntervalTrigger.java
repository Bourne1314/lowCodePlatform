package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;

public class CalendarIntervalTrigger extends AbstractTrigger {
    @XmlElement(name = "misfire-instruction")
    private String misfireInstruction;
    @XmlElement(name = "repeat-interval", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    private BigInteger repeatInterval;
    @XmlElement(name = "repeat-interval-unit", required = true)
    private String repeatIntervalUnit;

    public CalendarIntervalTrigger() {
        super();
    }

    public CalendarIntervalTrigger(String misfireInstruction, BigInteger repeatInterval, String repeatIntervalUnit
    ) {
        this.misfireInstruction = misfireInstruction;
        this.repeatInterval = repeatInterval;
        this.repeatIntervalUnit = repeatIntervalUnit;
    }

    @XmlTransient
    public String getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(String misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    @XmlTransient
    public BigInteger getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(BigInteger repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @XmlTransient
    public String getRepeatIntervalUnit() {
        return repeatIntervalUnit;
    }

    public void setRepeatIntervalUnit(String repeatIntervalUnit) {
        this.repeatIntervalUnit = repeatIntervalUnit;
    }

}
