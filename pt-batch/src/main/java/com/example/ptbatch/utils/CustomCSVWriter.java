package com.example.ptbatch.utils;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Slf4j
public abstract class CustomCSVWriter {

    public static int write(final String fileName, List<String[]> data) {
        int rows = 0;
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeAll(data);
            rows = data.size();
        } catch (IOException e) {
            log.error("CustomCSVWriter - write Error, cause : {}", e.getMessage());
        }
        return rows;
    }
}
