package com.tugba.approval;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Request(
        String id,
        String requesterId,
        BigDecimal amount,
        String category,
        LocalDate submittedOn,
        String costCenter
) {
}