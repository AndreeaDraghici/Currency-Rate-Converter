package org.example.controller;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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

            try {
                if (!currencyOne.isEmpty() && !toCurrencyTwo.isEmpty()) {
                    if (exchangeRates.containsKey(currencyOne) && exchangeRates.containsKey(toCurrencyTwo)) {
                        buildCurrencyConversion(currencyOne, toCurrencyTwo);
                        logger.info(CONVERSION_WAS_CREATED_WITH_SUCCESS);
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

    private void buildCurrencyConversion(String currencyOne, String toCurrencyTwo) {

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

            generateReport(timestamp, currencyTwoValue, currencyOne, currencyTwoValue, toCurrencyTwo, total);

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

    private void openReadmeInBrowser() {
        try {
            File file = new File(DOCUMENTATION_PATH);
            URI uri = file.toURI();
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            logger.error(String.format("Failed to open the documentation due to: %s", e.getMessage()));
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

    private void generateReport(Timestamp date, double fromValue, String fromCurrency, double toValue, String toCurrency, double convertedValue) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n");
        htmlContent.append("<html>\n");
        htmlContent.append("<head>\n");
        htmlContent.append("    <title>Currency Conversion Report</title>\n");
        htmlContent.append("</head>\n");
        htmlContent.append("<body>\n");
        htmlContent.append("<h1>Conversion Status</h1>\n");
        htmlContent.append("<p>Conversion Date: ").append(date).append("</p>\n");
        htmlContent.append("<p>The starting currency value: ").append(fromValue).append("</p>\n");
        htmlContent.append("<p>Departure currency: ").append(fromCurrency).append("</p>\n");
        htmlContent.append("<p>Arrival currency value: ").append(toValue).append("</p>\n");
        htmlContent.append("<p>Arrival currency: ").append(toCurrency).append("</p>\n");
        htmlContent.append("<p>Conversion value: ").append(convertedValue).append("</p>\n");
        htmlContent.append("</body>\n");
        htmlContent.append("</html>");

        saveReportToFile(htmlContent.toString());

    }

    private void saveReportToFile(String htmlContent) {
        try (PrintWriter writer = new PrintWriter("src/main/resources/report/StatusConversion.html")) {
            writer.write(htmlContent);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to save the report generation due to: " + e.getMessage());
        }
    }
}
