package org.example.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.example.util.Constants.INVALID_AMOUNT_ENTERED;
import static org.example.util.Constants.INVALID_CURRENCIES_ENTERED;


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
        initializeExchangeRates();

        logger.info("Starting the application....");
        applyBtn.setOnAction(event -> {

            String currencyConversion = fromCurrency.getText().toUpperCase();
            String toConversion = convertCurrency.getText().toUpperCase();

            if (!currencyConversion.isEmpty() && !toConversion.isEmpty()) {
                if (exchangeRates.containsKey(currencyConversion) && exchangeRates.containsKey(toConversion)) {

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    dateStamp.setText(SIMPLE_DATE_FORMAT.format(timestamp));

                    double ccyTwoValue = exchangeRates.get(toConversion) / exchangeRates.get(currencyConversion);
                    String ccyTwoStr = String.format("%.2f", ccyTwoValue) + " " + toConversion;
                    rateCcy.setText(ccyTwoStr);

                    try {
                        double amount = Double.parseDouble(amountField.getText());
                        double total = ccyTwoValue * amount;
                        String totalStr = String.format("%.2f", total) + " " + toConversion;
                        conversionTotal.setText(totalStr);

                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        getAlertDefaultIcon(alert);
                        alert.setContentText(INVALID_AMOUNT_ENTERED);
                        alert.setHeaderText("Warning");
                        alert.showAndWait();
                        logger.error(INVALID_AMOUNT_ENTERED);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    getAlertDefaultIcon(alert);
                    alert.setContentText(INVALID_CURRENCIES_ENTERED);
                    alert.setHeaderText("Warning");
                    alert.showAndWait();
                    logger.error(INVALID_CURRENCIES_ENTERED);
                }
            }
        });
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
}
