package ywcai.ls.core;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class GpsTest implements LocationListener, GpsStatus.Listener {


    private View tabView;
    private Context context;
    private LocationManager lm;
    private boolean isGpsEnable = false;
    private boolean gps_finding = false;
    private boolean gps_output_format=true;
    private double gps_j=104.3489031,gps_w=30.3543514;


    public GpsTest(View view) {
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        InitGpsStatus();
    }
    public void InitGpsStatus()
    {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(context, "您没有打开GPS模块", Toast.LENGTH_SHORT).show();
                isGpsEnable = false;

            } else {
                //Toast.makeText(context, "GPS模块为开启状态", Toast.LENGTH_SHORT).show();
                isGpsEnable = true;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.addGpsStatusListener(this);
        } else {
            //Toast.makeText(context, "没有获取到GPS使用权限", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            //模块开启后第一次获取到位置信息
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                gps_finding = false;//设置搜索状态false
                //Toast.makeText(context, "GP获取位置成功",Toast.LENGTH_SHORT).show();
                break;
            //GPS模块的工作状态
            case GpsStatus.GPS_EVENT_STARTED:
                gps_finding = true;//模块进入工作状态后开启卫星搜索
                //Toast.makeText(context, "GPS已进入工作状态",Toast.LENGTH_SHORT).show();
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                gps_finding = false;//模块检测到工作状态关闭，设置false

                //Toast.makeText(context,"GPS已结束工作状态",Toast.LENGTH_SHORT).show();
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                GpsStatus gpsStatus = lm.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) {
                    count++;
                    iters.next();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //只要获取到位置，即可认为系统已完成定位

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //gps_finding=true;此处不设置，在检查到GPS工作后再设置ture;
        //Toast.makeText(context, "GPS模块开启", Toast.LENGTH_SHORT).show();
        isGpsEnable = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(context, "GPS模块关闭",Toast.LENGTH_SHORT).show();
        isGpsEnable = false;

    }

    //程序自动开启GPS硬件模块，GPS打开只能通过系统自带的设置菜单，无法重系统直接开启
    private void openGPS() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    public void setGps()
    {
        try {
            Location location=new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(gps_j);
            location.setLongitude(gps_w);
            location.setAccuracy(0.1f);
        }
        catch (Exception e) {
            Toast.makeText(context, "模拟器异常" + e.toString() + "", Toast.LENGTH_LONG).show();
        }
    }

}
