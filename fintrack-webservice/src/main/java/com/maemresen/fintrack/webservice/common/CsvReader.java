package com.maemresen.fintrack.webservice.common;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@UtilityClass
public class CsvReader {

    /**
     * Reads the content of the given InputStream (representing a CSV)
     * and returns its content as a list of Java beans.
     *
     * @param inputStream InputStream representing the CSV content.
     * @return List of Java beans representing the content of the CSV.
     * @throws IOException if any error occurs while reading or mapping the CSV.
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> type) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            final var strategy = new CustomMappingStrategy<T>();
            strategy.setType(type);

            return new CsvToBeanBuilder<T>(reader)
                .withMappingStrategy(strategy)
                .withType(type)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
        }
    }
}
