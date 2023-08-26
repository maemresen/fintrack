package com.maemresen.fintrack.api.utils.helper;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DateUtils {
    public static LocalDateTime getFirstDayOfYear(int year) {
        return LocalDateTime.of(year, 1, 1, 0, 0, 0);
    }

    public static LocalDateTime getLastDayOfYear(int year) {
        return LocalDateTime.of(year, 12, 31, 23, 59, 59);
    }
}
