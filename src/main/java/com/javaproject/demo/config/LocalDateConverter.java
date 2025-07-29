package com.javaproject.demo.config;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    @Override
    protected LocalDate convert(String value) {
        return LocalDate.parse(value); // Expects format: yyyy-MM-dd
    }
}
