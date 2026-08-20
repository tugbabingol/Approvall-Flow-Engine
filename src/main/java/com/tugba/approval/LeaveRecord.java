package com.tugba.approval;

import java.time.LocalDate;

public record LeaveRecord(
        String personId,
        LocalDate start,
        LocalDate end,
        String delegateId
) {

    public boolean isActiveOn(LocalDate date) {
        return !date.isBefore(start)
                && !date.isAfter(end);
    }
}