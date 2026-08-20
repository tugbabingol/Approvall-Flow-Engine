package com.tugba.approval;

import java.time.LocalDate;
import java.util.List;

public class LeaveCalendar {

    private final List<LeaveRecord> records;

    public LeaveCalendar(List<LeaveRecord> records) {
        this.records = List.copyOf(records);
    }

    public LeaveRecord activeLeave(String personId, LocalDate date) {

        for (LeaveRecord record : records) {

            if (record.personId().equals(personId)
                    && record.isActiveOn(date)) {
                return record;
            }
        }

        return null;
    }
}