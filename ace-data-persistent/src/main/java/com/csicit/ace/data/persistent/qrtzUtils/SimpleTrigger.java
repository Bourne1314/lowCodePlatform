
package com.csicit.ace.data.persistent.qrtzUtils;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;

public class SimpleTrigger extends AbstractTrigger {

    @XmlElement(name = "misfire-instruction")
    private String misfireInstruction;
    @XmlElement(name = "repeat-count")
    private BigInteger repeatCount;
    @XmlElement(name = "repeat-interval")
    @XmlSchemaType(name = "nonNegativeInteger")
    private BigInteger repeatInterval;

    public SimpleTrigger() {
        super();
    }

    public SimpleTrigger(String misfireInstruction, BigInteger repeatCount, BigInteger repeatInterval) {
        this.misfireInstruction = misfireInstruction;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
    }

    @XmlTransient
    public String getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(String misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    @XmlTransient
    public BigInteger getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(BigInteger repeatCount) {
        this.repeatCount = repeatCount;
    }

    @XmlTransient
    public BigInteger getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(BigInteger repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

}
