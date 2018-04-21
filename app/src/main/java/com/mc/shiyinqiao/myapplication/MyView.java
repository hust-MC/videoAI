package com.mc.shiyinqiao.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by shiyinqiao on 2018/4/21.
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    private float x_1;
    private float y_1;
    private float x_2;
    private float y_2;

    public void setPosition(float x_set1, float y_set1, float x_set2, float y_set2) {
        x_1 = x_set1;
        x_2 = x_set2;
        y_1 = y_set1;
        y_2 = y_set2;

    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(x_1, y_1, x_2, y_2, paint);

        //  RectF rect = new RectF(3, 3, 50, 60);
//        canvas.drawOval(rect, paint);

    }
}
