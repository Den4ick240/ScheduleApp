package com.nsu.ccfit.nsuschedule.scheduleabstract;

public interface ScheduleItem {
    String getDescription();
    String getLocation();
    String getSummary();
    String getStartTime();
    String getEndTime();
    Parity getParity();
    boolean isVisible();
}
