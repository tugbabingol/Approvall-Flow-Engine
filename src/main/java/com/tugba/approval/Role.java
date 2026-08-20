package com.tugba.approval;

import java.util.List;

public enum Role {

    MANAGER,
    COST_CENTER_OWNER,
    FINANCE_MANAGER,
    TECHNOLOGY_DIRECTOR,
    CEO;

    public static final List<Role> CANONICAL_ORDER = List.of(
            MANAGER,
            COST_CENTER_OWNER,
            FINANCE_MANAGER,
            TECHNOLOGY_DIRECTOR,
            CEO
    );
}