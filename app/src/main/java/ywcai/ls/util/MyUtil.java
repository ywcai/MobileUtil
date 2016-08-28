package ywcai.ls.util;

/**
 * Created by zmy_11 on 2016/8/28.
 */
public class MyUtil {

    public static String ConvertIpToStr(int ip) {
        String ipaddr = "";
        ipaddr = (String.valueOf((ip & 0x000000FF))) + "." + String.valueOf((ip & 0x0000FFFF) >>> 8) +
                "." + String.valueOf((ip & 0x00FFFFFF) >>> 16) + "." + String.valueOf(ip >>> 24);
        return ipaddr;
    }
    public static int ConvertFrequencyToChannel(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            default:
                channel = -1;
                break;
        }
        return channel;
    }

}
