package ywcai.ls.bean;

import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class WifiInfos {
    public String sid,mac,device,keyType;
    public int dbm,channel,frequency,speed;
    public boolean isConnWifi=false;

    @Override
    public String toString() {
        return "WifiInfos{" +
                "sid='" + sid + '\'' +
                ", mac='" + mac + '\'' +
                ", device='" + device + '\'' +
                ", keyType='" + keyType + '\'' +
                ", dbm=" + dbm +
                ", channel=" + channel +
                ", frequency=" + frequency +
                '}';
    }
}
