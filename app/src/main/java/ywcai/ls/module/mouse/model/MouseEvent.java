package ywcai.ls.module.mouse.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ywcai.ls.common.ComponentStatus;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.module.mouse.presenter.inf.MouseEventInf;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

public class MouseEvent implements SensorEventListener, MouseEventInf {
    private boolean move = false;
    private ClientSocket temp = ComponentStatus.getInstance().socket;

    public MouseEvent(Context context) {
        RegOrientationListener(context);
    }

    private void RegOrientationListener(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!temp.getSessionStatus()) {
            return;
        }
        if (move) {
            int x = Math.round(event.values[2] * 100);
            int y = Math.round(event.values[0] * 100);
            x = Math.abs(x) < 3 ? 0 : x;
            y = Math.abs(y) < 3 ? 0 : y;
            if (x != 0 || y != 0) {
                    MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_move, x + "|" + y);
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
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_l_down, "");
    }

    @Override
    public void sendLeftUp() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_l_up, "");
    }

    @Override
    public void sendRightDown() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_r_down, "");
    }

    @Override
    public void sendRightUp() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_r_up, "");
    }

    @Override
    public void sendPagePrev() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_page_up, "");
    }

    @Override
    public void sendPageNext() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_page_down, "");
    }

    @Override
    public void sendEsc() {
        if(!temp.getSessionStatus()) {
            return;
        }
        MesUtil.sendJson(temp, ResultCode.json_type_cmd_mouse_event_esc, "");
    }
}
