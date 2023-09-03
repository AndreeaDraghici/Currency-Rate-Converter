package org.example.service;

import org.example.service.model.Report;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.sql.Timestamp;



/**
 * Created by Andreea Draghici on 9/2/2023
 * Name of project: CurrencyConvertor
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportPrinterTest {

    private File file;
    private Timestamp timestamp;
    private double fromValue;
    private String fromCurrency;
    private double toValue;
    private String toCurrency;
    private double convertedValue;
    private ReportPrinter printer;
    private static final File output = new File("src/test/resources/dummyReport.html");

    @BeforeAll
    void setup() {
        // Initialize the printer or any necessary resources before each test

        printer = new ReportPrinter();
    }

    @Test
    void shouldGenerateDummyReportTest() {

        printer.generateReport(file, timestamp, fromValue, fromCurrency, toValue, toCurrency, convertedValue);
    }

}