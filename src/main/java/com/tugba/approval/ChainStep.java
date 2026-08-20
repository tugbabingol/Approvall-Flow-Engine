package com.tugba.approval;

import java.util.List;

public record ChainStep(
        int order,
        String personId,
        List<Role> roles,
        String delegatedFrom,
        String escalationManagerId
) {
}