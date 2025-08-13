package com.javaproject.demo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateConverterTest {

    private LocalDateConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LocalDateConverter();
    }

    @Test
    void convertValidDateStringTest() {
        // Given
        String dateString = "2024-01-15";

        // When
        LocalDate result = converter.convert(dateString);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    void convertValidDateStringLeapYearTest() {
        // Given
        String dateString = "2024-02-29"; // 2024 is a leap year

        // When
        LocalDate result = converter.convert(dateString);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2024, 2, 29));
    }

    @Test
    void convertValidDateStringDecemberTest() {
        // Given
        String dateString = "2024-12-31";

        // When
        LocalDate result = converter.convert(dateString);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void convertValidDateStringJanuaryFirstTest() {
        // Given
        String dateString = "2025-01-01";

        // When
        LocalDate result = converter.convert(dateString);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void convertInvalidDateFormatTest() {
        // Given
        String invalidDateString = "15-01-2024"; // Wrong format

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidDateString);
        });
    }

    @Test
    void convertInvalidDateSlashFormatTest() {
        // Given
        String invalidDateString = "2024/01/15"; // Wrong separator

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidDateString);
        });
    }

    @Test
    void convertEmptyStringTest() {
        // Given
        String emptyString = "";

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(emptyString);
        });
    }

    @Test
    void convertNullStringTest() {
        // Given
        String nullString = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            converter.convert(nullString);
        });
    }

    @Test
    void convertInvalidMonthTest() {
        // Given
        String invalidDateString = "2024-13-15"; // Month 13 doesn't exist

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidDateString);
        });
    }

    @Test
    void convertInvalidDayTest() {
        // Given
        String invalidDateString = "2024-02-30"; // February 30th doesn't exist

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidDateString);
        });
    }

    @Test
    void convertInvalidYearTest() {
        // Given
        String invalidDateString = "abcd-01-15"; // Invalid year

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidDateString);
        });
    }

    @Test
    void convertShortDateTest() {
        // Given
        String shortDateString = "2024-1-1"; // Single digit month and day

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(shortDateString);
        });
    }

    @Test
    void convertDateWithExtraCharactersTest() {
        // Given
        String dateWithExtra = "2024-01-15T00:00:00"; // Contains time

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(dateWithExtra);
        });
    }

    @Test
    void convertPastDateTest() {
        // Given
        String pastDate = "1900-01-01";

        // When
        LocalDate result = converter.convert(pastDate);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(1900, 1, 1));
    }

    @Test
    void convertFutureDateTest() {
        // Given
        String futureDate = "2050-12-31";

        // When
        LocalDate result = converter.convert(futureDate);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2050, 12, 31));
    }

    @Test
    void convertNonLeapYearFebruaryTest() {
        // Given
        String nonLeapYear = "2023-02-28"; // 2023 is not a leap year

        // When
        LocalDate result = converter.convert(nonLeapYear);

        // Then
        assertThat(result).isEqualTo(LocalDate.of(2023, 2, 28));
    }

    @Test
    void convertNonLeapYearInvalidFebruaryTest() {
        // Given
        String invalidNonLeapYear = "2023-02-29"; // 2023 is not a leap year

        // When & Then
        assertThrows(DateTimeParseException.class, () -> {
            converter.convert(invalidNonLeapYear);
        });
    }
}
