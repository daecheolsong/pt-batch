package com.example.ptbatch.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeUtilsTest {


    @Test
    public void pares() {
        String localDateTimeString = LocalDateTime.of(2022, Month.APRIL, 20, 10, 0).format(LocalDateTimeUtils.YYYY_MM_DD_HH_MM);

        LocalDateTime actual = LocalDateTimeUtils.parse(localDateTimeString);

        assertEquals(LocalDateTime.of(2022, 4, 20, 10, 0), actual);

    }

}