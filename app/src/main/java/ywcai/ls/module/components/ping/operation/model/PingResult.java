package ywcai.ls.module.components.ping.operation.model;

/**
 * Created by zmy_11 on 2016/8/16.
 */
public class PingResult {
    public int send = 0;
    public int receive = 0;
    public float percent = 0;
    public float max = 0;
    public float min = 0;
    public float average = 0;
    public String log = "";

    @Override
    public String toString() {
        return "PingResult{" +
                "send=" + send +
                ", receive=" + receive +
                ", percent=" + percent +
                ", max=" + max +
                ", min=" + min +
                ", average=" + average +
                ", log='" + log + '\'' +
                '}';
    }
}
