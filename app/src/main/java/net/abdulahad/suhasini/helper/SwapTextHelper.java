package net.abdulahad.suhasini.helper;

import android.content.Context;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.library.SwapTextView;

public class SwapTextHelper {

    public static void setMoneyToSTV(double amount, boolean showSign, SwapTextView swapTextView, Context appContext) {

        boolean bdSign = Suhasini.getMoneySign(appContext).equals(Key.MONEY_SIGN_BDT);

        String sign = showSign ? (amount > 0 ? "+ " : "- ") : "";
        if (amount < 0) amount = Math.abs(amount);

        if (bdSign) {
            String moneyInBDT = NumberFormatHelper.getMoneyWithSign(amount, Key.MONEY_SIGN_BDT);
            moneyInBDT = String.format("%s%s", sign, moneyInBDT);
            swapTextView.setText(moneyInBDT);
        } else {
            /* get the money in BDT as we need it */
            float exchangeRate = Suhasini.getCurrencyRate(appContext);
            String moneyInBDT = NumberFormatHelper.getMoneyWithSign(amount * exchangeRate, Key.MONEY_SIGN_BDT);
            moneyInBDT = String.format("%s%s", sign, moneyInBDT);


            String moneyInGBP = NumberFormatHelper.getMoneyWithSign(amount, Key.MONEY_SIGN_GBP);
            moneyInGBP = String.format("%s%s", sign, moneyInGBP);
            swapTextView.setSwapText(moneyInGBP, moneyInBDT);
        }






/*        boolean hasSwap = Suhasini.getMoneySign(appContext).equals(Key.MONEY_SIGN_GBP);
        if (hasSwap) {
            String moneyInGBP = NumberFormatHelper.getMoneyWithSign(amount, Key.MONEY_SIGN_GBP);
            moneyInGBP = String.format("%s%s", sign, moneyInGBP);
            swapTextView.setSwapText(moneyInGBP, moneyInBDT);
        } else {
            swapTextView.setText(moneyInBDT);
        }
*/
    }

}
