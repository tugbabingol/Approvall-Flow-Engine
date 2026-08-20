package com.tugba.approval;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApprovalEngineTest {

    private final Organization organization =
            SeedData.organization();

    private final LeaveCalendar leaves =
            SeedData.leaveCalendar();

    private final List<RuleSet> rules =
            SeedData.ruleSets();


    @Test
    void scenario1_shouldGoToBurak() {

        Request request = new Request(
                "R1",
                "ayse",
                new BigDecimal("8000"),
                "Kırtasiye",
                LocalDate.of(2026, 3, 5),
                null
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                List.of("burak"),
                personIds(chain)
        );
    }


    @Test
    void scenario2_shouldMergeBurakRolesIntoDeniz() {

        Request request = new Request(
                "R2",
                "ayse",
                new BigDecimal("45000"),
                "IT-OPS",
                LocalDate.of(2026, 3, 12),
                "IT-OPS"
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                List.of("deniz", "fatma"),
                personIds(chain)
        );

        assertEquals(
                List.of(
                        Role.MANAGER,
                        Role.COST_CENTER_OWNER
                ),
                chain.steps().get(0).roles()
        );
    }


    @Test
    void scenario3_shouldSkipRequesterAsCostCenterOwner() {

        Request request = new Request(
                "R3",
                "burak",
                new BigDecimal("60000"),
                "IT-OPS",
                LocalDate.of(2026, 3, 12),
                "IT-OPS"
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                List.of("deniz", "fatma"),
                personIds(chain)
        );

        assertEquals(
                List.of(Role.COST_CENTER_OWNER),
                chain.skippedRequesterRoles()
        );
    }


    @Test
    void scenario4_shouldUseV1EvenAfterV2Starts() {

        Request request = new Request(
                "R4",
                "can",
                new BigDecimal("120000"),
                "Danışmanlık",
                LocalDate.of(2026, 3, 13),
                null
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                "v1",
                chain.ruleSetVersion()
        );

        assertEquals(
                List.of("fatma", "elif"),
                personIds(chain)
        );
    }


    @Test
    void scenario5_resubmitShouldUseV2() {

        Request request = new Request(
                "R5",
                "ayse",
                new BigDecimal("14000"),
                "Yazılım Lisansı",
                LocalDate.of(2026, 3, 16),
                null
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                "v2",
                chain.ruleSetVersion()
        );

        assertEquals(
                List.of("fatma"),
                personIds(chain)
        );
    }


    @Test
    void scenario6_shouldKeepOldOrganizationSnapshot() {

        Request request = new Request(
                "R6",
                "ayse",
                new BigDecimal("45000"),
                "IT-OPS",
                LocalDate.of(2026, 3, 18),
                "IT-OPS"
        );

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        assertEquals(
                "fatma",
                chain.orgSnapshot()
                        .get("deniz")
                        .managerId()
        );
    }

    private List<String> personIds(
            ApprovalChain chain
    ) {

        return chain.steps()
                .stream()
                .map(ChainStep::personId)
                .toList();
    }
}