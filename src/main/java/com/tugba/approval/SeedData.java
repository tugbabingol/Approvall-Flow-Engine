package com.tugba.approval;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class SeedData {

    private SeedData() {
    }

    public static Organization organization() {

        Person ayse = new Person(
                "ayse",
                "Employee",
                "IT",
                "burak"
        );

        Person burak = new Person(
                "burak",
                "Manager",
                "IT",
                "deniz"
        );

        Person deniz = new Person(
                "deniz",
                "Manager",
                "IT",
                "fatma"
        );

        Person fatma = new Person(
                "fatma",
                "Finance Manager",
                "Finance",
                "elif"
        );

        Person elif = new Person(
                "elif",
                "CEO",
                "Management",
                null
        );

        Person can = new Person(
                "can",
                "Employee",
                "Consulting",
                "fatma"
        );

        return new Organization(
                Map.of(
                        "ayse", ayse,
                        "burak", burak,
                        "deniz", deniz,
                        "fatma", fatma,
                        "elif", elif,
                        "can", can
                ),
                Map.of(
                        "IT-OPS", "burak"
                ),
                "fatma",
                "deniz",
                "elif"
        );
    }

    public static LeaveCalendar leaveCalendar() {

        return new LeaveCalendar(
                List.of(

                        new LeaveRecord(
                                "burak",
                                LocalDate.of(2026, 3, 10),
                                LocalDate.of(2026, 3, 20),
                                "deniz"
                        ),

                        new LeaveRecord(
                                "deniz",
                                LocalDate.of(2026, 3, 16),
                                LocalDate.of(2026, 3, 20),
                                "fatma"
                        )
                )
        );
    }

    public static List<RuleSet> ruleSets() {

        RuleSet v1 = new RuleSet(

                "v1",

                LocalDate.of(2026, 1, 1),

                LocalDate.of(2026, 3, 14),

                List.of(

                        new AmountBand(
                                null,
                                new BigDecimal("9999.99"),
                                List.of(Role.MANAGER)
                        ),

                        new AmountBand(
                                new BigDecimal("10000"),
                                new BigDecimal("100000"),
                                List.of(
                                        Role.MANAGER,
                                        Role.COST_CENTER_OWNER,
                                        Role.FINANCE_MANAGER
                                )
                        ),

                        new AmountBand(
                                new BigDecimal("100000.01"),
                                null,
                                List.of(
                                        Role.MANAGER,
                                        Role.FINANCE_MANAGER,
                                        Role.CEO
                                )
                        )
                ),

                Map.of()
        );

        RuleSet v2 = new RuleSet(

                "v2",

                LocalDate.of(2026, 3, 15),

                null,

                List.of(

                        new AmountBand(
                                null,
                                new BigDecimal("14999.99"),
                                List.of(Role.MANAGER)
                        ),

                        new AmountBand(
                                new BigDecimal("15000"),
                                new BigDecimal("100000"),
                                List.of(
                                        Role.MANAGER,
                                        Role.FINANCE_MANAGER
                                )
                        ),

                        new AmountBand(
                                new BigDecimal("100000.01"),
                                null,
                                List.of(
                                        Role.MANAGER,
                                        Role.FINANCE_MANAGER,
                                        Role.CEO
                                )
                        )
                ),

                Map.of(
                        "Yazılım Lisansı",
                        List.of(Role.TECHNOLOGY_DIRECTOR)
                )
        );

        return List.of(v1, v2);
    }
}