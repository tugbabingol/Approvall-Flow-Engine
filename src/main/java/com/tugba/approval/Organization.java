package com.tugba.approval;

import java.util.Map;

public record Organization(
        Map<String, Person> people,
        Map<String, String> costCenterOwners,
        String financeManagerId,
        String technologyDirectorId,
        String ceoId
) {

    public Person get(String personId) {

        Person person = people.get(personId);

        if (person == null) {
            throw new EngineException(
                    "Kisi bulunamadi: " + personId
            );
        }

        return person;
    }
}