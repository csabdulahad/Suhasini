package net.abdulahad.suhasini.library;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import net.abdulahad.suhasini.R;

import java.util.ArrayList;
import java.util.List;

public class Histogram extends View {

    private int defaultStrokeSize = 10;
    private int defaultNumberOfBar = 1;

    private int strokeSize;
    private int numberOfBar;
    private List<Paint> paints;
    private List<Float> barValues;
    private RectF rectF;

    float lastProgressAngle = -90;

    public Histogram(Context context) {
        super(context);
        init(null);
    }

    public Histogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Histogram(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {


        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Histogram, 0, 0);
        try {
            numberOfBar = typedArray.getInt(R.styleable.Histogram_numberOfBar, defaultNumberOfBar);
            strokeSize = typedArray.getInt(R.styleable.Histogram_barStrokeSize, defaultStrokeSize);
        } finally {
            typedArray.recycle();
        }

        rectF = new RectF();

        barValues = new ArrayList<>(numberOfBar);
        if (numberOfBar == 1) barValues.add(100f);
        else {
            for (int i = 0; i < numberOfBar; i++) {
                barValues.add(0f);
            }
        }

        paints = new ArrayList<>(numberOfBar);
        for (int i = 0; i < numberOfBar; i++) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(strokeSize);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.BUTT);
            paints.add(paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        float canvasOffsetTopLeft = strokeSize / 2;
        float canvasOffsetBottomRight = size - (strokeSize - 2);
        rectF.set(0 + canvasOffsetTopLeft, 0 + canvasOffsetTopLeft, 0 + canvasOffsetBottomRight, 0 + canvasOffsetBottomRight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < numberOfBar; i++) {
            if (Float.compare(barValues.get(i), 0f) == 0) continue;

//
//            int adjustment = 0;
//            if (i == (numberOfBar - 1) && (Float.compare(barValues.get(i), 0f) != 0))
//                adjustment = +4;

            float progressAngle = (barValues.get(i) * 360f) / 100f;
            canvas.drawArc(rectF, lastProgressAngle + 1, progressAngle + 1, false, paints.get(i));

            lastProgressAngle += progressAngle;
        }
        lastProgressAngle = -90;
    }

    public void setColors(int... colors) {
        for (int i = 0; i < numberOfBar; i++) {
            paints.get(i).setColor(colors[i]);
        }
        invalidate();
        requestLayout();
    }

    public void setValue(int index, float value) {
        barValues.add(index, value);
        invalidate();
        requestLayout();
    }

    public void setValues(float... value) {
        for (int i = 0; i < numberOfBar; i++) {
            barValues.add(i, value[i]);
        }
        invalidate();
        requestLayout();
    }

    public void setStrokeSize(float strokeSize) {
        this.strokeSize = (int) strokeSize;
        for (int i = 0; i < numberOfBar; i++) {
            paints.get(i).setStrokeWidth(this.strokeSize);
        }
        invalidate();
        requestLayout();
    }


    public void animateView() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "strokeSize", strokeSize);
        objectAnimator.setInterpolator(new OvershootInterpolator(25));
        objectAnimator.setDuration(1500);
        objectAnimator.start();
    }

    public void setColorsFromResources(int... colorsRef) {
        Resources resources = getResources();
        int colors[] = new int[numberOfBar];
        for (int i = 0; i < numberOfBar; i++)
            colors[i] = ResourcesCompat.getColor(resources, colorsRef[i], null);
        setColors(colors);
    }
}
