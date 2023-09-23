package converter.tool.service.model;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Andreea Draghici on 9/2/2023
 * Name of project: CurrencyConvertor
 */
public class Report {


    public static String getToCurrency(String toCurrency) {
        return toCurrency;
    }

    public static String getFromCurrency(String fromCurrency) {
        return fromCurrency;
    }

    public static double getConvertedValue(double convertedValue) {
        return convertedValue;
    }

    public static double getToValue(double toValue) {
        return toValue;
    }

    public static double getFromValue(double fromValue) {
        return fromValue;
    }

    public static Timestamp getDate(Timestamp date) {
        return date;
    }
}
