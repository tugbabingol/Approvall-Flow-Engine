package com.tugba.approval;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ScenarioRunner {

    public static void main(String[] args) {

        Organization organization =
                SeedData.organization();

        LeaveCalendar leaves =
                SeedData.leaveCalendar();

        var rules =
                SeedData.ruleSets();

        scenario1(organization, leaves, rules);
        scenario2(organization, leaves, rules);
        scenario3(organization, leaves, rules);
        scenario4(organization, leaves, rules);
        scenario5(organization, leaves, rules);
    }

    private static void scenario1(
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        Request request = new Request(
                "R1",
                "ayse",
                new BigDecimal("8000"),
                "Kırtasiye",
                LocalDate.of(2026, 3, 5),
                null
        );

        print(
                "Senaryo 1",
                request,
                organization,
                leaves,
                rules
        );
    }

    private static void scenario2(
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        Request request = new Request(
                "R2",
                "ayse",
                new BigDecimal("45000"),
                "IT-OPS",
                LocalDate.of(2026, 3, 12),
                "IT-OPS"
        );

        print(
                "Senaryo 2",
                request,
                organization,
                leaves,
                rules
        );
    }

    private static void scenario3(
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        Request request = new Request(
                "R3",
                "burak",
                new BigDecimal("60000"),
                "IT-OPS",
                LocalDate.of(2026, 3, 12),
                "IT-OPS"
        );

        print(
                "Senaryo 3",
                request,
                organization,
                leaves,
                rules
        );
    }

    private static void scenario4(
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        Request request = new Request(
                "R4",
                "can",
                new BigDecimal("120000"),
                "Danışmanlık",
                LocalDate.of(2026, 3, 13),
                null
        );

        print(
                "Senaryo 4",
                request,
                organization,
                leaves,
                rules
        );
    }

    private static void scenario5(
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        Request request = new Request(
                "R5",
                "ayse",
                new BigDecimal("14000"),
                "Yazılım Lisansı",
                LocalDate.of(2026, 3, 16),
                null
        );

        print(
                "Senaryo 5 - Resubmit",
                request,
                organization,
                leaves,
                rules
        );
    }

    private static void print(
            String name,
            Request request,
            Organization organization,
            LeaveCalendar leaves,
            java.util.List<RuleSet> rules
    ) {

        ApprovalChain chain =
                ApprovalEngine.resolveChain(
                        request,
                        rules,
                        organization,
                        leaves
                );

        System.out.println();
        System.out.println(name);

        for (ChainStep step : chain.steps()) {

            System.out.println(
                    step.order()
                            + ". "
                            + step.personId()
                            + " "
                            + step.roles()
            );
        }

        if (!chain.notes().isEmpty()) {
            System.out.println(
                    "Notlar: " + chain.notes()
            );
        }
    }
}