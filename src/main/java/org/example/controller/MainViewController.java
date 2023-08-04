package org.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


/**
 * Created by Andreea Draghici on 8/1/2023
 * Name of project: CurrencyConvertor
 */
public class MainViewController {

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

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    private Map<String, Double> exchangeRates = new HashMap<>();

    @FXML
    void initialize() {
        initializeExchangeRates();

        applyBtn.setOnAction(event -> {
            String ccyOneText = fromCurrency.getText().toUpperCase();
            String ccyTwoText = convertCurrency.getText().toUpperCase();

            if (!ccyOneText.isEmpty() && !ccyTwoText.isEmpty()) {
                if (exchangeRates.containsKey(ccyOneText) && exchangeRates.containsKey(ccyTwoText)) {
                    dateStamp.setText(""); // You may want to clear the date field as it's not relevant with hardcoded rates.

                    double ccyTwoValue = exchangeRates.get(ccyTwoText) / exchangeRates.get(ccyOneText);
                    String ccyTwoStr = String.format("%.2f", ccyTwoValue) + " " + ccyTwoText;
                    rateCcy.setText(ccyTwoStr);

                    try {
                        double amount = Double.parseDouble(amountField.getText());
                        double total = ccyTwoValue * amount;
                        String totalStr = String.format("%.2f", total) + " " + ccyTwoText;
                        conversionTotal.setText(totalStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered!");
                    }
                } else {
                    System.out.println("Invalid currencies entered!");
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
