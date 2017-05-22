package ywcai.ls.util;

/**
 * Created by zmy_11 on 2017/4/12.
 */

public class ResultCode {
    public static final  int SUCCESS_SESSION_OPEN=601;
    public static final  int SUCCESS_SESSION_CLOSE=602;

    public static final  int FAIL_SESSION_OPEN=501;
    public static final  int FAIL_WAIT_TIME_OUT=502;
    public static final  int TIME_CHECKING=401;
    public static final  int FAIL_UNKNOW=999;


    public static final  int SOCKET_RESPONSE_VALIDATE_SUCCESS=7706;
    public static final  int SOCKET_RESPONSE_VALIDATE_FAIL=7701;

    public static final  int NOTIFY_DEVICE_TURN_ON=6001;
    public static final  int NOTIFY_DEVICE_TURN_OFF=6002;
    public static final  int NOTIFY_DEVICE_LINK_UP=6667;
    public static final  int NOTIFY_DEVICE_LINK_DOWN=6668;

    public static final  int JSON_TYPE_CHECK_PSW=8000;
    public static final  int JSON_TYPE_REQUEST_LINK=8001;
    public static final  int JSON_TYPE_CUT_LINK=8002;
    public static final  int JSON_TYPE_CONTROL_CMD=9000;
}
