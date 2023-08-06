package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;

import static org.example.util.Constants.SCRIPT_JS;
import static org.example.util.Constants.STYLE_CSS;


/**
 * Created by Andreea Draghici on 8/6/2023
 * Name of project: CurrencyConvertor
 */
public class ReportPrinter {

    private static final Logger logger = LogManager.getLogger(ReportPrinter.class);

    public void generateReport(File file, Timestamp date, double fromValue, String fromCurrency, double toValue, String toCurrency, double convertedValue) {

        copyStaticResources(URI.create(String.valueOf(Paths.get(file.getParent()).toUri())));

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Currency Conversion Report</title>\n" +
                "    <link href=\"resources/style.css\" rel=\"stylesheet\">\n" +
                "    <script src=\"resources/script.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <h1 > Conversion Status </h1>\n" +
                "   <hr>\n" +
                "       <h2 > Generated today: <span id=\"currentDateTime\"></h2>\n" +
                "   <hr>\n" +
                "   <p class=\"navy\"> Conversion Date: " + date + " </p>\n" +
                "   <p class=\"green\"> The starting currency value: " + fromValue + " </p>\n" +
                "   <p class=\"red\"> Departure currency: " + fromCurrency + " </p>\n" +
                "   <p> Arrival currency value: " + toValue + " </p>\n" +
                "   <p class=\"orange\"> Arrival currency: " + toCurrency + " </p>\n" +
                "   <p class=\"blue\"> Conversion value: " + convertedValue + " </p>\n" +
                "</body>\n" +
                "   <hr>\n" +
                "       <footer>\n" +
                "            <p> Developed by Andreea Draghici</p>\n" +
                "       </footer>\n" +
                "   <hr>\n" +
                "</html>";

        saveReportToFile(file, htmlContent);

    }

    /**
     * method that copies the static resources of the output to the output folder.
     *
     * @param path - path to copy output files to.
     */
    private void copyStaticResources(URI path) {
        InputStream styleFile = getClass().getResourceAsStream(STYLE_CSS);
        InputStream scriptFile = getClass().getResourceAsStream(SCRIPT_JS);

        File resources = new File(path.getPath() + "/resources");
        // if the directory does not exist, create it
        if (!resources.exists()) {
            logger.info(String.format("Creating static directory: %s", resources.getName()));
            boolean result = false;

            try {
                resources.mkdir();
                result = true;
            } catch (SecurityException se) {
                logger.warn(se.getMessage());
            }
            if (result) {
                URI pathResources = Paths.get(resources.getAbsolutePath()).toUri();
                Path path1 = Paths.get(pathResources);
                filesChecked(styleFile, scriptFile, path1);
            }
        }

    }

    private static void filesChecked(InputStream styleFile, InputStream scriptFile, Path path) {
        if (styleFile != null) {
            logger.info("Files style.css and script.js were copied with succes into resources directory.");
            try {
                Files.copy(styleFile, path.resolve("style.css"), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(scriptFile, path.resolve("script.js"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void saveReportToFile(File file, String htmlContent) throws RuntimeException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(htmlContent);
            logger.info(String.format("Generated %s report.", file.getAbsoluteFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to save the report generation due to: " + e.getMessage());
        }
    }

}
