package ywcai.ls.core;


import android.content.Context;
import android.hardware.*;
import android.hardware.Sensor;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;


/**
 * Created by zmy_11 on 2016/8/12.
 */


public class Orientation implements SensorEventListener {
    private ImageView imgPoint;
    private TextView textDegree;
    private View tabView;
    private Context context;
    private int from = 0;
    private int to = 0;
    private float x = 0;
    private float y = 0;

    public Orientation(View view,Context pContext) {
        tabView = view;
        context = pContext;
        InitView();
    }
    @SuppressWarnings("deprecation")
    private void InitView() {
        imgPoint = (ImageView) tabView.findViewById(R.id.img_point);
        textDegree = (TextView) tabView.findViewById(R.id.text_degree);
        ViewTreeObserver viewTreeObserver = imgPoint.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                x = imgPoint.getWidth() / 2.0f;
                y = imgPoint.getHeight() / 2.0f;
            }
        });
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        to = (int) event.values[0];
        if (from != to) {
            if (x > 0 && y > 0) {
                textDegree.setText(to + "°");
                startChange(from, to);
            }
                from = to;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void startChange(int from, int to) {
        if (from <= 60 && to >= 300) {
            to = to - 360;
        }
        if (from >= 300 && to <= 60) {
            from = from - 360;
        }

            Animation animation = new RotateAnimation(from, to, x, y);
            animation.setDuration(20);
            animation.setFillAfter(true);
            imgPoint.startAnimation(animation);

    }
}
