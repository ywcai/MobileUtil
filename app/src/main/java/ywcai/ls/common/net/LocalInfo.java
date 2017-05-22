package ywcai.ls.common.net;

import android.content.Context;
import android.net.wifi.WifiManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.util.MyUtil;
import ywcai.ls.util.MyConfig;

public class LocalInfo {
    private Context context;
    public LocalInfo(Context context) {
        this.context=context;
    }

    public  String getLocalIp() {
        String localIp = "0.0.0.0";
        try {
            WifiManager wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            localIp = MyUtil.ConvertIpToStr(wifiInfo.getIpAddress());
        } catch (Exception e) {
        }
        if (localIp.equals("0.0.0.0")) {
            localIp = ReadRecord();
        }
        return localIp;
    }
    public  String getLocalMac() {
        String mac = "null";
        File arpFile = new File(MyConfig.STR_LOCAL_MAC_FILE);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            mac = buffReader.readLine();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    //如果本机是热点，返回本机值,网关值一般默认为246
    private  String ReadRecord() {
        String localIp = "0.0.0.0";
        File arpFile = new File(MyConfig.STR_ARP_FILE_PATH);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            String temp;
            buffReader.readLine();
            if ((temp = buffReader.readLine()) != null) {
                temp = temp.replaceAll("\\s+", " ");
                String[] result = temp.split(" ");
                localIp = result[0].substring(0, result[0].lastIndexOf(".")) + ".1";
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIp;
    }
    public  String getLocalDevice() {
        return null;
    }
    public List getLocalList(String localIp)
    {
        List tempList=new ArrayList();
        HashMap<String, String> hs = new HashMap();
        hs.put("lanIp", localIp);
        hs.put("lanMac", getLocalMac());
        hs.put("lanDevice", "[本机]");
        tempList.add(hs);
        return tempList;
    }
}
