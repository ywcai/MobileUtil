package ywcai.ls.core;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.ui.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Gps implements UpdateViewInf, LocationListener, GpsStatus.Listener {

    private String gps_x = "0", gps_y = "0";
    private int gps_h = 0, gps_num = 0;//高层单位为毫米
    private boolean isGpsEnable = false;//GPS模块开关状态
    private boolean gps_finding = false;//GPS是否在搜索卫星的状态， 未开启和已定位都将是false。
    private Context context;
    private LocationManager lm;
    private List<HashMap<String, String>> list;
    private SimpleAdapter adpter;
    private View tabView;



    public Gps(View view, Activity pActivity) {
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        InitGpsStatus();
        InitListView();
    }
    public void InitGpsStatus()
    {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        SwitchCompat switchCompat=(SwitchCompat)tabView.findViewById(R.id.isGpsSave);
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(context, "您没有打开GPS模块", Toast.LENGTH_SHORT).show();
                isGpsEnable = false;
                switchCompat.setChecked(false);
            } else {
                //Toast.makeText(context, "GPS模块为开启状态", Toast.LENGTH_SHORT).show();
                isGpsEnable = true;
                switchCompat.setChecked(true);
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.addGpsStatusListener(this);
        } else {
            //Toast.makeText(context, "没有获取到GPS使用权限", Toast.LENGTH_SHORT).show();
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    openGPS();
                }
            }
        });
    }

    public void InitListView()
    {
        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_gps);
        list = new ArrayList<>();
        list = getList();
        adpter = new SimpleAdapter(context, list, R.layout.listview_gps, new String[]{"title", "info"}, new int[]{R.id.title_gps, R.id.info_gps});
        listViewCompat.setAdapter(adpter);
        updateStatus();
    }
    @Override
    public String toString() {
        String searchStatus = "";
        if (isGpsEnable && gps_finding) {
            searchStatus = "正在搜索...";
        } else {
            searchStatus = "未搜星";
        }
        String gpsStatus="";
        if (isGpsEnable) {
            gpsStatus = "已打开";
        } else {
            gpsStatus = "已关闭";
        }
        return
                "GPS工作状态=" + searchStatus +
                        ",GPS硬件是否开启=" + gpsStatus +
                        ",当前收星数量=" + gps_num +
                        ",WGS84-经度=" + gps_x +
                        ",WGS84-维度=" + gps_y +
                        ",WGS84-高程=" + gps_h + "mm"
                ;
    }

    public List<HashMap<String, String>> getList() {
        String[] strs = toString().split(",");
        if (list.size() > 0) {
            list.clear();
        }
        for (int n = 0; n < strs.length; n++) {
            HashMap<String, String> hp = new HashMap<String, String>();
            hp.put("title", strs[n].split("=")[0]);
            hp.put("info", strs[n].split("=")[1]);
            list.add(hp);
        }
        return list;
    }

    @Override
    public void updateView() {
        updateStatus();
        list = getList();
        adpter.notifyDataSetChanged();

    }
    private void updateStatus() {
        ContentLoadingProgressBar loadingProgressBar = (ContentLoadingProgressBar) tabView.findViewById(R.id.findGps);
            if (gps_finding && isGpsEnable) {
            loadingProgressBar.show();
        } else {
            loadingProgressBar.hide();
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
                gps_num = 0;//重置数据
                clearGPS();//重置数据
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
                gps_num = count;
                if (count < 4) {
                    clearGPS();//搜索小于4颗后则重置GPS数据
                    gps_finding = true;//如果GPS模块在工作，且搜到的星少于4颗,则设置GPS正在搜索状态;
                } else {
                    //四颗以上可以有定位数据，也可以没有，这里不做判别，通过上面的位置变换事件获取到位置信息来判别是否获取到信号;
                }
                break;
        }
        updateView();
    }

    @Override
    public void onLocationChanged(Location location) {
        //只要获取到位置，即可认为系统已完成定位
        gps_finding = false;
        //只有GPS开启的时候才获取经纬度变换信息，否则则缓存的上一次的位置
        if (isGpsEnable) {
            gps_x = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS);
            gps_y = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS);
            gps_h = (int) (location.getAltitude() * 1000);//高程，单位mm
        }
        updateView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //gps_finding=true;此处不设置，在检查到GPS工作后再设置ture;
        //Toast.makeText(context, "GPS模块开启", Toast.LENGTH_SHORT).show();
        isGpsEnable = true;
        updateView();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(context, "GPS模块关闭",Toast.LENGTH_SHORT).show();
        isGpsEnable = false;
        updateView();
    }

    //当处于搜索状态时，重置位置信息为0，不然会持续上报最近一次获得的位置
    private void clearGPS() {
        gps_x = "0";
        gps_y = "0";
        gps_h = 0;
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
}
