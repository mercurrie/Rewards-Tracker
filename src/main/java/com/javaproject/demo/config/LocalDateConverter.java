package com.javaproject.demo.config;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    private static final Logger logger = LogManager.getLogger(LocalDateConverter.class);
    
    @Override
    protected LocalDate convert(String value) {
        try {
            logger.debug("Converting date string to LocalDate: {}", value);
            LocalDate date = LocalDate.parse(value); // Expects format: yyyy-MM-dd
            logger.debug("Successfully converted date: {} -> {}", value, date);
            return date;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date string: {}", value, e);
            throw e;
        }
    }
}
