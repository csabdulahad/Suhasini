package net.abdulahad.suhasini.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

public class SwapTextView extends AppCompatTextView {

    String text1, text2;

    boolean animation;
    boolean swapping;
    long delay = 1500;

    public SwapTextView(Context context) {
        super(context);
    }

    public SwapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwapTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSwapText(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
        setText(text1);
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // only allow touch down event
        if (event.getAction() != MotionEvent.ACTION_DOWN) return true;

        /* make sure we have text 2 to swap to  */
        if (text2 == null || text2.length() < 1) return true;

        if (!animation) { // no animated swapping; ordinary text swapping on click
            String text = getText().toString().equals(text1) ? text2 : text1;
            setText(text);
        } else {
            // mark the flat appropriately based on the touch event
            if (!swapping) swapping = true;

            /* swap the text and post the animation to the handler */
            setText(text2);
            getHandler().postDelayed(swapToOrigin, delay);
        }

        performClick();
        return true;
    }

    private final Runnable swapToOrigin = () -> {
        setText(text1);
        swapping = false;
    };

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

}
