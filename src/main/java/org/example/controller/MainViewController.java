package org.example.controller;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static org.example.util.Constants.*;


/**
 * Created by Andreea Draghici on 8/1/2023
 * Name of project: CurrencyConvertor
 */
public class MainViewController {

    private static final Logger logger = LogManager.getLogger(MainViewController.class);

    @FXML
    public TextField fromCurrency;

    @FXML
    public TextField convertCurrency;

    @FXML
    public AnchorPane rootPane;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountField;

    @FXML
    private Button applyBtn;

    @FXML
    private Text rateCcy;

    @FXML
    private Text conversionTotal;

    @FXML
    private Text dateStamp;

    private final Map<String, Double> exchangeRates = new HashMap<>();

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @FXML
    void initialize() {
        rootPane.getChildren().add(buildMenuItems());
        initializeExchangeRates();
        setApplyButton();
    }

    private void setApplyButton() {

        applyBtn.setOnAction(event -> {

            String currencyOne = fromCurrency.getText().toUpperCase();
            String toCurrencyTwo = convertCurrency.getText().toUpperCase();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose output directory and save the report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
            File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

            try {
                if (!currencyOne.isEmpty() && !toCurrencyTwo.isEmpty()) {
                    if (exchangeRates.containsKey(currencyOne) && exchangeRates.containsKey(toCurrencyTwo)) {
                        if (file != null) {
                            buildCurrencyConversion(file, currencyOne, toCurrencyTwo);
                            logger.info(CONVERSION_WAS_CREATED_WITH_SUCCESS);
                        }
                    } else {
                        throw new RuntimeException(INVALID_CURRENCIES_ENTERED);
                    }
                }
            } catch (Exception exception) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                getAlertDefaultIcon(alert);
                alert.setHeaderText("Warning");
                alert.setContentText(String.format("%s%s", WARNING_TO_BUILD_THE_CURRENCY_CONVERSION, exception.getMessage()));
                alert.showAndWait();
                logger.error(String.format("%s%s", WARNING_TO_BUILD_THE_CURRENCY_CONVERSION, exception.getMessage()));
            }
        });
    }

    private void buildCurrencyConversion(File file, String currencyOne, String toCurrencyTwo) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        dateStamp.setText(SIMPLE_DATE_FORMAT.format(timestamp));

        double currencyTwoValue = exchangeRates.get(toCurrencyTwo) / exchangeRates.get(currencyOne);
        String ccyTwoStr = String.format("%s %s", String.format("%.2f", currencyTwoValue), toCurrencyTwo);

        rateCcy.setText(ccyTwoStr);

        try {
            double amount = Double.parseDouble(amountField.getText());
            double total = currencyTwoValue * amount;

            String totalStr = String.format("%s %s", String.format("%.2f", total), toCurrencyTwo);

            conversionTotal.setText(totalStr);

            generateReport(file, timestamp, amount, currencyOne, currencyTwoValue, toCurrencyTwo, total);

        } catch (NumberFormatException e) {
            throw new RuntimeException(INVALID_AMOUNT_ENTERED);
        }
    }


    /**
     * method used to build be menu available in the application
     *
     * @return the menu of the application
     */
    private MenuBar buildMenuItems() {
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        MenuItem exitItem = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");

        exitItem.setOnAction(event -> System.exit(0));
        aboutItem.setOnAction(event -> openReadmeInBrowser());

        fileMenu.getItems().add(exitItem);
        helpMenu.getItems().add(aboutItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    /**
     * method have the responsible for opening the README.pdf file in the default browser,
     * it uses a recursive search to find the file in the project directory and its subdirectories
     */
    private void openReadmeInBrowser() {
        try {
            String fileName = "README.pdf";
            //using a glob pattern to match the file with the name "README.pdf" anywhere in the directory structure.
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/" + fileName);
            //gets the current working directory of the project as the starting path for the search.
            Path startPath = Paths.get(System.getProperty("user.dir"));

            try (Stream<Path> paths = Files.walk(startPath)) {
                Optional<Path> readmePath = paths.filter(matcher::matches).findFirst();

                if (readmePath.isPresent()) {
                    //converts the file path to a URI so that it can be opened in the browser.
                    URI uri = readmePath.get().toUri();
                    //uses the Desktop class to open the URI in the default system browser.
                    Desktop.getDesktop().browse(uri);
                } else {
                    throw new RuntimeException("README.pdf not found in the project directory.");
                }
            }
        } catch (IOException e) {
            logger.error("Failed to open the documentation due to: " + e.getMessage());
        }
    }


    private void getAlertDefaultIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("/logo/logo.png")).toString()));
    }


    private void initializeExchangeRates() {
        // Hardcode your exchange rates here
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("GBP", 0.72);
        // Add other currency rates as needed
    }

    private void generateReport(File file, Timestamp date, double fromValue, String fromCurrency, double toValue, String toCurrency, double convertedValue) {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Currency Conversion Report</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #8FBC8F;}\n" +
                "        h1 { color: #007bff; }\n" +
                "        footer {text-align: center; padding: 3px; background-color: DarkSalmon; color: white; + }\n" +
                "        p {color: navy;text-indent: 30px;text-transform: uppercase;}" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Conversion Status</h1>\n" +
                "<p>Conversion Date: " + date + "</p>\n" +
                "<p>The starting currency value: " + fromValue + "</p>\n" +
                "<p>Departure currency: " + fromCurrency + "</p>\n" +
                "<p>Arrival currency value: " + toValue + "</p>\n" +
                "<p>Arrival currency: " + toCurrency + "</p>\n" +
                "<p>Conversion value: " + convertedValue + "</p>\n" +
                "</body>\n" +
                "<footer>\n" +
                "    <p>Developed by Andreea Draghici.</p>\n" +
                "</footer>\n" +
                "</html>";

        saveReportToFile(file, htmlContent);

    }

    private void saveReportToFile(File file, String htmlContent) throws RuntimeException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(htmlContent);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to save the report generation due to: " + e.getMessage());
        }
    }
}
