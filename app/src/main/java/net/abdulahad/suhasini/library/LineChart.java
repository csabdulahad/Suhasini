package net.abdulahad.suhasini.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import net.abdulahad.suhasini.helper.ViewHelper;

import java.util.ArrayList;

public class LineChart extends View {

    private Canvas canvas;

    private boolean rightBorder = false;
    private boolean bottomBorder = false;

    private final int defaultBarSize = 5;
    private final int defaultBorderSize = 1;

    private final int defaultBorderColor = Color.GRAY;
    private final int defaultBarColor = Color.RED;

    private ArrayList<Float> progressList;

    private Paint borderPaint;
    private Paint barPaint;

    private boolean fillChart = true;

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
        progressList = new ArrayList<>();
        initBorderPaint();
        initBarPaint();
    }

    private void initBorderPaint() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(defaultBorderSize);
        borderPaint.setColor(defaultBorderColor);
    }

    private void initBarPaint() {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setColor(defaultBarColor);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setStrokeWidth(defaultBarSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.canvas == null) this.canvas = canvas;

        // draw bottom border
        if (bottomBorder)
            canvas.drawRect(0, getHeight(), getWidth(), getHeight(), borderPaint);

        // draw right border
        if (rightBorder)
            canvas.drawRect(getWidth(), 0, getWidth(), getHeight(), borderPaint);

        if (fillChart) drawFillChart(canvas);
        else drawStrokeChart(canvas);
    }

    private void drawFillChart(Canvas canvas) {
        barPaint.setStyle(Paint.Style.FILL);

        float barSize = (float) getWidth() / progressList.size();

        float gap = 0;
        for (int i = 0; i < progressList.size(); i++) {
            float barHeight = (float) (getHeight() / 100) * progressList.get(i);
            float offsetY = getHeight() - barHeight;
            gap = i * barSize;

            canvas.drawRect(gap, offsetY, gap + barSize, getHeight(), barPaint);
        }
    }

    private void drawStrokeChart(Canvas canvas) {
        barPaint.setStyle(Paint.Style.STROKE);

        //  calculate the gap
        float gap = (float) (getWidth() / (progressList.size() + 1));
        for (int i = 1; i <= progressList.size(); i++) {
            int key = i - 1;
            float barHeight = (float) (getHeight() / 100) * progressList.get(key);
            float offsetY = getHeight() - barHeight;
            canvas.drawRect(gap * i, offsetY, gap * i, getHeight(), barPaint);
        }
    }

    float duration = 2000;

    private void animateBar() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("ahad", "Name" + Thread.currentThread().getName());
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void setProgress(ArrayList<Float> progresses) {
        progressList.clear();
        progressList.addAll(progresses);
        invalidate();
        requestLayout();
    }

    public void setBorderSize(int size) {
        borderPaint.setStrokeWidth(size);
        invalidate();
        requestLayout();
    }

    public void setBarColor(int color) {
        barPaint.setColor(color);
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

}
