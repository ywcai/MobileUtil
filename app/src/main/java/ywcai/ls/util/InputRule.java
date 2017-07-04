package ywcai.ls.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.util.statics.MesUtil;

/**
 * Created by zmy_11 on 2017/1/5.
 */

public class InputRule {

    public  boolean checkIpRule(String ip)
    {
        String rule = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(ip);
        Boolean result=matcher.matches();
        return  result;
    }

    public  boolean checkPswRule(String psw) {
        Boolean result=true;
        return result;
    }
}
