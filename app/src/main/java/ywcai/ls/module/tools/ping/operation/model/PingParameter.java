package ywcai.ls.module.tools.ping.operation.model;

/**
 * Created by zmy_11 on 2016/8/17.
 */
public class PingParameter {
    public int lenth=100;
    public int count=1;
    public int timeout=3;
    public String ipAddress="119.6.6.6";
    public Boolean sendMethod=false;
    public Boolean isWorking=false;

    @Override
    public String toString() {
        return "PingParameter{" +
                "lenth=" + lenth +
                ", count=" + count +
                ", timeout=" + timeout +
                ", ipAddress='" + ipAddress + '\'' +
                ", sendMethod=" + sendMethod +
                ", isWorking=" + isWorking +
                '}';
    }
}
