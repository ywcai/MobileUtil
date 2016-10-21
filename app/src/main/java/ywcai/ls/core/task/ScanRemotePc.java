package ywcai.ls.core.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import ywcai.ls.bean.LanInfo;
import ywcai.ls.inf.CallBackLanScanResultInf;
import ywcai.ls.util.MyConfig;


public class ScanRemotePc implements Runnable {
    public String lanIp = "",localIp="";
    CallBackLanScanResultInf callBack;

    public ScanRemotePc(String pIp, CallBackLanScanResultInf pCallBack,String pLocalIp) {
        lanIp = pIp;
        callBack = pCallBack;
        localIp=pLocalIp;
    }

    @Override
    public void run() {
        ping();
    }

    private void ping() {
        Boolean isExist = false;
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 " + lanIp);
            BufferedReader bufError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int status = p.waitFor();
            if (status == 0) {
                while (true) {
                    if ((bufError.readLine()) == null) break;
                }
                while (true) {
                    if ((buf.readLine()) == null) break;
                }
                isExist = true;
            }
        } catch (Exception ignored) {

        }
        LanInfo lanInfo=new LanInfo();
        lanInfo.lanIp=lanIp;
        lanInfo.isExist=isExist;
        if (lanInfo.isExist) {
            String lanMac = GetMac();
            String lanDevice=GetDevice(lanMac);
            lanInfo.lanMac=lanMac;
            lanInfo.lanDevice=lanDevice;
            if(!localIp.equals(lanIp)) {
                SocketTest socketTest = new SocketTest();
                lanInfo.isExist = socketTest.GetRemotePcStatus(lanIp);
                lanInfo.lanDevice="远端应用开启";
            }
        }
        callBack.UpdateIpList(lanInfo);
    }

    private String GetDevice(String mac) {
        String lanDevice = "";

        return lanDevice;
    }

    private String GetMac() {
        String lanMac = "未知";
        File arpFile = new File(MyConfig.STR_ARP_FILE_PATH);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            String temp;
            while ((temp = buffReader.readLine()) != null) {
                temp=temp.replaceAll("\\s+", " ");
                String[] result=temp.split(" ");
                if (result[0].equals(lanIp)) {
                    lanMac = result[3];
                    break;
                }
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lanMac;
    }

}
