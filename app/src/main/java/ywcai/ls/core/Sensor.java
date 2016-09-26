package ywcai.ls.core;


import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import ywcai.ls.adapter.SensorAdapter;
import ywcai.ls.bean.SensorInfo;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Sensor implements UpdateViewInf , SensorEventListener {

    private List<SensorInfo> list;
    private SensorAdapter adpter;
    private View tabView;
    private Context context;
    private SensorEventListener listener;
    private int permissionMaxSensorNum=3;
    private int openSensorNum=0;
    private HashMap<Integer,Integer> sensorPos=new HashMap<>();


    public Sensor(View view) {
        context=MyApplication.getInstance().getApplicationContext();
        tabView = view;
        listener=this;
        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_sensor);
        list = getList();
        adpter=new SensorAdapter(list);
        listViewCompat.setAdapter(adpter);
        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!list.get(position).isDetail) {
                    if(openSensorNum<permissionMaxSensorNum)
                    {
                        list.get(position).isDetail = true;
                        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(list.get(position).sensorType), SensorManager.SENSOR_DELAY_UI);
                        openSensorNum++;
                    }
                } else {
                    list.get(position).isDetail = false;
                    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                    sensorManager.unregisterListener(listener, sensorManager.getDefaultSensor(list.get(position).sensorType));
                    openSensorNum--;
                }
                adpter.notifyDataSetChanged();
                TextView tv=(TextView)tabView.findViewById(R.id.tip_sensor);
                tv.setText("应用同时只允许开启" + permissionMaxSensorNum+"个传感器，目前已开启"+openSensorNum+"个");

            }
        });
    }

    public List<SensorInfo> getList()
    {
        SensorManager sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<android.hardware.Sensor> sensors = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);
        if(list==null)
        {
            list = new ArrayList<SensorInfo>();
        }
        for (int i=0;i<sensors.size();i++)
        {
            sensorPos.put(sensors.get(i).getType(),i);
            SensorInfo sensorInfo=new SensorInfo();
            android.hardware.Sensor sensor= sensors.get(i);
            switch (sensor.getType()) {
                case android.hardware.Sensor.TYPE_GRAVITY:
                    sensorInfo.name_cn="重力";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_ACCELEROMETER:
                    sensorInfo.name_cn="加速度含重力";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_LINEAR_ACCELERATION:
                    sensorInfo.name_cn="加速度除重力";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_GAME_ROTATION_VECTOR:
                    sensorInfo.name_cn="游戏动作";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                    sensorInfo.name_cn="地磁矢量";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_GYROSCOPE:
                    sensorInfo.name_cn="陀螺仪";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    sensorInfo.name_cn="未校准陀螺仪";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_LIGHT:
                    sensorInfo.name_cn="光感";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD:
                    sensorInfo.name_cn="磁力计";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    sensorInfo.name_cn="未校准磁力计";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_PRESSURE:
                    sensorInfo.name_cn="气压";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_PROXIMITY:
                    sensorInfo.name_cn="距离";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sensorInfo.name_cn="温度";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY:
                    sensorInfo.name_cn="湿度";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_ROTATION_VECTOR:
                    sensorInfo.name_cn="旋转矢量";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_SIGNIFICANT_MOTION:
                    sensorInfo.name_cn="特殊动作";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_STEP_COUNTER:
                    sensorInfo.name_cn="计步器";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_STEP_DETECTOR:
                    sensorInfo.name_cn="步行检测";
                    sensorInfo.name_native=sensor.getName();
                    break;
                case android.hardware.Sensor.TYPE_ORIENTATION:
                    sensorInfo.name_cn="方向传感";
                    sensorInfo.name_native=sensor.getName();
                    break;
                default:
                    sensorInfo.name_cn="未知";
                    sensorInfo.name_native=sensor.getName();
                    break;
            }
            sensorInfo.isDetail=false;
            sensorInfo.sensorType=sensor.getType();
            sensorInfo.collect_data="";
            list.add(sensorInfo);
        }
        return list;
    }

    @Override
    public void updateView() {
        list = getList();
        adpter.notifyDataSetChanged();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorInfo sensorInfo;
        int pos=sensorPos.get(event.sensor.getType());
        sensorInfo = list.get(pos);
        String temp="";
        for(int i=0;i<event.values.length;i++)
        {
            temp+="value"+i+" : "+(int)event.values[i]+" ";
        }
        if(!temp.equals(sensorInfo.collect_data))
        {
            sensorInfo.collect_data = temp;
            adpter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy)
    {

    }
}
