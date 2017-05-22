package ywcai.ls.module.mouse.model.net;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.common.net.PingTest;


public class ScanCoreProcess{
    public  String ipNet;
    private int ipNum;


    public ScanCoreProcess(String localIp) {
        String[] temp=localIp.split("\\.");
        ipNet= temp[0]+"."+temp[1]+"."+temp[2]+".";
        ipNum=Integer.parseInt(temp[3]);
    }

    public List scan(int i) {
        List list=new ArrayList();
        if(ipNum==i)
        {
            return list;
        }
        String lanIp=ipNet+i;
        PingTest pingTest =new PingTest();
        if(!pingTest.pingTest(ipNet+i))
        {
            return list;
        }
        SocketTest socketTest = new SocketTest();
        if(socketTest.GetRemotePcStatus(lanIp))
        {
            String mac= pingTest.getMac(lanIp);
            String device= pingTest.getDevice(mac);
            HashMap hs=new HashMap();
            hs.put("lanIp",lanIp);
            hs.put("lanMac",mac);
            hs.put("lanDevice",device);
            list.add(hs);
        }
        return list;
    }


}
