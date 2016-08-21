package ywcai.ls.task;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ywcai.ls.bean.PingParameter;
import ywcai.ls.bean.PingResult;


/**
 * Created by zmy_11 on 2016/8/17.
 */
public class PingNormal implements Runnable {
    private Handler myHadler;
    private PingParameter pingParameter;
    private PingResult pingResult;
    private Runtime r = Runtime.getRuntime();
    private Process p;


    public PingNormal(Handler handler, PingParameter pingParameter) {
        myHadler = handler;
        this.pingParameter = pingParameter;
        pingResult = new PingResult();

    }

    @Override
    public void run() {
        while (pingParameter.isWorking)
        {
            Ping();
        }
        pingResult.log="\n----PING OVER !----";
        updateEnd(pingResult);
    }

    private void Ping() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 -s "+pingParameter.lenth+ " " + pingParameter.ipAddress);
            BufferedReader bufError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int status = p.waitFor();
            pingResult.send++;
            if (status == 0) {
                pingResult.receive++;
                String str = "";
                String delay = "";
                while ((str = buf.readLine()) != null) {
                    pingResult.percent = pingResult.receive * 100 / pingResult.send;
                    if (str.indexOf("icmp_seq=") >= 0 && str.indexOf("time=") >= 0) {
                        delay = str.split("=")[3].split(" ")[0];
                        float temp = Float.parseFloat(delay);
                        if (pingResult.receive == 1) {
                            pingResult.max = temp;
                            pingResult.min = temp;
                            pingResult.average = temp;
                        }
                        if (pingResult.receive > 1) {
                            pingResult.min = temp < pingResult.min ? temp : pingResult.min;
                            pingResult.max = temp > pingResult.max ? temp : pingResult.max;
                            pingResult.average = (float) Math.round((pingResult.average * (pingResult.receive - 1) + temp) * 100 / pingResult.receive) / 100;
                        }
                        pingResult.percent = (float) Math.round(pingResult.receive * 10000 / pingResult.send) / 100;
                        pingResult.log=delay + "ms ";
                        updateDetailLog(pingResult);
                        break;
                    }
                }
            } else {
                pingResult.percent = (float) Math.round(pingResult.receive * 10000 / pingResult.send) / 100;
                pingResult.log="×";
                updateDetailLog(pingResult);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            pingParameter.isWorking = false;
            pingResult.log="\nPING错误:" + e.toString()+"\n";
            updateDetailLog(pingResult);
        }
    }

    private void updateDetailLog(PingResult pingResult) {
        Message mes = new Message();
        mes.what = 0;
        mes.obj = pingResult;
        myHadler.sendMessage(mes);
    }

    private void updateEnd(PingResult pingResult) {
        Message mes = new Message();
        mes.what = 1;
        mes.obj = pingResult;
        myHadler.sendMessage(mes);
    }

}
