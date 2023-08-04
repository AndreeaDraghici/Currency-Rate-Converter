package org.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="amountField"
    private TextField amountField; // Value injected by FXMLLoader

    @FXML // fx:id="applyBtn"
    private Button applyBtn; // Value injected by FXMLLoader

    @FXML // fx:id="rateCcy"
    private Text rateCcy; // Value injected by FXMLLoader

    @FXML // fx:id="conversionTotal"
    private Text conversionTotal; // Value injected by FXMLLoader

    @FXML // fx:id="dateStamp"
    private Text dateStamp; // Value injected by FXMLLoader

    // This method is called by the FXMLLoader when initialization is complete
    private final Map<String, Double> exchangeRates = new HashMap<>();

    @FXML
    void initialize() {
        initializeExchangeRates();

        logger.info("Starting the application....");
        applyBtn.setOnAction(event -> {

            String currencyConversion = fromCurrency.getText().toUpperCase();
            String toConversion = convertCurrency.getText().toUpperCase();

            if (!currencyConversion.isEmpty() && !toConversion.isEmpty()) {
                if (exchangeRates.containsKey(currencyConversion) && exchangeRates.containsKey(toConversion)) {
                    dateStamp.setText(""); // You may want to clear the date field as it's not relevant with hardcoded rates.

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
                        alert.setContentText(INVALID_AMOUNT_ENTERED);
                        alert.setHeaderText("Warning");
                        alert.showAndWait();
                        logger.error(INVALID_AMOUNT_ENTERED);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(INVALID_CURRENCIES_ENTERED);
                    alert.setHeaderText("Warning");
                    alert.showAndWait();
                    logger.error(INVALID_CURRENCIES_ENTERED);
                }
            }
        });
    }


    private void initializeExchangeRates() {
        // Hardcode your exchange rates here
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("GBP", 0.72);
        // Add other currency rates as needed
    }
}
