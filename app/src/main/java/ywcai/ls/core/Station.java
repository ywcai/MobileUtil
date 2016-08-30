package ywcai.ls.core;

import android.content.Context;

import android.support.v7.widget.ListViewCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.SimpleAdapter;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.ui.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Station implements UpdateViewInf {

    private int lac = 0, cid = 0, rsp = 0, netType = 0;
    private String mobileNum = "null", driveId = "null", simSerialNumber = "null";
    private String networkOperator = "null", networkOperatorName = "null";
    private String net = "null";
    private List<HashMap<String, String>> list;
    private TelephonyManager telephonyManager;
    private SimpleAdapter adpter;
    private View tabView;

    public Station(View view) {
        telephonyManager = (TelephonyManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            driveId = telephonyManager.getDeviceId();
        } catch (Exception e) {
            driveId="null";
        }
        try
        {
            netType = telephonyManager.getNetworkType();
            simSerialNumber = telephonyManager.getSimSerialNumber().equals("") ? "null" : telephonyManager.getSimSerialNumber();
            mobileNum = telephonyManager.getLine1Number().equals("") ? "null" : telephonyManager.getLine1Number();
            networkOperator = telephonyManager.getNetworkOperator().equals("") ? "null" : telephonyManager.getNetworkOperator();
            networkOperatorName = telephonyManager.getNetworkOperatorName().equals("") ? "null" : telephonyManager.getNetworkOperatorName();
            telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CELL_LOCATION);
            telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        catch (Exception e)
        {
            netType = 0;
            simSerialNumber ="null";
            mobileNum = "null";
            networkOperator = "null";
            networkOperatorName = "null";
        }
        tabView = view;
        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_station);
        list = new ArrayList<HashMap<String, String>>();
        list = getList();
        adpter = new SimpleAdapter(MyApplication.getInstance().getApplicationContext(), list, R.layout.listview_station, new String[]{"title", "info"}, new int[]{R.id.title_station, R.id.info_station});
        listViewCompat.setAdapter(adpter);
    }

    public List<HashMap<String, String>> getList() {
        String[] strs = toString().split(",");
        if (list.size() > 0) {
            list.clear();
        }
        for (int n = 0; n < strs.length; n++) {
            HashMap<String, String> hp = new HashMap<String, String>();
            hp.put("title", strs[n].split("=")[0]);
            if( strs[n].split("=").length>=2)
            {
                hp.put("info", strs[n].split("=")[1]);
            }
            else
            {
                hp.put("info", "null");
            }
            list.add(hp);
        }
        return list;
    }

    @Override
    public void updateView() {
        list = getList();
        adpter.notifyDataSetChanged();
    }

    private class PhoneListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            Class<? extends SignalStrength> clazz = signalStrength.getClass();
            Field Lte_rsp = null;
            Field Wcdma_rsp = null;
            try {
                netType = telephonyManager.getNetworkType();
                Lte_rsp = clazz.getDeclaredField("mLteRsrp");
                Lte_rsp.setAccessible(true);
                Wcdma_rsp = clazz.getDeclaredField("mWcdmaRscp");
                Wcdma_rsp.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (netType == TelephonyManager.NETWORK_TYPE_EDGE || netType == TelephonyManager.NETWORK_TYPE_GPRS) {
                rsp = signalStrength.getGsmSignalStrength();//2G
            } else if (netType == TelephonyManager.NETWORK_TYPE_LTE) {

                try {
                    rsp = (Integer) Lte_rsp.getInt(signalStrength);
                } catch (Exception e) {

                }
            } else {
                try {
                    rsp = (Integer) Wcdma_rsp.getInt(signalStrength);
                } catch (Exception e) {

                }
            }
            updateView();
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            try {
                netType = telephonyManager.getNetworkType();
                simSerialNumber = telephonyManager.getSimSerialNumber().equals("") ? "null" : telephonyManager.getSimSerialNumber();
                mobileNum = telephonyManager.getLine1Number().equals("") ? "null" : telephonyManager.getLine1Number();
                networkOperator = telephonyManager.getNetworkOperator().equals("") ? "null" : telephonyManager.getNetworkOperator();
                networkOperatorName = telephonyManager.getNetworkOperatorName().equals("") ? "null" : telephonyManager.getNetworkOperatorName();
                lac = ((GsmCellLocation) location).getLac();
                cid = ((GsmCellLocation) location).getCid();
            } catch (Exception e) {
                netType = 0;
                simSerialNumber ="null";
                mobileNum = "null";
                networkOperator = "null";
                networkOperatorName = "null";
                lac = 0;
                cid = 0;
            }
            updateView();
        }
    }

    @Override
    public String toString() {
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                net = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                net = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                net = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                net = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                net = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                net = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                net = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                net = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                net = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                net = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                net = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                net = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                net = "UMTS";
                break;
            default:
                net = "UNKNOWN";
                break;
        }
        return
                "运营商=" + networkOperatorName +
                        ",网络码=" + networkOperator +
                        ",地区码=" + lac +
                        ",小区码=" + cid +
                        ",信号强度=" + rsp + " db" +
                        ",网络制式=" + net +
                        ",手机号=" + mobileNum +
                        ",设备号=" + driveId +
                        ",号卡串号=" + simSerialNumber
                //",strengths=" + strengths  +
                ;
    }
}
