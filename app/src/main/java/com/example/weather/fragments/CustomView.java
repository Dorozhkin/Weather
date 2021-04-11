package com.example.weather.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.weather.R;


public class CustomView  extends androidx.appcompat.widget.AppCompatImageView {

    private float left;
    private float center;
    private float right;
    Paint white = new Paint();
    Path path = new Path();
    Shader lg;

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomView(Context context) {
        super(context);
        init();
    }
    private void init() {
        white.setColor(Color.WHITE);
        white.setTextSize(40);
        white.setStyle(Paint.Style.FILL_AND_STROKE);
        white.setStrokeWidth(5);
        white.setDither(true);
        white.setAntiAlias(true);
        lg = new LinearGradient(200,0, 200, 550, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        white.setShader(lg);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("THE WIDTH", String.valueOf(getWidth()));
        int height = getHeight();
        int width = getWidth();

        int yStart = (int) (height*left);
        int yMiddle = (int) (height*center);
        int yStop = (int) (height*right);
        int xMiddle = width/2;
        int xStop = width;

        path.reset();
        path.moveTo(0, yStart);

        path.cubicTo(0, yStart, xMiddle, yMiddle, xStop, yStop);
        path.lineTo(xStop, height);
        path.lineTo(0, height);
        path.close();

        canvas.drawPath(path, white);
    }

    public void setLeft(float left) {
        this.left = left;
    }
    public void setCenter(float center) {
        this.center = center;
    }
    public void setRight(float right) {
        this.right = right;
    }
}
