package ywcai.ls.module.tools.wifi.model;

public class WifiInfo {
    public String sid,mac,device,keyType;
    public int dbm,channel,frequency,speed;
    public boolean isConnWifi=false;

    @Override
    public String toString() {
        return "WifiInfo{" +
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
