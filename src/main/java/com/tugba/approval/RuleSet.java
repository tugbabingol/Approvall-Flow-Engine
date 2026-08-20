package com.tugba.approval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record RuleSet(
        String version,
        LocalDate validFrom,
        LocalDate validTo,
        List<AmountBand> amountBands,
        Map<String, List<Role>> requiredRolesByCategory
) {

    public boolean isActiveOn(LocalDate date) {

        boolean startsBefore =
                !date.isBefore(validFrom);

        boolean endsAfter =
                validTo == null || !date.isAfter(validTo);

        return startsBefore && endsAfter;
    }
}