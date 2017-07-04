package ywcai.ls.common.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.util.statics.MyConfig;

/**
 * Created by zmy_11 on 2016/12/18.
 */

public class PingTest {
    public Boolean  pingTest(String ip) {
        Boolean isExist = false;
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 " + ip);
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
        return isExist;
    }
    public String getMac(String lanIp) {
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
    public String getDevice(String mac) {
        String lanDevice = "远端应用开启";

        return lanDevice;
    }
    public List getListInfo(String ip)
    {
        List tempList=new ArrayList();
        HashMap<String, String> hs = new HashMap();
        hs.put("lanIp", ip);
        hs.put("lanMac", getMac(ip));
        hs.put("lanDevice", "[远端]");
        tempList.add(hs);
        return tempList;
    }
}
