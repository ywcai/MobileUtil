package ywcai.ls.module.components.ping.operation.model;

import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by zmy_11 on 2016/8/17.
 */
public class PingFast implements Runnable {
    private Handler myHadler;
    private PingParameter pingParameter;
    private PingResult pingResult;
    public ArrayList<Integer> delayList=new ArrayList<>();


    public PingFast(Handler handler, PingParameter pingParameter) {
        myHadler = handler;
        this.pingParameter = pingParameter;
        pingResult = new PingResult();
    }

    @Override
    public void run() {
        if (pingParameter.isWorking) {
        Ping();
        }
    }

    private void Ping() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 -s "+pingParameter.lenth+ " " + pingParameter.ipAddress);
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bufError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            int status = p.waitFor();
            if (status == 0) {
                String str = "";
                String delay = "";
                while ((str = buf.readLine()) != null) {
                    if (str.indexOf("icmp_seq=") >= 0 && str.indexOf("time=") >= 0) {
                        delay = str.split("=")[3].split(" ")[0];
                        float temp = Float.parseFloat(delay);
                        synchronized (pingResult) {
                            pingResult.send++;
                            pingResult.log = "!";
                            delayList.add(Math.round(temp));
                            pingResult.receive++;
                            pingResult.percent = (float) Math.round(pingResult.receive * 10000 / pingResult.send) / 100;
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
                        }
                        updateDetailLog(pingResult);
                        break;
                    }
                }
            } else {
                synchronized (pingResult) {
                    pingResult.send++;
                    pingResult.percent = (float) Math.round(pingResult.receive * 10000 / pingResult.send) / 100;
                    pingResult.log = ".";
                    delayList.add(0);
                    updateDetailLog(pingResult);
                }
            }
        } catch (Exception e) {
            pingParameter.isWorking = false;
            //pingResult.log = "\nPING异常:" + e ;
            //updateDetailLog(pingResult);
        }

    }

    private void updateDetailLog(PingResult pingResult) {
        Message mes = new Message();
        mes.what = 2;
        PingResult pingResultObj = new PingResult();
        pingResultObj.log = pingResult.log;
        pingResultObj.send = pingResult.send;
        pingResultObj.receive = pingResult.receive;
        pingResultObj.average = pingResult.average;
        pingResultObj.max = pingResult.max;
        pingResultObj.min = pingResult.min;
        pingResultObj.percent = pingResult.percent;
        mes.obj = pingResultObj;
        myHadler.sendMessage(mes);
    }
}
