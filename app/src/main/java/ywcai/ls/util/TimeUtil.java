package ywcai.ls.util;

import java.util.Calendar;

/**
 * Created by zmy_11 on 2017/6/30.
 */

public class TimeUtil {

    public  String getNowDate() {
        String nowDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return nowDate;
    }

    public  String getNowTime() {
        String nowTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
        return nowTime;
    }

    public  String getDetailTime() {
        return getNowDate() + " " + getNowTime();
    }
}
