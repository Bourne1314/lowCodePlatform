package com.csicit.ace.data.persistent.qrtzUtils;

import java.util.List;
public class JobDataMap {

    private List<Entry> entry;

    public JobDataMap() {
        super();
    }

    public JobDataMap(List<Entry> entry) {
        this.entry = entry;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
}
