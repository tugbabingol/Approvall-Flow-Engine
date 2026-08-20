package com.tugba.approval;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public final class ApprovalEngine {

    private ApprovalEngine() {
    }

    public static ApprovalChain resolveChain(
            Request request,
            List<RuleSet> ruleSets,
            Organization organization,
            LeaveCalendar leaveCalendar
    ) {

        RuleSet ruleSet =
                findRuleSet(ruleSets, request.submittedOn());

        List<Role> roles =
                findRoles(ruleSet, request);

        List<ChainStep> steps = new ArrayList<>();
        List<Role> skippedRoles = new ArrayList<>();
        List<String> notes = new ArrayList<>();

        for (Role role : roles) {

            String personId =
                    findPersonForRole(
                            role,
                            request,
                            organization
                    );

            if (personId == null) {
                notes.add(role + " adimi atlandi.");
                continue;
            }

            if (personId.equals(request.requesterId())) {
                skippedRoles.add(role);

                notes.add(
                        role +
                                " adimi atlandi: talep sahibi kendi talebini onaylayamaz."
                );

                continue;
            }

            String actingPerson =
                    findDelegate(
                            personId,
                            leaveCalendar,
                            request.submittedOn(),
                            new HashSet<>()
                    );

            if (actingPerson.equals(request.requesterId())) {
                skippedRoles.add(role);

                notes.add(
                        role +
                                " adimi atlandi: vekil talep sahibinin kendisi."
                );

                continue;
            }

            addOrMergeStep(
                    steps,
                    organization,
                    personId,
                    actingPerson,
                    role
            );
        }

        return new ApprovalChain(
                request.id(),
                ruleSet.version(),
                request.submittedOn(),
                steps,
                skippedRoles,
                notes,
                organization.people()
        );
    }

    private static RuleSet findRuleSet(
            List<RuleSet> ruleSets,
            LocalDate date
    ) {

        RuleSet result = null;

        for (RuleSet ruleSet : ruleSets) {

            if (!ruleSet.isActiveOn(date)) {
                continue;
            }

            if (result != null) {
                throw new EngineException(
                        "Birden fazla kural seti aktif: "
                                + date
                );
            }

            result = ruleSet;
        }

        if (result == null) {
            throw new EngineException(
                    "Bu tarih için kural seti bulunamadı: "
                            + date
            );
        }

        return result;
    }

    private static List<Role> findRoles(
            RuleSet ruleSet,
            Request request
    ) {

        AmountBand band = null;

        for (AmountBand amountBand : ruleSet.amountBands()) {

            if (amountBand.matches(request.amount())) {
                band = amountBand;
                break;
            }
        }

        if (band == null) {
            throw new EngineException(
                    "Tutar için uygun bant bulunamadı."
            );
        }

        List<Role> roles =
                new ArrayList<>(band.roles());

        List<Role> categoryRoles =
                ruleSet.requiredRolesByCategory()
                        .getOrDefault(
                                request.category(),
                                List.of()
                        );

        for (Role role : categoryRoles) {

            if (!roles.contains(role)) {
                roles.add(role);
            }
        }

        roles.sort(
                Comparator.comparingInt(
                        Role.CANONICAL_ORDER::indexOf
                )
        );

        return roles;
    }

    private static String findPersonForRole(
            Role role,
            Request request,
            Organization organization
    ) {

        return switch (role) {

            case MANAGER ->
                    organization
                            .get(request.requesterId())
                            .managerId();

            case COST_CENTER_OWNER -> {

                if (request.costCenter() == null
                        || request.costCenter().isBlank()) {
                    yield null;
                }

                yield organization
                        .costCenterOwners()
                        .get(request.costCenter());
            }

            case FINANCE_MANAGER ->
                    organization.financeManagerId();

            case TECHNOLOGY_DIRECTOR ->
                    organization.technologyDirectorId();

            case CEO ->
                    organization.ceoId();
        };
    }

    private static String findDelegate(
            String personId,
            LeaveCalendar leaveCalendar,
            LocalDate date,
            Set<String> visited
    ) {

        if (!visited.add(personId)) {
            throw new EngineException(
                    "Vekalet döngüsü bulundu: "
                            + personId
            );
        }

        LeaveRecord leave =
                leaveCalendar.activeLeave(
                        personId,
                        date
                );

        if (leave == null) {
            return personId;
        }

        return findDelegate(
                leave.delegateId(),
                leaveCalendar,
                date,
                visited
        );
    }

    private static void addOrMergeStep(
            List<ChainStep> steps,
            Organization organization,
            String originalPerson,
            String actingPerson,
            Role role
    ) {

        for (int i = 0; i < steps.size(); i++) {

            ChainStep step = steps.get(i);

            if (step.personId().equals(actingPerson)) {

                List<Role> roles =
                        new ArrayList<>(step.roles());

                if (!roles.contains(role)) {
                    roles.add(role);
                }

                steps.set(
                        i,
                        new ChainStep(
                                step.order(),
                                step.personId(),
                                roles,
                                step.delegatedFrom(),
                                step.escalationManagerId()
                        )
                );

                return;
            }
        }

        Person person =
                organization.get(actingPerson);

        steps.add(
                new ChainStep(
                        steps.size() + 1,
                        actingPerson,
                        List.of(role),
                        originalPerson.equals(actingPerson)
                                ? null
                                : originalPerson,
                        person.managerId()
                )
        );
    }
}