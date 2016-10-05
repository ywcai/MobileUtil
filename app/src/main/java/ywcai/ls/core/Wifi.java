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
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import ywcai.ls.bean.BsrLineObj;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.control.CurveView;
import ywcai.ls.core.task.RefreshWifi2d4G;
import ywcai.ls.core.task.RefreshWifi5G;
import ywcai.ls.core.task.RefreshWifiDbm;
import ywcai.ls.core.task.RefreshWifiList;
import ywcai.ls.inf.CallBackMainTitle;
import ywcai.ls.inf.WifiRefreshInf;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.main.fragment.sub.WifiDbmRecordFragment;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Wifi extends BroadcastReceiver {
    private CallBackMainTitle fragmentCallBack;
    private Context context;
    private WifiManager wifiMg;
    private String connMac = "-1", connIp = "-1";
    private int connSpeed = -1;
    private int checkScanCount = 0, selfAdd = 0, scanAutoFlag = 0;
    private List<WifiInfo> list=null;
    private HashMap<String, BsrLineObj> hashMap2d4G=null, hashMap5G=null;
    private WifiRefreshInf wifiRefreshInf;

    public Wifi(CallBackMainTitle pFragmentCallBack, WifiRefreshInf pWifiRefreshInf) {
        fragmentCallBack = pFragmentCallBack;
        wifiRefreshInf = pWifiRefreshInf;
        context = MyApplication.getInstance().getApplicationContext();
        wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        list = new ArrayList<>();
        hashMap2d4G = new HashMap<>();
        hashMap5G = new HashMap<>();
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
                while (scanAutoFlag < MyConfig.INT_CHECK_WIFI_AUTO_SCAN_COUNT) {
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        fragmentCallBack.callBackSetTitle("没有wifi权限");
        return false;
    }

    private void ListenerWifiEnableStatus() {
        int state = wifiMg.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            fragmentCallBack.callBackSetTitle("WIFI模块开启！");
            ListenerAllWifi();
        }
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            fragmentCallBack.callBackSetTitle("WIFI模块关闭！");
            ClearList();
        }
        if (state == WifiManager.WIFI_STATE_ENABLING) {
            fragmentCallBack.callBackSetTitle("正在开启WIFI...");
        }
        if (state == WifiManager.WIFI_STATE_DISABLING) {
            fragmentCallBack.callBackSetTitle("正在关闭WIFI...");
        }
    }

    private void ListenerWifiConnStatus(Intent intent) {
        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            State state = networkInfo.getState();
            if (state == State.CONNECTED) {
                fragmentCallBack.callBackSetTitle("WIFI已连接！");
                ListenerWifiConn();
            }
            if (state == State.CONNECTING) {
                fragmentCallBack.callBackSetTitle("正在连接WIFI...");
            }
            if (state == State.DISCONNECTING) {
                fragmentCallBack.callBackSetTitle("正在断开WIFI...");
            }
            if (state == State.DISCONNECTED) {
                fragmentCallBack.callBackSetTitle("WIFI未连接");
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

    private boolean checkGpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    private void ListenerAllWifi() {
        list.clear();
        ClearList();
        if (!checkPermission()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkGpsStatus()) {
                wifiRefreshInf.SetListInfoTip("系统版本为6.0以上需要开启GPS才可扫描WIFI!");
                return;
            }
        }
        List<ScanResult> results;
        try {
            results = wifiMg.getScanResults();
        } catch (Exception e) {
            results = null;
        }
        if (results == null) {
            wifiRefreshInf.SetListInfoTip("没扫描到任何wifi信号");
            return;
        }
        selfAdd++;
        int[] channelSum = new int[166];
        for (ScanResult result : results) {
            WifiInfo wifiInfo = new WifiInfo();
            wifiInfo.sid = result.SSID;
            wifiInfo.mac = result.BSSID;
            wifiInfo.channel = MyUtil.ConvertFrequencyToChannel(result.frequency);
            wifiInfo.dbm = result.level;
            wifiInfo.device = "";
            wifiInfo.frequency = result.frequency;
            wifiInfo.keyType = result.capabilities;
            if (connMac.toString().equals(result.BSSID.toString())) {
                wifiInfo.isConnWifi = true;
                wifiInfo.device = connIp;
                wifiInfo.speed = connSpeed;
                list.add(0, wifiInfo);
            } else {
                list.add(wifiInfo);
            }
            channelSum[wifiInfo.channel]++;
            updateHashMap(wifiInfo);
        }
        UpdateAllUI(channelSum);
    }
    private void updateHashMap(WifiInfo wifiInfo) {
        if (wifiInfo.channel < 14 && wifiInfo.channel > 0) {
            if (hashMap2d4G.containsKey(wifiInfo.mac)) {
                BsrLineObj bsrLineObj = hashMap2d4G.get(wifiInfo.mac);
                bsrLineObj.isNew = false;
                bsrLineObj.isExist = true;
                bsrLineObj.wifiInfo = wifiInfo;
            } else {
                BsrLineObj bsrLineObj = new BsrLineObj();
                bsrLineObj.isNew = true;
                bsrLineObj.isExist = true;
                bsrLineObj.wifiInfo = wifiInfo;
                bsrLineObj.curveView = new CurveView(context);
                hashMap2d4G.put(wifiInfo.mac, bsrLineObj);
            }
        }
        if (wifiInfo.channel >= 14) {
            if (hashMap5G.containsKey(wifiInfo.mac)) {
                BsrLineObj bsrLineObj = hashMap5G.get(wifiInfo.mac);
                bsrLineObj.isNew = false;
                bsrLineObj.isExist = true;
                bsrLineObj.wifiInfo = wifiInfo;
            } else {
                BsrLineObj bsrLineObj = new BsrLineObj();
                bsrLineObj.isNew = true;
                bsrLineObj.isExist = true;
                bsrLineObj.wifiInfo = wifiInfo;
                bsrLineObj.curveView = new CurveView(context);
                hashMap5G.put(wifiInfo.mac, bsrLineObj);
            }
        }
    }

    private void UpdateAllUI(int[] channelSum) {
        wifiRefreshInf.UpdateListInfoList(list);
        wifiRefreshInf.SetListInfoTip("扫描到" + list.size() + "个WIFI信号");
        wifiRefreshInf.UpdateChanelCount(channelSum);
        if(hashMap2d4G!=null) {
            wifiRefreshInf.UpdateGraphic2d4G(hashMap2d4G);
        }
        if(hashMap5G!=null) {
            wifiRefreshInf.UpdateGraphic5G(hashMap5G);
        }
        if (list!=null) {
            wifiRefreshInf.UpdateGraphicDbm(list);
        }
}

    private void ClearList() {
        wifiRefreshInf.ClearListInfoUI();
    }
}