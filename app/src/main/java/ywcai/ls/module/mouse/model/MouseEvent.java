package ywcai.ls.module.mouse.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ywcai.ls.module.mouse.model.net.RequestMsg;
import ywcai.ls.module.mouse.presenter.inf.MouseEventInf;

public class MouseEvent implements SensorEventListener, MouseEventInf {
    private RequestMsg requestMsg = new RequestMsg();
    private boolean move = false;
    public MouseEvent(Context context) {
        RegOrientationListener(context);
    }
    private void RegOrientationListener(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (move) {
            int x = Math.round(event.values[0] * 100);
            int y = Math.round(event.values[2] * 100);
            x = Math.abs(x) < 3 ? 0 : x;
            y = Math.abs(y) < 3 ? 0 : y;
            if (x != 0 || y != 0) {
                requestMsg.sendJson("move", x + "_" + y);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void sendMoveDown() {
        move = true;
    }

    @Override
    public void sendMoveUp() {
        move = false;
    }

    @Override
    public void sendLeftDown() {
        requestMsg.sendJson("click", "left_down");
    }

    @Override
    public void sendLeftUp() {
        requestMsg.sendJson("click", "left_up");
    }

    @Override
    public void sendRightDown() {
        requestMsg.sendJson("click", "right_down");
    }

    @Override
    public void sendRightUp() {
        requestMsg.sendJson("click", "right_up");
    }

    @Override
    public void sendPagePrev() {
        requestMsg.sendJson("click", "page_up");
    }

    @Override
    public void sendPageNext() {
        requestMsg.sendJson("click", "page_down");
    }

    @Override
    public void sendEsc() {
        requestMsg.sendJson("click", "esc");
    }

    @Override
    public void sendExitBack() {

    }
}
