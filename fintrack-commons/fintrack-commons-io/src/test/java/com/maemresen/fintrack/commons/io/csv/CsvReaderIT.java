package com.maemresen.fintrack.commons.io.csv;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.util.stream.Stream;

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

    record FootballPlayerArgument(int index, int id, String name, String surname) {
    }

    static class PersonArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                new FootballPlayerArgument(0, 1, "Lionel", "Messi"),
                new FootballPlayerArgument(1, 2, "Cristiano", "Ronaldo")
            ).map(Arguments::of);
        }
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

    @ParameterizedTest(name = "Read Test - Players[{0}]")
    @ArgumentsSource(PersonArgumentProvider.class)
    void readTest(FootballPlayerArgument footballPlayerArgument) throws Exception {
        final var footballPlayer = getFootballPlayer(footballPlayerArgument, FootballPlayer.class);

        assertEquals(footballPlayerArgument.id, footballPlayer.getId());
        assertEquals(footballPlayerArgument.name, footballPlayer.getName());
        assertEquals(footballPlayerArgument.surname, footballPlayer.getSurname());
    }

    @ParameterizedTest(name = "Read Test - Players[{0}]")
    @ArgumentsSource(PersonArgumentProvider.class)
    void readTestWithCustomNameField(FootballPlayerArgument footballPlayerArgument) throws Exception {
        final var footballPlayer = getFootballPlayer(footballPlayerArgument, FootballPlayerWithCustomNameField.class);

        assertEquals(footballPlayerArgument.id, footballPlayer.getId());
        assertEquals(footballPlayerArgument.name, footballPlayer.getName());
        assertEquals(footballPlayerArgument.surname, footballPlayer.getCustomSurnameField());
    }

    private <T> T getFootballPlayer(FootballPlayerArgument footballPlayerArgument, Class<T> type) throws IOException {
        final var csvFile = "/csv/football-players.csv";
        try (final var inputStream = CsvReaderIT.class.getResourceAsStream(csvFile)) {
            final var footballPlayers = CsvReader.read(inputStream, type);
            return footballPlayers.get(footballPlayerArgument.index);
        }
    }
}
