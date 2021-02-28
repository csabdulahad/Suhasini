package net.abdulahad.suhasini.helper;

import net.abdulahad.suhasini.data.Key;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatHelper {

    public static NumberFormat getIntFormatter() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumIntegerDigits(2);
        return numberFormat;
    }

    public static String getPercent(double number) {
        return String.format("%.2f%%", number);
    }

    public static String getMoneyFormat(double money) {
        NumberFormat numberFormat = getIntFormatter();
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(money);
    }

    public static String getMoneyWithSign(double money, String moneySign) {
        String moneyStr = getMoneyFormat(money);
        if (moneySign.equals(Key.MONEY_SIGN_BDT)) moneyStr = enToBDNumber(moneyStr);
        return String.format("%s %s", moneySign, moneyStr);
    }

    public static String enToBDNumber(String number) {
        StringBuilder builder = new StringBuilder();
        char[] chars = number.toCharArray();
        for (char digit : chars) builder.append(toBDDigit(digit));
        return builder.toString();
    }

    public static String toBDDigit(char digit) {
        if (!Character.isDigit(digit)) return String.valueOf(digit);
        switch (digit) {
            case '0':
                return "০";
            case '1':
                return "১";
            case '2':
                return "২";
            case '3':
                return "৩";
            case '4':
                return "৪";
            case '5':
                return "৫";
            case '6':
                return "৬";
            case '7':
                return "৭";
            case '8':
                return "৮";
            default:
                return "৯";
        }
    }

}
