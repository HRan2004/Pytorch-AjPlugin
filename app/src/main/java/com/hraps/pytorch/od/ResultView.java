package com.hraps.pytorch.od;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class ResultView extends View {

    private final static int TEXT_X = 20;
    private final static int TEXT_Y = 15;
    private final static int TEXT_WIDTH = 240;
    private final static int TEXT_HEIGHT = 50;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<OdResult> mOdResults;

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mOdResults == null) return;
        for (OdResult odResult : mOdResults) {
            int rectColor = Color.argb(200,30,100,255);

            mPaintRectangle.setStrokeWidth(5);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            mPaintRectangle.setColor(rectColor);
            canvas.drawRect(odResult.rect, mPaintRectangle);

            Path mPath = new Path();
            RectF mRectF = new RectF(odResult.rect.left, odResult.rect.top-TEXT_HEIGHT, odResult.rect.left + TEXT_WIDTH,  odResult.rect.top);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPaintText.setColor(rectColor);
            canvas.drawPath(mPath, mPaintText);

            mPaintText.setColor(Color.argb(200,250,250,250));
            mPaintText.setStrokeWidth(0);
            mPaintText.setStyle(Paint.Style.FILL);
            mPaintText.setTextSize(32);
            canvas.drawText(String.format("%s %.3f", PrePostProcessor.mClasses[odResult.classIndex], odResult.score), odResult.rect.left + TEXT_X, odResult.rect.top - TEXT_Y, mPaintText);
        }
    }

    public void setResults(ArrayList<OdResult> odResults) {
        mOdResults = odResults;
    }
}