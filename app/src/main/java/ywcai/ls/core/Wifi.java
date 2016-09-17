package ywcai.ls.core;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import ywcai.ls.adapter.WifiAdapter;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Wifi extends BroadcastReceiver {
    private List<WifiInfo> list;
    private WifiAdapter wifiAdpter;
    private View tabView;
    private Context context;
    private WifiManager wifiMg;
    private TextView tv_title_wifi, tv_wifiTip;
    private String connMac = "-1";
    private int connSpeed = -1;
    private String connIp = "-1";
    private int checkScanCount=0;
    private int selfAdd=0;
    private int scanAutoFlag=0;


    public Wifi(View view) {
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        tv_title_wifi = (TextView) tabView.findViewById(R.id.tv_connNet);
        tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
        wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        list = new ArrayList<WifiInfo>();
        ListViewCompat listView = (ListViewCompat) tabView.findViewById(R.id.now_wifiInfo);
        wifiAdpter = new WifiAdapter(list);
        listView.setAdapter(wifiAdpter);
        context.registerReceiver(this, new IntentFilter(
                WifiManager.WIFI_STATE_CHANGED_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.RSSI_CHANGED_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.EXTRA_WIFI_STATE));
        context.registerReceiver(this, new IntentFilter(
                WifiManager.NETWORK_STATE_CHANGED_ACTION));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(scanAutoFlag< MyConfig.INT_CHECK_WIFI_AUTO_SCAN_COUNT) {
                    try {
                        Thread.sleep(MyConfig.INT_WIFI_AUTO_SCAN_REFRESH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkScanCount++;
                    if (checkScanCount > selfAdd) {
                        checkScanCount = 0;
                        selfAdd = 0;
                        wifiMg.startScan();
                    }
                    if (checkScanCount < selfAdd - 2) {
                        checkScanCount = 0;
                        selfAdd = 0;
                        scanAutoFlag++;
                    }
                }
            }
        }).start();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction().toString()) {
            case WifiManager.RSSI_CHANGED_ACTION:


                break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                ListenerAllWifi();
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                ListenerWifiEnableStatus();
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                ListenerWifiConnStatus(intent);
                break;
        }
    }

    private boolean checkRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        tv_wifiTip.setText("你没有授予应用权限");
        return false;
    }


    private void ListenerWifiEnableStatus() {
        int state = wifiMg.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            tv_title_wifi.setText("WIFI模块开启！");
            ListenerAllWifi();
        }
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            tv_title_wifi.setText("WIFI模块关闭！");
            tv_wifiTip.setText("WIFI模块已关闭！");
            list.clear();
            wifiAdpter.notifyDataSetChanged();
        }
        if (state == WifiManager.WIFI_STATE_ENABLING) {
            tv_title_wifi.setText("WIFI模块正在开启......");
        }
        if (state == WifiManager.WIFI_STATE_DISABLING) {
            tv_title_wifi.setText("WIFI模块正在关闭......");

        }
    }

    private void ListenerWifiConnStatus(Intent intent) {
        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            State state = networkInfo.getState();
            if (state == State.CONNECTED) {
                tv_title_wifi.setText("WIFI已连接！");
                ListenerWifiConn();
            }
            if (state == State.CONNECTING) {
                tv_title_wifi.setText("正在连接WIFI......");
            }
            if (state == State.DISCONNECTING) {
                tv_title_wifi.setText("正在断开WIFI......");
            }
            if (state == State.DISCONNECTED) {
                tv_title_wifi.setText("WIFI未连接！");
                ListenerWifiDisconnect();
            }
            if (state == State.SUSPENDED) {
                tv_title_wifi.setText("WIFI状态:State.SUSPENDED");
                ListenerWifiDisconnect();
            }
        }
    }

    private void ListenerWifiDisconnect() {
        connMac = "-1";
        connSpeed = -1;
        connIp = "-1";
    }

    private void ListenerWifiConn() {
        try {
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            String ip = MyUtil.ConvertIpToStr(wifiInfo.getIpAddress());
            connMac = wifiInfo.getBSSID();
            connSpeed = wifiInfo.getLinkSpeed();
            connIp = ip;
        } catch (Exception e) {
            connMac = "-1";
            connSpeed = -1;
            connIp = "-1";
        }
    }

    public boolean checkGpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    private void ListenerAllWifi() {
        list.clear();
        if (!checkRequestLocation()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkGpsStatus()) {
                wifiAdpter.notifyDataSetChanged();
                tv_wifiTip.setText("温馨提示：您系统版本为6.0以上\n需要开启GPS后可扫描WIFI!");
                return;
            }
        }
        List<ScanResult> results=null;
        try {
            results = wifiMg.getScanResults();
        }
        catch(Exception e)
        {
            results=null;
        }
        if(results==null)
        {
            wifiAdpter.notifyDataSetChanged();
            tv_wifiTip.setText("未扫描到网络");
            return ;
        }
        selfAdd++;
        int[] channelSum = new int[166];
        for (ScanResult result : results) {
            WifiInfo wifiInfos = new WifiInfo();
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            wifiInfos.sid = result.SSID;
            wifiInfos.mac = result.BSSID;
            wifiInfos.channel = MyUtil.ConvertFrequencyToChannel(result.frequency);
            wifiInfos.dbm = result.level;
            wifiInfos.device = "";
            wifiInfos.frequency = result.frequency;
            wifiInfos.keyType = result.capabilities;
            if (connMac.toString().equals(result.BSSID.toString())) {
                wifiInfos.isConnWifi = true;
                wifiInfos.device = connIp;
                wifiInfos.speed = connSpeed;
                list.add(0, wifiInfos);
            } else {
                list.add(wifiInfos);
            }
            channelSum[wifiInfos.channel]++;
        }
        wifiAdpter.notifyDataSetChanged();
        String s = "";
        s += "扫描到WIFI连接: " + list.size() + " 个，信道分布情况:\n";
        for (int i = 1; i < 14; i++) {
            if (channelSum[i] != 0) {
                s += "信道" + i + " [" + channelSum[i] + "]   ";
            }
        }
        for (int i = 149; i < 166; i++) {
            if (channelSum[i] != 0) {
                s += "信道" + i + " [" + channelSum[i] + "]   ";
            }
        }
        tv_wifiTip.setText(s);
    }
}