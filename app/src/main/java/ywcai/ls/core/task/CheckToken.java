package ywcai.ls.core.task;

import ywcai.ls.inf.CallBackLanScanResultInf;

/**
 * Created by zmy_11 on 2016/10/19.
 */
public class CheckToken implements Runnable {
    private String psw;
    private String serverIp;
    private CallBackLanScanResultInf callback;
    public CheckToken(String pPsw, String pServerIp, CallBackLanScanResultInf pCallBack)
    {
        psw=pPsw;
        serverIp=pServerIp;
        callback=pCallBack;
    }
    @Override
    public void run() {
        SendToken();
    }

    private void SendToken() {
        
        //do
        //callback do
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.CallBackCheckResult(false,"identity");
    }
}
