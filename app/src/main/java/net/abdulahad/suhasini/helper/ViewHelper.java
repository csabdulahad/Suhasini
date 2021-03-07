package net.abdulahad.suhasini.helper;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public abstract class ViewHelper {

    /*
     * This method tells if a view is currently shown or not, which is in an activity
     *
     * @param activity The activity which holding the view
     * @param  viewId The id for the view, you want to look for
     * @return true If the view is shown, false otherwise
     * */
    public static boolean isVisible(Activity activity, int viewId) {
        return activity.findViewById(viewId).getVisibility() == View.VISIBLE;
    }

    /*
     * This method tells if a view is currently shown or not, which is in specified rootView
     *
     * @param rootView The rootView which holding the view
     * @param  viewId The id for the view, you want to look for
     * @return true If the view is shown, false otherwise
     * */
    public static boolean isVisible(View rootView, int viewId) {
        return rootView.findViewById(viewId).getVisibility() == View.VISIBLE;
    }


    /*
     * This method set the visibility of a view to visible, which is inside an activity
     *
     * @param activity The activity which holding the view
     * @param  viewId The id for the view, you want to show
     * */
    public static void showView(Activity activity, int viewId) {
        activity.findViewById(viewId).setVisibility(View.VISIBLE);
    }


    /*
     * This method set the visibility of a view to visible, which is inside specified rootView
     *
     * @param rootView The rootView which holding the view
     * @param viewId The id for the view, you want to show
     * */
    public static void showView(View rootView, int viewId) {
        rootView.findViewById(viewId).setVisibility(View.VISIBLE);
    }


    /*
     * This little method vanishes any view, which is inside an activity
     *
     * @param activity The activity which holding the view
     * @param  viewId The id for the view, you want to vanish
     * */
    public static void vanishView(Activity activity, int viewId) {
        activity.findViewById(viewId).setVisibility(View.GONE);
    }


    /*
     * This little method vanishes any view, which is inside specified rootView
     *
     * @param rootView The rootView which holding the view
     * @param viewId The id for the view, you want to vanish
     * */
    public static void vanishView(View rootView, int viewId) {
        rootView.findViewById(viewId).setVisibility(View.GONE);
    }


    /*
     * This method set resource for an image view
     *
     * @param activity The activity in which, the view is
     * @param imageId The id for the image view
     * @param drawableId the drawable resource id
     * */
    public static void setImageViewSource(Activity activity, int imageViewId, int drawableId) {
        ((ImageView) activity.findViewById(imageViewId)).setImageResource(drawableId);
    }


    /*
     * this methods return a view, that is inside of an activity
     *
     * @param activity the activity the view is in
     * @param viewId the id for the view
     * */
    public static View getView(Activity activity, int viewId) {
        return activity.findViewById(viewId);
    }


    /*
     * this methods return a view, that is inside of a viewGroup
     *
     * @param parentView the view as the root
     * @param viewId the id for the view, you want to get
     * */
    public static View getView(View parentView, int viewId) {
        return parentView.findViewById(viewId);
    }


    public static TextView getTextView(Activity activity, int textViewId) {
        return (TextView) activity.findViewById(textViewId);
    }

    public static TextView getTextView(View rootView, int textViewId) {
        return (TextView) rootView.findViewById(textViewId);
    }

    public static EditText getEditText(Activity activity, int editTextId) {
        return (EditText) activity.findViewById(editTextId);
    }

    public static EditText getEditText(View view, int editTextId) {
        return (EditText) view.findViewById(editTextId);
    }

    public static String getTextFromTextView(Activity activity, int viewId) {
        return ((EditText) activity.findViewById(viewId)).getText().toString();
    }

    public static void showEditTextError(Activity activity, int viewId, String message) {
        ((EditText) activity.findViewById(viewId)).setError(message);
    }

    public static void showEditTextError(View view, int viewId, String message) {
        ((EditText) view.findViewById(viewId)).setError(message);
    }


    public static void requestFocus(Activity activity, int viewId) {
        getEditText(activity, viewId).requestFocus();
    }

    public static void requestFocus(View view, int viewId) {
        getEditText(view, viewId).requestFocus();
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ViewGroup getRootView(Activity activity) {
        return (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

/*    public static View getCoordinatorLayout(Activity activity) {
        return activity.findViewById(R.id.coordinator_layout);
    }*/


    public static CheckBox getCheckBox(View view, int checkBoxId) {
        CheckBox checkBox = (CheckBox) view.findViewById(checkBoxId);
        return checkBox;
    }


    public static boolean getStatusOfCheckBox(View view, int checkBoxId) {
        CheckBox checkBox = (CheckBox) view.findViewById(checkBoxId);
        return checkBox.isChecked();
    }

    public static void disableRadioGroup(View view, int radioGroupId) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(radioGroupId);
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = radioGroup.getChildAt(i);
            child.setEnabled(false);
        }
    }

    public static void enableRadioGroup(View view, int radioGroupId) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(radioGroupId);
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = radioGroup.getChildAt(i);
            child.setEnabled(true);
        }
    }

    public static void showToast(String message, View anyView) {
        Toast.makeText(anyView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void setHtmlTextToTextView(TextView textView, String value) {
        textView.setText(Html.fromHtml(value, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
    }


    public static void animateHeart(View view, int repetition, int duration) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", .75f),
                PropertyValuesHolder.ofFloat("scaleY", .75f));
        scaleDown.setDuration(duration);

        scaleDown.setRepeatCount(repetition == 0 ? ObjectAnimator.INFINITE : repetition);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    public static void animateInCircle(View view, int duration) {
        RotateAnimation r = new RotateAnimation(360, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration(duration);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatMode(Animation.RESTART);
        r.setRepeatCount(Animation.INFINITE);
        view.startAnimation(r);
    }

    public static void showSnackBar(String message, int duration, Activity activity) {
        Snackbar.make(getRootView(activity), message, duration).show();
    }

    /**
     * this method makes an activity go full screen
     *
     * @param activity the activity you want to go full screen
     */
    public static void goFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setTint(int colorId, ImageView imageView) {
        imageView.setColorFilter(colorId);
    }

    public static void requestFocusAndKeyboard(EditText editText) {
        editText.requestFocus();

        InputMethodManager keyboard = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(editText, 0);
    }

}
