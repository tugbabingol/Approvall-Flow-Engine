package com.tugba.approval;

import java.math.BigDecimal;
import java.util.List;

public record AmountBand(
        BigDecimal minInclusive,
        BigDecimal maxInclusive,
        List<Role> roles
) {

    public boolean matches(BigDecimal amount) {

        if (minInclusive != null &&
                amount.compareTo(minInclusive) < 0) {
            return false;
        }

        if (maxInclusive != null &&
                amount.compareTo(maxInclusive) > 0) {
            return false;
        }

        return true;
    }
}