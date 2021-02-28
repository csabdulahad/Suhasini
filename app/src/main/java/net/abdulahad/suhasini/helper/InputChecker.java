package net.abdulahad.suhasini.helper;

import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * one of the greatest helper class of all. this helper class has methods that helps you filter &
 * validate user input that is very critical for any app. on invalid input, each method fires an
 * error message and tries to animate and focus that input so that user can know what is going on
 *
 * @author abdul ahad
 */
public class InputChecker {

    /**
     * checks whether input is empty or not
     *
     * @param editText the editText you want to check for
     * @return true if it pass the test, false otherwise
     */
    public static boolean passEmptyCheck(EditText editText) {
        String data = editText.getText().toString();
        if (data.isEmpty()) {
            editText.setError("Input is empty");
            focusAndAnimate(editText);
            return false;
        }
        return true;
    }


    /**
     * it checks for whether an edit text has input is long of specified length
     *
     * @param editText the editText you want to check for
     * @param min      defines how many characters are allowed in minimum
     * @param max      defines how many characters are allowed in maximum
     * @param infinity defines weather input has range or should be checked in infinite range
     * @return true if it input passes all tests & is in the range, false otherwise
     */
    public static boolean passTextRangeCheck(EditText editText, int min, int max, boolean infinity) {
        if (!passEmptyCheck(editText)) return false;

        int length = editText.getText().toString().length();
        if (infinity && length < min) {
            editText.setError(String.format(Locale.getDefault(), "Minimum input length %d", min));
            editText.requestFocus();
            return false;
        }

        if (!infinity && (length < min || length > max)) {
            editText.setError(String.format(Locale.getDefault(), "Length must be between %d - %d", min, max));
            editText.requestFocus();
            return false;
        }


        return true;
    }

    /**
     * it checks for whether input is of only letters & spaces
     *
     * @param editText the editText you want to check for
     * @return true if it pass the test, false otherwise
     */
    public static boolean passTextSpaceCheck(EditText editText) {
        String data = editText.getText().toString();
        if (data.isEmpty()) {
            editText.setError("Input is empty");
            editText.requestFocus();
            return false;
        }

        if (!Pattern.matches("[a-zA-Z\\s]+", data)) {
            editText.setError("Letters & Spaces only");
            editText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * it checks for whether an edit text has input of only letters & spaces only
     * with specified length
     *
     * @param editText the editText you want to check for
     * @param min      defines how many characters are allowed in minimum
     * @param max      defines how many characters are allowed in maximum
     * @param infinity defines weather input has range or should be checked in infinite range
     * @return true if it input passes all tests & is in the range, false otherwise
     */
    public static boolean passTextSpaceRangeCheck(EditText editText, int min, int max, boolean infinity) {
        if (!passTextSpaceCheck(editText)) return false;

        int length = editText.getText().toString().length();

        if (infinity && length < min) {
            editText.setError(String.format(Locale.getDefault(), "Minimum input length %d", min));
            editText.requestFocus();
            return false;
        }

        if (!infinity && (length < min || length > max)) {
            editText.setError(String.format(Locale.getDefault(), "Length must be between %d - %d", min, max));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * check whether an edit text has only number input or not
     *
     * @param editText  the editText you want to check for
     * @param zeroCheck if true it checks whether number is greater than zero or not
     * @return true if all test pass, false otherwise
     */
    public static boolean passNumberCheck(EditText editText, boolean zeroCheck) {

        String data = editText.getText().toString();
        if (data.isEmpty()) {
            editText.setError("Input is empty");
            editText.requestFocus();
            return false;
        }

        if (!TextUtils.isDigitsOnly(data)) {
            editText.setError("Must be a number");
            editText.requestFocus();
            return false;
        }

        if (zeroCheck)
            if (Integer.parseInt(data) < 1) {
                editText.setError("Must be greater than 0");
                editText.requestFocus();
                return false;
            }

        return true;
    }

    /**
     * it checks for whether an edit text has input within a defined range
     *
     * @param editText the editText you want to check for
     * @param min      defines minimum value
     * @param max      defines maximum value
     * @param infinity defines weather input should be checked in infinite range
     * @return true if it input passes all tests & is in the range, false otherwise
     */
    public static boolean passNumberRangeCheck(EditText editText, int min, int max, boolean infinity) {
        if (!passNumberCheck(editText, false)) return false;

        int data = Integer.parseInt(editText.getText().toString());

        if (infinity && data < min) {
            editText.setError(String.format(Locale.getDefault(), "Must be greater than %d", min));
            editText.requestFocus();
            return false;
        }

        if (!infinity && (data < min || data > max)) {
            editText.setError(String.format(Locale.getDefault(), "Must be between %d - %d", min, max));
            editText.requestFocus();
            return false;
        }

        return true;
    }


    /**
     * It checks whether an editText has valid decimal number and checks against
     * zero value if specified by argument
     *
     * @param editText  the editText you want to check for
     * @param zeroCheck if true it checks whether number is greater than zero or not
     * @return true if all test pass, false otherwise
     */
    public static boolean isDouble(EditText editText, boolean zeroCheck) {
        if (!passEmptyCheck(editText)) return false;

        double value;
        try {
            value = Double.parseDouble(String.valueOf(editText.getText()));

            if (zeroCheck) {
                if (Double.compare(value, 0) < 1) {
                    editText.setError("Must be greater than zero");
                    focusAndAnimate(editText);
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            editText.setError("Must be a valid decimal number");
            focusAndAnimate(editText);
            return false;
        }
    }

    /**
     * It checks whether a radio group is selected or not
     *
     * @param radioGroup which you want to check for selection
     * @return false if no radio button is selected, true if one radio button is selected
     */
    public static boolean passSelectionCheck(RadioGroup radioGroup) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            focusAndAnimate(radioGroup);
            return false;
        }
        return true;
    }

    public static void focusAndAnimate(View view) {
        view.requestFocus();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 50f, 0f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.start();
    }

}