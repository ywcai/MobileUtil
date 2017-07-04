package ywcai.ls.common.em;

/**
 * Created by zmy_11 on 2017/6/26.
 */

public enum RemoteViewUpdateType {
    LOADING(0),
    LOAD_END(1),
    QQ_LOGIN_OK(2),
    QQ_LOGIN_OUT(3),
    CONNECT_OK(4),
    CONNECT_FAIL(5),
    REG_OK(6),
    REG_FAIL(7),
    INTERNET_SESSION_CLOSED(8),
    TURN_ON(9),
    TURN_OFF(10),
    TURN_BUSY(11),
    ;
    RemoteViewUpdateType(int i ) {
    }
}
