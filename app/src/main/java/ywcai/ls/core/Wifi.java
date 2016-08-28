package ywcai.ls.core;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.adapter.WifiAdapter;
import ywcai.ls.bean.WifiInfos;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyUtil;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Wifi extends BroadcastReceiver {
    private List<WifiInfos> list;
    private WifiAdapter wifiAdpter;
    private View tabView;
    private Context context;
    private WifiManager wifiMg;
    private TextView tv_title_wifi;
    private String connSID = "-1";
    private String connMac = "-1";
    private int connSpeed = -1;
    private String connIp = "-1";

    public Wifi(View view) {
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        tv_title_wifi = (TextView) tabView.findViewById(R.id.tv_connNet);
        wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        list = new ArrayList<WifiInfos>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            InitGpsStatus();
        }
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
    }

    public void InitGpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil") == PackageManager.PERMISSION_GRANTED) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(context, "温馨提示：您系统版本为6.0以上\n需要开启GPS后可扫描WIFI!", Toast.LENGTH_LONG).show();
                TextView tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
                tv_wifiTip.setText("温馨提示：您系统版本为6.0以上\n需要开启GPS后可扫描WIFI!");
            } else {
                //Toast.makeText(context, "GPS模块为开启状态", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(context, "没有获取到GPS使用权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction().toString()) {
            case WifiManager.RSSI_CHANGED_ACTION:
                ListenerConnWifi();
                ListenerAllWifi();
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                ListenerStatusWifi();
                break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                ListenerAllWifi();
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                ListenerConnWifi();
                break;

        }
    }

    private void ListenerStatusWifi() {
        int state = wifiMg.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            ListenerConnWifi();
            ListenerAllWifi();
        }
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            tv_title_wifi.setText("WIFI模块关闭！");
        }
    }

    private void ListenerConnWifi() {
        WifiInfo wifiInfo = wifiMg.getConnectionInfo();
        String s = "";
        try {
            String ip = MyUtil.ConvertIpToStr(wifiInfo.getIpAddress());
            if ((WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED
                    || WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.OBTAINING_IPADDR) && wifiInfo.getLinkSpeed() > 0) {
                //  s = "连接网络：" + wifiInfo.getSSID() + "   速率：" + wifiInfo.getLinkSpeed() + "\n网关:" + ip;
                connMac = wifiInfo.getBSSID();
                connSpeed = wifiInfo.getLinkSpeed();
                connIp = ip;

            } else {
                connSID = "-1";
                connMac = "-1";
                connSpeed = -1;
                connIp = "-1";
                //s = "无WIFI连接 ! ";
            }
        } catch (Exception e) {
            //s = "WIFI连接获取信息异常" + e;
            connSID = "-1";
            connSpeed = -1;
            connIp = "-1";
        }
        //tv_title_wifi.setText(s);
    }

    private void ListenerAllWifi() {
        list.clear();
        List<ScanResult> results = wifiMg.getScanResults();
        if (results.size() != 0) {
            TextView tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
            tv_wifiTip.setVisibility(View.GONE);
        } else {
            TextView tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
            tv_wifiTip.setVisibility(View.VISIBLE);
            tv_wifiTip.setText("温馨提示：您系统版本为6.0以上\n需要开启GPS后可扫描WIFI!");
        }
        int[] channelSum=new int[15];
        for (ScanResult result : results) {
            WifiInfos wifiInfos = new WifiInfos();
            WifiInfo wifiInfo = wifiMg.getConnectionInfo();
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
        String s="";
        s+="扫描到WIFI连接: "+list.size()+" 个，信道分布:\n";
        for(int i=0;i<channelSum.length;i++)
        {
            if(channelSum[i]!=0)
            {
                s+="信道"+i+" ["+channelSum[i]+"] ";
            }
        }
        tv_title_wifi.setText(s);
    }
}