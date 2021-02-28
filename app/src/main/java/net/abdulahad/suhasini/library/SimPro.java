package net.abdulahad.suhasini.library;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import net.abdulahad.suhasini.R;

import java.text.NumberFormat;


public class SimPro extends View {

    private float progress;

    private int strokeSize;

    private int barColor;

    private int textColor;
    private int textSize;

    private int guideColor;

    private boolean showPercentage;
    private boolean showPercentageSign;

    private Paint guidePaint, barPaint;
    private TextPaint textPaint;

    private RectF guideRectF;
    private Rect bound;

    private StringBuilder percentageTextBuilder;
    private NumberFormat numberFormat;

    private boolean clockType;

    float indicator;

    public SimPro(Context context) {
        super(context);
    }

    public SimPro(Context context, AttributeSet attrs) {
        super(context, attrs);

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true);
        // TODO: 8/14/2018 - add option to dynamically set this fraction value
        // TODO: 8/14/2018 - provide setters and revise this code
        numberFormat.setMaximumFractionDigits(1);

        final float defaultProgress = 0;
        int defaultStrokeSize = 10;
        int defaultColor = Color.parseColor("#3F51B5");

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimPro, 0, 0);
        try {
            progress = typedArray.getFloat(R.styleable.SimPro_progress, defaultProgress);
            strokeSize = typedArray.getInt(R.styleable.SimPro_strokeSize, defaultStrokeSize);


            barColor = typedArray.getColor(R.styleable.SimPro_barColor, defaultColor);
            guideColor = typedArray.getColor(R.styleable.SimPro_guideColor, adjustAlpha(barColor, 0.3f));

            textColor = typedArray.getColor(R.styleable.SimPro_textColor, Color.BLACK);
            textSize = (int) typedArray.getDimension(R.styleable.SimPro_textSize, 16);

            showPercentage = typedArray.getBoolean(R.styleable.SimPro_showPercentage, true);
            showPercentageSign = typedArray.getBoolean(R.styleable.SimPro_showPercentageSign, true);

            clockType = typedArray.getBoolean(R.styleable.SimPro_counterClock, false);

        } finally {
            typedArray.recycle();
        }

        percentageTextBuilder = new StringBuilder();
        guideRectF = new RectF();
        bound = new Rect();


        guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        guidePaint.setStrokeWidth(strokeSize);
        guidePaint.setColor(guideColor);
        guidePaint.setStyle(Paint.Style.STROKE);
        guidePaint.setStrokeCap(Paint.Cap.ROUND);

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStrokeWidth(strokeSize);
        barPaint.setColor(barColor);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(getResources().getDisplayMetrics().scaledDensity * textSize);
        textPaint.setColor(textColor);
    }

    public SimPro(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        float canvasOffsetTopLeft = (float) strokeSize / 2;
        float canvasOffsetBottomRight = size - (strokeSize - 2);
        guideRectF.set(0 + canvasOffsetTopLeft, 0 + canvasOffsetTopLeft, 0 + canvasOffsetBottomRight, 0 + canvasOffsetBottomRight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(guideRectF, guidePaint);


        float progressAngle = progress * 360 / 100;
        float indicatorAngle = calculateIndicator() * 360 / 100;
        int clockTypeValue = clockType ? 90 : -90;
        canvas.drawArc(guideRectF, clockTypeValue, progressAngle, false, barPaint);

        canvas.drawArc(guideRectF, clockTypeValue, indicatorAngle, false, barPaint);

        if (showPercentage) {
            percentageTextBuilder.delete(0, percentageTextBuilder.length());

            percentageTextBuilder.append(numberFormat.format(progress));

            if (showPercentageSign) percentageTextBuilder.append("%");
            textPaint.getTextBounds(percentageTextBuilder.toString(), 0, percentageTextBuilder.length(), bound);

            canvas.drawText(percentageTextBuilder.toString(), ((float) getWidth() / 2) - ((float)bound.width() / 2), (float)getHeight() / 2 + ((float)bound.height() / 2), textPaint);
        }

    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
        requestLayout();
    }

    private float calculateIndicator() {
        if (indicator < progress)
            indicator++;
        else indicator = 0;
        return indicator;
    }

    private int adjustAlpha(int color, float factor) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(alpha, red, green, blue);
    }

    public void setProgressWithAnimation(float progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

}
