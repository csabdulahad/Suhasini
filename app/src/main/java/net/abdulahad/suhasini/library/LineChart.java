package net.abdulahad.suhasini.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import net.abdulahad.suhasini.helper.ViewHelper;

import java.util.ArrayList;

public class LineChart extends View {

    private boolean showLabel;
    private boolean rightBorder = true;
    private boolean bottomBorder = true;

    private final int defaultBarSize = 5;
    private final int defaultBorderSize = 1;
    private float defaultTextSize;

    private final int defaultBorderColor = Color.GRAY;
    private final int defaultTextColor = Color.BLACK;

    private ArrayList<Float> progressList;

    private ArrayList<Paint> barPaintList;

    private Paint borderPaint;
    private TextPaint textPaint;
    private Rect bound;

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        defaultTextSize = getResources().getDisplayMetrics().scaledDensity * 11;
        progressList = new ArrayList<>();
        barPaintList = new ArrayList<>();

        bound = new Rect();

        initBorderPaint();
        initTextPaint();
    }

    private void initBorderPaint() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(defaultBorderSize);
        borderPaint.setColor(defaultBorderColor);
    }

    private void initTextPaint() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(defaultTextSize);
        textPaint.setColor(defaultTextColor);
    }

    private void initBarPaint(Progress progress) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(progress.color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(defaultBarSize);
        barPaintList.add(paint);
    }

    private void initBarPint(Progress... progresses) {
        barPaintList.clear();
        for (Progress progress : progresses) initBarPaint(progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw bottom border
        if (bottomBorder)
            canvas.drawRect(0, getHeight(), getWidth(), getHeight(), borderPaint);

        // draw right border
        if (rightBorder)
            canvas.drawRect(getWidth(), 0, getWidth(), getHeight(), borderPaint);

        //  calculate the gap
        float gap = (float) (getWidth() / (progressList.size() + 1));

        if (progressList.size() < 1) return;

        for (int i = 1; i <= barPaintList.size(); i++) {
            int key = i - 1;
            float barHeight = (float) (getHeight() / 100) * progressList.get(key);
            float offsetY = getHeight() - barHeight + defaultTextSize + bound.height();
            canvas.drawRect(gap * i, offsetY, gap * i, getHeight(), barPaintList.get(key));

            if (showLabel) {
                String value = progressList.get(key) + "%";
                textPaint.getTextBounds(value, 0, value.length(), bound);
                canvas.drawText(value, gap * i - ((float) bound.width() / 2), offsetY - defaultTextSize, textPaint);
            }
            // canvas.drawText("Abdul", gap * 1, offsetY - 16, textPaint);
        }

    }

    public void setProgress(Progress... progresses) {
        initBarPint(progresses);
        for (Progress progress : progresses) progressList.add(progress.progress);
        invalidate();
        requestLayout();
    }

    public void setProgress(ArrayList<Progress> progresses) {
        barPaintList.clear();
        progressList.clear();
        for (Progress progress : progresses) {
            initBarPaint(progress);
            progressList.add(progress.progress);
        }
        invalidate();
        requestLayout();
    }


    public void setBarSize(int barSize) {
        for (Paint paint : barPaintList) paint.setStrokeWidth(barSize);
        invalidate();
        requestLayout();
    }

    public void setBorderSize(int size) {
        borderPaint.setStrokeWidth(size);
        invalidate();
        requestLayout();
    }

    public void setBorderColor(int color) {
        borderPaint.setColor(color);
        invalidate();
        requestLayout();
    }


    public void setRightBorder(boolean rightBorder) {
        this.rightBorder = rightBorder;
        invalidate();
        requestLayout();
    }

    public void setBottomBorder(boolean bottomBorder) {
        this.bottomBorder = bottomBorder;
        invalidate();
        requestLayout();
    }

    public void animateView() {
        ViewHelper.animateHeart(this, 1, 400);
    }

    public static class Progress {
        public float progress;
        public int color;

        public Progress(float progress, int color) {
            this.progress = progress;
            this.color = color;
        }
    }

}
