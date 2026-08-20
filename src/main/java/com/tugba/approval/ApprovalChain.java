package com.tugba.approval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ApprovalChain(
        String requestId,
        String ruleSetVersion,
        LocalDate submittedOn,
        List<ChainStep> steps,
        List<Role> skippedRequesterRoles,
        List<String> notes,
        Map<String, Person> orgSnapshot
) {
}