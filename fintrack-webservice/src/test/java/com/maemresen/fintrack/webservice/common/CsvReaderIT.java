package com.maemresen.fintrack.webservice.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvReaderIT {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class FootballPlayer {
        private int id;
        private String name;
        private String surname;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class FootballPlayerWithCustomNameField {
        private int id;

        private String name;

        @CsvHeader("surname")
        private String customSurnameField;
    }

    @ParameterizedTest(name = "Read Test - Players[{0}]=[{1} {2} {3}]")
    @CsvSource(value = {
        "0,1,Lionel,Messi",
        "1,2,Cristiano,Ronaldo",
    }, delimiter = ',')
    void readTest(final int index, final int id, final String name, final String surname) throws Exception {
        final var csvFile = "csv/football-players.csv";
        final var csvResource = new ClassPathResource(csvFile);
        try (final var inputStream = csvResource.getInputStream()) {
            final var footballPlayers = CsvReader.read(inputStream, FootballPlayer.class);

            assertTrue(CollectionUtils.isNotEmpty(footballPlayers));

            final var footballPlayer = footballPlayers.get(index);
            assertNotNull(footballPlayer);
            assertEquals(id, footballPlayer.getId());
            assertEquals(name, footballPlayer.getName());
            assertEquals(surname, footballPlayer.getSurname());
        }
    }

    @ParameterizedTest(name = "Read Test [Custom Field Name] - Players[{0}]=[{1} {2} {3}]")
    @CsvSource(value = {
        "0,1,Lionel,Messi",
        "1,2,Cristiano,Ronaldo",
    }, delimiter = ',')
    void readTestWithCustomFieldName(final int index, final int id, final String name, final String surname) throws Exception {
        final var csvFile = "csv/football-players.csv";
        final var csvResource = new ClassPathResource(csvFile);
        try (final var inputStream = csvResource.getInputStream()) {
            final var footballPlayers = CsvReader.read(inputStream, FootballPlayerWithCustomNameField.class);

            assertTrue(CollectionUtils.isNotEmpty(footballPlayers));

            final var footballPlayer = footballPlayers.get(index);
            assertNotNull(footballPlayer);
            assertEquals(id, footballPlayer.getId());
            assertEquals(name, footballPlayer.getName());
            assertEquals(surname, footballPlayer.getCustomSurnameField());
        }
    }
}
